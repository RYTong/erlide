%% ``The contents of this file are subject to the Erlang Public License,
%% Version 1.1, (the "License"); you may not use this file except in
%% compliance with the License. You should have received a copy of the
%% Erlang Public License along with this software. If not, it can be
%% retrieved via the world wide web at http://www.erlang.org/.
%% 
%% Software distributed under the License is distributed on an "AS IS"
%% basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
%% the License for the specific language governing rights and limitations
%% under the License.
%% 
%% The Initial Developer of the Original Code is Ericsson Utvecklings AB.
%% Portions created by Ericsson are Copyright 1999, Ericsson Utvecklings
%% AB. All Rights Reserved.''
%% 
%%     $Id$
%%
-module(erlide_dbg_ieval).

-export([eval/3,exit_info/5]).
-export([eval_expr/3]).
-export([check_exit_msg/3,exception/4,in_use_p/2]).
-export([stack_level/0, bindings/1, stack_frame/2, backtrace/1, all_frames/0, all_modules_on_stack/0]).

-include("erlide_dbg_ieval.hrl").

-import(lists, [foldl/3,flatmap/2]).

%%====================================================================
%% External exports
%%====================================================================

%%--------------------------------------------------------------------
%% eval(Mod, Func, Args) -> Meta
%%   Mod = Func = atom()
%%   Args = [term()]
%%   MFA = {Mod,Func,Args} | {Mod,Func,Arity} | {Fun,Args}
%%   Arity = integer()
%%   Meta = pid()
%% Entry point from debugged process (erlide_dbg_debugged).
%% Immediately returns the pid for the meta process.
%% The evaluated value will later be sent as a message to
%% the calling process.
%%--------------------------------------------------------------------
eval(Mod, Func, Args) ->
    Debugged = self(),
    log({ieval_eval, {mf, Mod, Func, {debugged, Debugged}}}),
    Int = erlide_dbg_iserver:find(),
    case erlide_dbg_iserver:call(Int, {get_meta,Debugged}) of
	{ok,Meta} ->
	    Meta ! {re_entry, Debugged, {eval,{Mod,Func,Args}}},
	    Meta;
	{error,not_interpreted} ->
	    spawn(fun() ->
			  meta(Int, Debugged, Mod, Func, Args)
		  end)
    end.

%%--------------------------------------------------------------------
%% exit_info(Int, AttPid, OrigPid, Reason, ExitInfo)
%%  Int = AttPid = OrigPid = pid()
%%  Reason = term()
%%  ExitInfo = {{Mod,Line}, Bs, Stack} | {}
%% Meta process started when attaching to a terminated process.
%% Spawned (by erlide_dbg_iserver) in response to user request.
%%--------------------------------------------------------------------
exit_info(Int, AttPid, OrigPid, Reason, ExitInfo) ->
    put(int, Int),
    put(attached, AttPid),
    put(breakpoints, erlide_dbg_iserver:call(Int, all_breaks)),
    put(self, OrigPid),
    put(exit_info, ExitInfo),
    
    case ExitInfo of
	{{Mod,Line},Bs,S} ->
	    Stack = binary_to_term(S),
	    put(stack, Stack),
	    Le = stack_level(Stack),
	    erlide_dbg_icmd:tell_attached({exit_at, {Mod, Line}, Reason, Le, OrigPid, all_frames(Stack), Bs}),
	    exit_loop(OrigPid, Reason, Bs,#ieval{module=Mod,line=Line});
	{} ->
	    put(stack, []),
	    erlide_dbg_icmd:tell_attached({exit_at, null, Reason, 1, OrigPid}),
	    exit_loop(OrigPid, Reason, erl_eval:new_bindings(),#ieval{})
    end.

%%--------------------------------------------------------------------
%% eval_expr(Expr, Bs, Ieval) -> {value, Value, Bs}
%%
%% Evalute a shell expression in the real process.
%% Called (erlide_dbg_icmd) in response to a user request.
%%--------------------------------------------------------------------
eval_expr(Expr, Bs, Ieval) ->

    %% Save current exit info
    ExitInfo = get(exit_info),
    Stacktrace = get(stacktrace),

    %% Emulate a surrounding catch
    try debugged_cmd({eval,Expr,Bs}, Bs, Ieval)
    catch
	Class:Reason ->
	    Result = case Class of
			 throw -> Reason;
			 _ -> {'EXIT', Reason}
		     end,

	    %% Reset exit info
	    put(exit_info, ExitInfo),
	    put(stacktrace, Stacktrace),

	    {value, Result, Bs}
    end.

%%--------------------------------------------------------------------
%% check_exit_msg(Msg, Bs, Ieval)
%%   Msg = term()
%% Check if Msg is an 'EXIT' msg from the iserver or a 'DOWN' msg
%% from the debugged process. If so exit with correct reason.
%%--------------------------------------------------------------------
check_exit_msg({'EXIT', Int, Reason}, _Bs, #ieval{level=Le}) ->
    %% This *must* be interpreter which has terminated,
    %% we are not linked to anyone else
    if
	Le==1 ->
	    exit(Reason);
	Le>1 ->
	    exit({Int, Reason})
    end;
check_exit_msg({'DOWN',_,_,_,Reason}, Bs,
	       #ieval{level=Le, module=Mod, line=Li}) ->
    %% This *must* be Debugged which has terminated,
    %% we are not monitoring anyone else

    %% Inform Int about current position, bindings and stack
    ExitInfo =
	case get(exit_info) of

	    %% Debugged has been terminated by someone
	    %% - really the position, bindings and stack are of no
	    %%   importance in this case
	    %% If we don't save them, however, post-mortem analysis
	    %% of the process isn't possible
	    undefined when Le==1 -> % died outside interpreted code
		{};
	    undefined when Le>1 ->
		StackBin = term_to_binary(get(stack)),
		{{Mod, Li}, Bs, StackBin};

	    %% Debugged has terminated due to an exception
	    ExitInfo0 ->
		ExitInfo0
	end,
    erlide_dbg_iserver:cast(get(int), {set_exit_info,self(),ExitInfo}),

   if
	Le==1 ->
	    exit(Reason);
	Le>1 ->
	    exit({get(self), Reason})
    end;
check_exit_msg(_Msg, _Bs, _Ieval) ->
    ignore.

%%--------------------------------------------------------------------
%% exception(Class, Reason, Bs, Ieval)
%%   Class = error | exit | throw
%%   Reason = term()
%%   Bs = bindings()
%%   Ieval = #ieval{}
%% Store information about where in the code the error is located
%% and then raise the exception.
%%--------------------------------------------------------------------
exception(Class, Reason, Bs, Ieval) ->
    exception(Class, Reason, fix_stacktrace(1), Bs, Ieval).

exception(Class, Reason, Stacktrace, Bs, #ieval{module=M, line=Line}) ->
    ExitInfo = {{M,Line}, Bs, term_to_binary(get(stack))},
    put(exit_info, ExitInfo),
    put(stacktrace, Stacktrace),
    erlang:Class(Reason).

%%--------------------------------------------------------------------
%% in_use_p(Mod, Cm) -> boolean()
%%   Mod = Cm = atom()
%% Returns true if Mod is found on the stack, otherwise false.
%%--------------------------------------------------------------------
in_use_p(Mod, Mod) -> true;
in_use_p(Mod, _Cm) ->
    case get(trace_stack) of
	false -> true;
	_ -> %  all | no_tail
	    lists:any(fun({_,{M,_,_,_}}) when M =:= Mod -> true;
			 (_) -> false
		      end,
		      get(stack))
    end.

%%====================================================================
%% Internal functions
%%====================================================================

%%--Loops-------------------------------------------------------------

%% Entry point for first-time initialization of meta process
meta(Int, Debugged, M, F, As) ->
    process_flag(trap_exit, true),
    erlang:monitor(process, Debugged),

    %% Inform erlide_dbg_iserver, get the initial status in return
    Pargs = case {M, F} of
		%% If it's a fun we're evaluating, show a text
		%% representation of the fun and its arguments,
		%% not erlide_dbg_ieval:eval_fun(...)
		{erlide_dbg_ieval, eval_fun} ->
		    {Mx, Fx} = lists:last(As),
		    {Mx, Fx, lists:nth(2, As)};
		_ ->
		    {M, F, As}
	    end,
    log({ieval_meta, {self_meta, self()}, {debugged, Debugged}, {mf, M, F}, As}),
    Status = erlide_dbg_iserver:call(Int, {new_process,Debugged,self(),Pargs}),
    
    %% Initiate process dictionary
    put(int, Int),           % pid() erlide_dbg_iserver
    put(attached, undefined),% pid() attached process
    put(breakpoints, erlide_dbg_iserver:call(Int, all_breaks)),
    put(cache, []),
    put(next_break, Status), % break | running (other values later)
    put(self, Debugged),     % pid() interpreted process
    put(stack, []),
    put(stacktrace, []),
    put(trace_stack, erlide_dbg_iserver:call(Int, get_stack_trace)),
    put(trace, false),       % bool() Trace on/off
    put(user_eval, []),


    %% Send the result of the meta process
    Ieval = #ieval{},
    Debugged ! {sys, self(), eval_mfa(Debugged,M,F,As,Ieval)},

    erlide_dbg_iserver:cast(Int, {set_status, self(), idle, {}}),
    erlide_dbg_icmd:tell_attached(idle),

    meta_loop(Debugged, erl_eval:new_bindings(), Ieval).

debugged_cmd(Cmd, Bs, Ieval) ->
    Debugged = get(self),
    Stacktrace = fix_stacktrace(2),
    Debugged ! {sys, self(), {command,Cmd,Stacktrace}},
    meta_loop(Debugged, Bs, Ieval).

meta_loop(Debugged, Bs, #ieval{level=Le} = Ieval) ->
    receive

	%% The following messages can only be received when Meta is
	%% waiting for Debugged to evaluate non-interpreted code
	%% or a Bif. Le>1
	{sys, Debugged, {value,Val}} ->
	    {value, Val, Bs};
	{sys, Debugged, {value,Val,Bs2}} ->
	    {value, Val, Bs2};
	{sys, Debugged, {exception,{Class,Reason,Stacktrace}}} ->
	    case get(exit_info) of

		%% Error occured outside interpreted code
		undefined ->
		    exception(Class,Reason,Stacktrace,Bs,Ieval);

		%% Error must have occured within a re-entry to
		%% interpreted code, simply raise the exception
		_ ->
		    erlang:Class(Reason)
	    end;

	%% Re-entry to Meta from non-interpreted code
	{re_entry, Debugged, {eval,{M,F,As}}} when Le==1 ->
	    %% Reset process dictionary
	    %% This is really only necessary if the process left
	    %% interpreted code at a call level > 1
	    put(stack, []),
	    put(stacktrace, []),
	    put(exit_info, undefined),
	    
	    erlide_dbg_iserver:cast(get(int), {set_status,self(),running,{}}),
	    erlide_dbg_icmd:tell_attached(running),

	    %% Tell attached process(es) to update source code.
	    erlide_dbg_icmd:tell_attached({re_entry,M,F}),

	    %% Send the result of the meta process
	    Debugged ! {sys,self(),eval_mfa(Debugged,M,F,As,Ieval)},

	    erlide_dbg_iserver:cast(get(int), {set_status,self(),idle,{}}),
	    erlide_dbg_icmd:tell_attached(idle),
	    meta_loop(Debugged, Bs, Ieval);

	%% Evaluation in Debugged results in call to interpreted
	%% function (probably? a fun)
	{re_entry, Debugged, {eval,{M,F,As}}} when Le>1 ->
	    Ieval2 = Ieval#ieval{module=undefined, line=-1},
	    Debugged ! {sys,self(),eval_mfa(Debugged,M,F,As,Ieval2)},
	    meta_loop(Debugged, Bs, Ieval);

	Msg ->
	    check_exit_msg(Msg, Bs, Ieval),
	    erlide_dbg_icmd:handle_msg(Msg, idle, Bs, Ieval),
	    meta_loop(Debugged, Bs, Ieval)
    end.

exit_loop(OrigPid, Reason, Bs, Ieval) ->
    receive
	Msg ->
	    check_exit_msg(Msg, Bs, Ieval),
	    erlide_dbg_icmd:handle_msg(Msg, exit_at, Bs, Ieval),
	    exit_loop(OrigPid, Reason, Bs, Ieval)
    end.

%%--Stack emulation---------------------------------------------------

%% We keep track of a call stack that is used for
%%  1) saving stack frames that can be inspected from an Attached
%%     Process GUI (using erlide_dbg_icmd:get(Meta, stack_frame, {Dir, SP})
%%  2) generate an approximation of regular stacktrace -- sent to
%%     Debugged when it should raise an exception or evaluate a
%%     function (since it might possible raise an exception)
%%
%% Stack = [Entry]
%%   Entry = {Le, {MFA, Where, Bs}}
%%     Le = int()         % current call level
%%     MFA = {M,F,Args}   % called function (or fun)
%%         | {Fun,Args}   %
%%     Where = {M,Li}     % from where (module+line) function is called
%%     Bs = bindings()    % current variable bindings
%%
%% How to push depends on the "Stack Trace" option (value saved in
%% process dictionary item 'trace_stack').
%%   all - everything is pushed
%%   no_tail - tail recursive push
%%   false - nothing is pushed
%% Whenever a function returns, the corresponding call frame is popped.

push(MFA, Bs, #ieval{level=Le,module=Cm,line=Li,last_call=Lc}) ->
    Entry = {Le, {MFA, {Cm,Li}, Bs}},
    case get(trace_stack) of
	false -> ignore;
	no_tail when Lc ->
	    case get(stack) of
		[] -> put(stack, [Entry]);
		[_Entry|Entries] -> put(stack, [Entry|Entries])
	    end;
	_ -> % all | no_tail when Lc==false
	    put(stack, [Entry|get(stack)])
    end.

pop() ->
    case get(trace_stack) of
	false -> ignore;
	_ -> % all � no_tail
	    case get(stack) of
		[_Entry|Entries] ->
		    put(stack, Entries);
		[] ->
		    ignore
	    end
    end.

pop(Le) ->
    case get(trace_stack) of
	false -> ignore;
	_ -> % all | no_tail
	    put(stack, pop(Le, get(stack)))
    end.

pop(Level, [{Le, _}|Stack]) when Level=<Le ->
    pop(Level, Stack);
pop(_Level, Stack) ->
    Stack.


%% stack_level() -> Le
%% stack_level(Stack) -> Le
%% Top call level
stack_level() ->
    stack_level(get(stack)).

stack_level([]) -> 1;
stack_level([{Le,_}|_]) -> Le.

%% fix_stacktrace(Start) -> Stacktrace
%%   Start = 1|2
%%   Stacktrace = [{M,F,Args|Arity} | {Fun,Args}]
%% Convert internal stack format to imitation of regular stacktrace.
%% Max three elements, no repeated (recursive) calls to the same
%% function and convert argument lists to arity for all but topmost
%% entry (and funs).
%% 'Start' indicates where at get(stack) to start. This somewhat ugly
%% solution is because fix_stacktrace has two uses: 1) to imitate
%% the stacktrace in the case of an exception in the interpreted code,
%% in which case the current call (top of the stack = first of the list)
%% should be included, and 2) to send a current stacktrace to Debugged
%% when evaluation passes into non-interpreted code, in which case
%% the current call should NOT be included (as it is Debugged which
%% will make the actual function call).
fix_stacktrace(Start) ->
    case fix_stacktrace2(sublist(get(stack), Start, 3)) of
	[] ->
	    [];
	[H|T] ->
	    [H|args2arity(T)]
    end.

sublist([], _Start, _Length) ->
    []; % workaround, lists:sublist([],2,3) fails
sublist(L, Start, Length) ->
    lists:sublist(L, Start, Length).

fix_stacktrace2([{_,{{M,F,As1},_,_}}, {_,{{M,F,As2},_,_}}|_])
  when length(As1)==length(As2) ->
    [{M,F,As1}];
fix_stacktrace2([{_,{{Fun,As1},_,_}}, {_,{{Fun,As2},_,_}}|_])
  when length(As1)==length(As2) ->
    [{Fun,As1}];
fix_stacktrace2([{_,{MFA,_,_}}|Entries]) ->
    [MFA|fix_stacktrace2(Entries)];
fix_stacktrace2([]) ->
    [].

args2arity([{M,F,As}|Entries]) when is_list(As) ->
    [{M,F,length(As)}|args2arity(Entries)];
args2arity([Entry|Entries]) ->
    [Entry|args2arity(Entries)];
args2arity([]) ->
    [].

all_frames() ->
    {all_frames(get(stack)), saved_frames()}.

all_frames(Stack) ->
    [{{M, F, args2arity(As)}, Wh, orddict:to_list(Bs), X} || {X, {{M, F, As}, Wh, Bs}} <- Stack].

all_modules_on_stack() ->
    all_modules_on_stack(get(stack)).

all_modules_on_stack(Stack) ->
    [M || {_X, {{M, _F, _As}, _Wh, _Bs}} <- Stack].

saved_frames() ->
%%     erlang:display(hej),
    Debugged = get(self),
%%     erlang:display(Debugged),
    Debugged ! {sys, self(), get_saved_stacktrace},
    receive
	{sys, Debugged, M} ->
	    M
    end.

%% bindings(SP) -> Bs
%%   SP = Le  % stack pointer
%% Return the bindings for the specified call level
bindings(SP) ->
    bindings(SP, get(stack)).

bindings(SP, [{SP,{_MFA,_Wh,Bs}}|_]) ->
    Bs;
bindings(SP, [_Entry|Entries]) ->
    bindings(SP, Entries);
bindings(_SP, []) ->
    erl_eval:new_bindings().

%% stack_frame(Dir, SP) -> {Le, Where, Bs} | top | bottom
%%   Dir = up | down
%%   Where = {Cm, Li}
%%     Cm = Module | undefined  % module
%%     Li = int()  | -1         % line number
%%     Bs = bindings()
%% Return stack frame info one step up/down from given stack pointer
%%  up = to lower call levels
%%  down = to higher call levels
stack_frame(up, SP) ->
    stack_frame(SP, up, get(stack));
stack_frame(down, SP) ->
    stack_frame(SP, down, lists:reverse(get(stack))).

stack_frame(SP, up, [{Le, {_MFA,Where,Bs}}|_]) when Le<SP ->
    {Le, Where, Bs};
stack_frame(SP, down, [{Le, {_MFA,Where,Bs}}|_]) when Le>SP ->
    {Le, Where, Bs};
stack_frame(SP, Dir, [{SP, _}|Stack]) ->
    case Stack of
	[{Le, {_MFA,Where,Bs}}|_] ->
	    {Le, Where, Bs};
	[] when Dir==up ->
	    top;
	[] when Dir==down ->
	    bottom
    end;
stack_frame(SP, Dir, [_Entry|Stack]) ->
    stack_frame(SP, Dir, Stack).

%% backtrace(HowMany) -> Backtrace
%%   HowMany = all | int()
%%   Backtrace = {Le, MFA}
%% Return all/the last N called functions, in reversed call order
backtrace(HowMany) ->
    Stack = case HowMany of
		all -> get(stack);
		N -> lists:sublist(get(stack), N)
	    end,
    [{Le, MFA} || {Le,{MFA,_Wh,_Bs}} <- Stack].

%%--Trace function----------------------------------------------------

%%--------------------------------------------------------------------
%% trace(What, Args)
%%   What = send | receivex | received | call | return | bif
%%   Args depends on What, see the code.
%%--------------------------------------------------------------------
trace(What, Args) ->
    trace(What, Args, get(trace)).

trace(return, {_Le,{dbg_apply,_,_,_}}, _Bool) ->
    ignore;
trace(What, Args, true) ->
%%     Str = case What of
%% 	      send ->
%% %% 		  {To,Msg} = Args,
%% %% 		  {send, To, Msg};
%% %% 		  io_lib:format("==> ~w : ~p~n", [To, Msg]);
%% 	      receivex ->
%% 		  {Le, TimeoutP} = Args,
%% 		  {'receive', Le, TimeoutP};
%% %% 		  Tail = case TimeoutP of
%% %% 			     true -> "with timeout~n";
%% %% 			     false -> "~n"
%% %% 			 end,
%% %% 		  io_lib:format("   (~w) receive " ++ Tail, [Le]);
%% %% 		      
%% 	      received when Args==null ->
%% 		  {received};
%% %% 		  io_lib:format("~n", []);
%% 	      received -> % Args=Msg
%% 		  {received, Args};
%% %% 		  io_lib:format("~n<== ~p~n", [Args]);
%% %% 		      
%% 	      call ->
%% 		  {Called, {Le,Li,M,F,As}} = Args,
%% 		  {call, Called, {Le, Li, M, F, As}};
%% %% 		  case Called of
%% %% 		      extern ->
%% %% 			  io_lib:format("++ (~w) <~w> ~w:~w~s~n",
%% %% 					[Le,Li,M,F,format_args(As)]);
%% %% 		      local ->
%% %% 			  io_lib:format("++ (~w) <~w> ~w~s~n",
%% %% 					[Le,Li,F,format_args(As)])
%% %% 		  end;
%% 	      call_fun ->
%% 		  {Le,Li,F,As} = Args,
%% 		  {call_fun, Le, Li, F, As};
%% %% 	      io_lib:format("++ (~w) <~w> ~w~s~n",
%% %% 			    [Le, Li, F, format_args(As)]);
%% 	      return ->
%% 		  {Le,Val} = Args,
%% 		  {return, Le, Val};
%% %% 		  io_lib:format("-- (~w) ~p~n", [Le, Val]);
%% 	      bif ->
%% 		  {Le,Li,M,F,As} = Args,
%% 		  {bif, Le, Li, M, F, As}
%% %% 		  io_lib:format("++ (~w) <~w> ~w:~w~s~n",
%% %% 				[Le, Li, M, F, format_args(As)])
%% 	  end,
%%     erlide_dbg_icmd:tell_attached({trace_output, Str});
    erlide_dbg_icmd:tell_attached({trace_output, {What, Args}});
trace(_What, _Args, false) ->
    ignore.

%% format_args(As) when is_list(As) ->
%%     [$(,format_args1(As),$)];
%% format_args(A) ->
%%     [$/,io_lib:format('~p', [A])].
%% 
%% format_args1([A]) ->
%%     [io_lib:format('~p', [A])];
%% format_args1([A|As]) ->
%%     [io_lib:format('~p', [A]),$,|format_args1(As)];
%% format_args1([]) ->
%%     [].

%%--Other useful functions--------------------------------------------

%% Mimic catch behaviour
catch_value(error, Reason) ->
    {'EXIT',{Reason,get(stacktrace)}};
catch_value(exit, Reason) ->
    {'EXIT',Reason};
catch_value(throw, Reason) ->
    Reason.

%%--Code interpretation-----------------------------------------------

%%--------------------------------------------------------------------
%% Top level function of meta evaluator. 
%% Return message to be replied to the target process.
%%--------------------------------------------------------------------
eval_mfa(Debugged, M, F, As, Ieval) ->
    Int = get(int),
    Bs = erl_eval:new_bindings(),
    try eval_function(M,F,As,Bs,extern,Ieval#ieval{last_call=true}) of
	{value, Val, _Bs} ->
	    {ready, Val}
    catch
	exit:{Debugged, Reason} ->
	    exit(Reason);
	exit:{Int, Reason} ->
	    exit(Reason);
	Class:Reason ->
	    {exception, {Class, Reason, get(stacktrace)}}
    end.

eval_function(Mod, Fun, As0, Bs0, _Called, Ieval) when is_function(Fun);
						       Mod==?MODULE,
						       Fun==eval_fun ->
    #ieval{level=Le, last_call=Lc} = Ieval,
    case lambda(Fun, As0) of
	{Cs,Module,Name,As,Bs} ->
	    push({Module,Name,As}, Bs0, Ieval),
	    trace(call_fun, {Ieval, {Name, As, Bs}}),
	    {value, Val, _Bs} =
		fnk_clauses(Cs, Module, Name, As, Bs,
			    Ieval#ieval{level=Le+1}),
	    pop(),
	    trace(return, {Ieval, {Val}}),
	    {value, Val, Bs0};

	not_interpreted when Lc -> % We are leaving interpreted code
	    trace(call_fun, {Ieval, {Fun, As0, Bs0}}),
	    {value, {dbg_apply,erlang,apply,[Fun,As0]}, Bs0};
	not_interpreted ->
	    push({Fun,As0}, Bs0, Ieval),
	    trace(call_fun, {Ieval, {Fun, As0, Bs0}}),
	    {value, Val, _Bs} =
		debugged_cmd({apply,erlang,apply,[Fun,As0]},Bs0,
			     Ieval#ieval{level=Le+1}),
	    pop(),
	    trace(return, {Ieval, {Val}}),
	    {value, Val, Bs0};

	{error,Reason} ->
	    %% It's ok not to push anything in this case, the error
	    %% reason contains information about the culprit
	    %% ({badarity,{{Mod,Name},As}})
	    trace(error, {Ieval, {Reason, Bs0}}),
	    exception(error, Reason, Bs0, Ieval)
    end;
eval_function(Mod, Name, As0, Bs0, Called, Ieval) ->
    #ieval{level=Le, last_call=Lc} = Ieval,

    push({Mod,Name,As0}, Bs0, Ieval),
    trace(call, {Ieval, {Mod, Name, As0, Bs0}}),

    case get_function(Mod, Name, As0, Called) of
	Cs when is_list(Cs) ->
	    {value, Val, _Bs} =
		fnk_clauses(Cs, Mod, Name, As0, erl_eval:new_bindings(),
			    Ieval#ieval{level=Le+1}),
	    pop(),
	    trace(return, {Ieval, {Val}}),
	    {value, Val, Bs0};

	not_interpreted when Lc -> % We are leaving interpreted code
	    {value, {dbg_apply,Mod,Name,As0}, Bs0};
	not_interpreted ->
	    {value, Val, _Bs} =
		debugged_cmd({apply,Mod,Name,As0}, Bs0,
			     Ieval#ieval{level=Le+1}),
	    pop(),
	    trace(return, {Ieval, {Val}}),
	    {value, Val, Bs0};

	undef ->
	    exception(error, undef, Bs0, Ieval)
    end.

lambda(eval_fun, [Cs,As,Bs,{Mod,Name}=F]) ->
    %% Fun defined in interpreted code, called from outside
    if 
	length(element(3,hd(Cs))) == length(As) ->
	    db_ref(Mod),  %% Adds ref between module and process
	    {Cs,Mod,Name,As,Bs};
	true -> 
	    {error,{badarity,{F,As}}}
    end;
lambda(Fun, As) when is_function(Fun) ->
    %% Fun called from within interpreted code...
    case erlang:fun_info(Fun, module) of

	%% ... and the fun was defined in interpreted code
	{module, ?MODULE} ->
	    {env, [{Mod,Name},Bs,Cs]} = erlang:fun_info(Fun, env),
	    {arity, Arity} = erlang:fun_info(Fun, arity),
	    if 
		length(As) == Arity ->
		    db_ref(Mod), %% Adds ref between module and process
		    {Cs,Mod,Name,As,Bs};
		true ->
		    {error,{badarity,{Fun,As}}}
	    end;

	%% ... and the fun was defined outside interpreted code
	_ ->
	    not_interpreted
    end.

get_function(Mod, Name, Args, local) ->
    Arity = length(Args),
    Key = {Mod,Name,Arity},
    case cached(Key) of
	false ->
	    DbRef = db_ref(Mod),
	    case erlide_dbg_idb:match_object(DbRef, {{Mod,Name,Arity,'_'},'_'}) of
		[{{Mod,Name,Arity,Exp},Clauses}] ->
		    cache(Key, {Exp,Clauses}),
		    Clauses;
		_ -> undef
	    end;
	{_Exp,Cs} -> Cs
    end;
get_function(Mod, Name, Args, extern) ->
    Arity = length(Args),
    Key = {Mod,Name,Arity},
    case cached(Key) of
	false ->
	    case db_ref(Mod) of
		not_found -> not_interpreted;
		DbRef ->
		    case erlide_dbg_idb:lookup(DbRef, {Mod,Name,Arity,true}) of
			{ok,Data} ->
			    cache(Key, {true,Data}),
			    Data;
			not_found ->
			    case erlide_dbg_idb:lookup(DbRef, module) of
				{ok,_} -> undef;
				not_found -> not_interpreted
			    end
		    end
	    end;
	{true,Cs} -> Cs;
	{false,_} -> undef
    end.

db_ref(Mod) ->
    case get([Mod|db]) of
	undefined ->
	    case erlide_dbg_iserver:call(get(int),
				  {get_module_db, Mod, get(self)}) of
		not_found ->
		    not_found;
		ModDb ->
		    Node = node(get(int)),
		    DbRef = if
				Node/=node() -> {Node,ModDb};
				true -> ModDb
			    end,
		    put([Mod|db], DbRef),
		    DbRef
	    end;
	DbRef ->
	    DbRef
    end.


cache(Key, Data) ->
    put(cache, lists:sublist([{Key,Data}|get(cache)], 5)).
	    
cached(Key) ->
    case lists:keysearch(Key, 1, get(cache))  of
	{value,{Key,Data}} -> Data;
	false -> false
    end.

%% Try to find a matching function clause
%% #ieval.level is set, the other fields must be set in this function
fnk_clauses([{clause,Line,Pars,Gs,Body}|Cs], M, F, As, Bs0, Ieval) ->
    case head_match(Pars, As, [], Bs0) of
	{match,Bs1} ->
	    Bs = add_bindings(Bs1, Bs0),
	    case guard(Gs, Bs) of
		true ->
		    seq(Body, Bs,
			Ieval#ieval{line=Line,
				    module=M,function=F,arguments=As});
		false ->
		    fnk_clauses(Cs, M, F, As, Bs0, Ieval)
	    end;
	nomatch ->
	    fnk_clauses(Cs, M, F, As, Bs0, Ieval)
    end;
fnk_clauses([], _M, _F, _As, Bs, Ieval) ->
    exception(error, function_clause, Bs, Ieval).

seq([E], Bs0, Ieval) ->
    case erlide_dbg_icmd:cmd(E, Bs0, Ieval) of
	{skip,Bs} ->
	    {value,skipped,Bs};
	{drop_to_frame, Le} ->
	    {dropped, Le, Bs0};
	Bs ->
	    expr(E, Bs, Ieval)
    end;
seq([E|Es], Bs0, Ieval) ->
    case erlide_dbg_icmd:cmd(E, Bs0, Ieval) of
	{skip,Bs} ->
	    seq(Es, Bs, Ieval);
	{drop_to_frame, Le} ->
	    {dropped, Le, Bs0};
	Bs1 ->
	    {value,_,Bs} = expr(E, Bs1, Ieval#ieval{last_call=false}),
	    seq(Es, Bs, Ieval)
    end;
seq([], Bs, _) ->
    {value,true,Bs}.

%% Variable
expr({var,Line,V}, Bs, Ieval) ->
    case binding(V, Bs) of
	{value,Val} ->
	    {value,Val,Bs};
	unbound ->
	    exception(error, {unbound,V}, Bs, Ieval#ieval{line=Line})
    end;

expr({value,_,Val}, Bs, _Ieval) ->
    {value,Val,Bs};
expr({value,Val}, Bs, _Ieval) -> % Special case straight values
    {value,Val,Bs};

%% List
expr({cons,Line,H0,T0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    Ieval1 = Ieval#ieval{last_call=false},
    {value,H,Bs1} = expr(H0,Bs0,Ieval1),
    {value,T,Bs2} = expr(T0,Bs0,Ieval1),
    {value,[H|T],merge_bindings(Bs2, Bs1, Ieval)};

%% Tuple
expr({tuple,Line,Es0}, Bs0, Ieval) ->
    {Vs,Bs} = eval_list(Es0, Bs0, Ieval#ieval{line=Line}),
    {value,list_to_tuple(Vs),Bs};

%% A block of statements
expr({block,Line,Es},Bs,Ieval) ->
    seq(Es, Bs, Ieval#ieval{line=Line});

%% Catch statement
expr({'catch',Line,Expr}, Bs0, Ieval) ->
    try expr(Expr, Bs0, Ieval#ieval{line=Line, last_call=false})
    catch
	Class:Reason ->
	    %% Exception caught, reset exit info
	    put(exit_info, undefined),
	    pop(Ieval#ieval.level),
	    Value = catch_value(Class, Reason),
	    trace(return, {Ieval, {Value, Bs0}}),
	    {value, Value, Bs0}
    end;

%% Try-catch statement
expr({'try',Line,Es,CaseCs,CatchCs,[]}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    try seq(Es, Bs0, Ieval#ieval{last_call=false}) of
	{value,Val,Bs} = Value ->
	    case CaseCs of
		[] -> Value;
		_ ->
		    case_clauses(Val, CaseCs, Bs, try_clause, Ieval)
	    end
    catch
	Class:Reason when CatchCs=/=[] ->
	    catch_clauses({Class,Reason,[]}, CatchCs, Bs0, Ieval)
    end;
expr({'try',Line,Es,CaseCs,CatchCs,As}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    try seq(Es, Bs0, Ieval#ieval{last_call=false}) of
	{value,Val,Bs} = Value ->
	    case CaseCs of
		[] -> Value;
		_ ->
		    case_clauses(Val, CaseCs, Bs, try_clause, Ieval)
	    end
    catch
	Class:Reason when CatchCs =/= [] ->
	    catch_clauses({Class,Reason,[]}, CatchCs, Bs0, Ieval)
    after
            seq(As, Bs0, Ieval#ieval{last_call=false})
    end;

%% Case statement
expr({'case',Line,E,Cs}, Bs0, Ieval) ->
    {value,Val,Bs} =
	expr(E, Bs0, Ieval#ieval{line=Line, last_call=false}),
    case_clauses(Val, Cs, Bs, case_clause, Ieval#ieval{line=Line});

%% If statement
expr({'if',Line,Cs}, Bs, Ieval) ->
    if_clauses(Cs, Bs, Ieval#ieval{line=Line});

%% Andalso/orelse
expr({'andalso',Line,E1,E2}, Bs, Ieval) ->
    case expr(E1, Bs, Ieval#ieval{line=Line, last_call=false}) of
	{value,false,_}=Res -> Res;
	{value,true,_} ->
	    case expr(E2, Bs, Ieval#ieval{line=Line, last_call=false}) of
		{value,Bool,_}=Res when is_boolean(Bool) -> Res;
		{value,Val,_} -> exception(error, {badarg,Val}, Bs, Ieval)
	    end;
	{value,Val,Bs} ->
	    exception(error, {badarg,Val}, Bs, Ieval)
    end;
expr({'orelse',Line,E1,E2}, Bs, Ieval) ->
    case expr(E1, Bs, Ieval#ieval{line=Line, last_call=false}) of
	{value,true,_}=Res -> Res;
	{value,false,_} ->
	    case expr(E2, Bs, Ieval#ieval{line=Line, last_call=false}) of
		{value,Bool,_}=Res when is_boolean(Bool) -> Res;
		{value,Val,_} -> exception(error, {badarg,Val}, Bs, Ieval)
	    end;
	{value,Val,_} ->
	    exception(error, {badarg,Val}, Bs, Ieval)
    end;

%% Matching expression
expr({match,Line,Lhs,Rhs0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,Rhs,Bs1} = expr(Rhs0, Bs0, Ieval#ieval{last_call=false}),
    case match(Lhs, Rhs, Bs1) of
	{match,Bs} ->
	    {value,Rhs,Bs};
	nomatch ->
	    exception(error, {badmatch,Rhs}, Bs1, Ieval)
    end;

%% Construct a fun
expr({make_fun,Line,Name,Cs}, Bs, #ieval{module=Module}=Ieval) ->
    Arity = length(element(3,hd(Cs))),
    Info = {Module,Name},
    Fun = 
	case Arity of
	    0 -> fun() -> eval_fun(Cs, [], Bs, Info) end;
	    1 -> fun(A) -> eval_fun(Cs, [A], Bs,Info) end;
	    2 -> fun(A,B) -> eval_fun(Cs, [A,B], Bs,Info) end;
	    3 -> fun(A,B,C) -> eval_fun(Cs, [A,B,C], Bs,Info) end;
	    4 -> fun(A,B,C,D) -> eval_fun(Cs, [A,B,C,D], Bs,Info) end;
	    5 -> fun(A,B,C,D,E) -> eval_fun(Cs, [A,B,C,D,E], Bs,Info) end;
	    6 -> fun(A,B,C,D,E,F) -> eval_fun(Cs, [A,B,C,D,E,F], Bs,Info) end;
	    7 -> fun(A,B,C,D,E,F,G) -> 
			 eval_fun(Cs, [A,B,C,D,E,F,G], Bs,Info) end;
	    8 -> fun(A,B,C,D,E,F,G,H) -> 
			 eval_fun(Cs, [A,B,C,D,E,F,G,H], Bs,Info) end;
	    9 -> fun(A,B,C,D,E,F,G,H,I) -> 
			 eval_fun(Cs, [A,B,C,D,E,F,G,H,I], Bs,Info) end;
	    10 -> fun(A,B,C,D,E,F,G,H,I,J) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J], Bs,Info) end;
	    11 -> fun(A,B,C,D,E,F,G,H,I,J,K) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K], Bs,Info) end;
	    12 -> fun(A,B,C,D,E,F,G,H,I,J,K,L) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L], Bs,Info) end;
	    13 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M], Bs,Info) end;
	    14 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N], Bs,Info) end;
	    15 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O], Bs,Info) end;
	    16 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P], Bs,Info) end;
	    17 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q], Bs,Info) end;
	    18 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R], Bs,Info) end;
	    19 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S) -> 
		     	  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S],Bs,Info) end;
	    20 -> fun(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T) -> 
			  eval_fun(Cs, [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T],Bs,Info) end;
	    _Other ->
		exception(error, {'argument_limit',{'fun',Cs}}, Bs,
			  Ieval#ieval{line=Line})
	end,
    {value,Fun,Bs};

%% Local function call
expr({local_call,Line,F,As0}, Bs0, #ieval{module=M} = Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    eval_function(M, F, As, Bs, local, Ieval);

%% Remote function call
expr({call_remote,Line,M,F,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    eval_function(M, F, As, Bs, extern, Ieval);

%% Emulated semantics of some BIFs
expr({dbg,Line,self,[]}, Bs, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    trace(bif, {Ieval, {erlang, self, [], Bs}}),
    Self = get(self),
    trace(return, {Ieval, Self}),
    {value,Self,Bs};
expr({dbg,Line,get_stacktrace,[]}, Bs, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    trace(bif, {Ieval, {erlang, get_stacktrace, [], Bs}}),
    Stacktrace = get(stacktrace),
    trace(return, {Ieval, Stacktrace}),
    {value,Stacktrace,Bs};
expr({dbg,Line,throw,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {[Term],Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {erlang, throw, [Term], Bs}}),
    exception(throw, Term, Bs, Ieval);
expr({dbg,Line,error,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {[Term],Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {erlang, error, [Term], Bs}}),
    exception(error, Term, Bs, Ieval);
expr({dbg,Line,fault,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {[Term],Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {erlang, fault, [Term], Bs}}),
    exception(fault, Term, Bs, Ieval);
expr({dbg,Line,exit,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {[Term],Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {erlang, exit, [Term], Bs}}),
    exception(exit, Term, Bs, Ieval);

%% Call to "safe" BIF, ie a BIF that can be executed in Meta process
expr({safe_bif,Line,M,F,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {M, F, As, Bs}}),
    push({M,F,As}, Bs0, Ieval),
    {_,Value, _Bs} = Res = safe_bif(M, F, As, Bs, Ieval),
    trace(return, {Ieval, Value}),
    pop(),
    Res;

%% Call to a BIF that must be evaluated in the correct process
expr({bif,Line,M,F,As0}, Bs0, #ieval{level=Le}=Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {M, F, As, Bs}}),
    push({M,F,As}, Bs0, Ieval),
    {_,Value, _Bs} =
	Res = debugged_cmd({apply,M,F,As}, Bs, Ieval#ieval{level=Le+1}),
    trace(return, {Ieval, {Value}}),
    pop(),
    Res;

%% Call to a BIF that spawns a new process
expr({spawn_bif,Line,M,F,As0}, Bs0, #ieval{level=Le}=Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    trace(bif, {Ieval, {M, F, As, Bs}}),
    push({M,F,As}, Bs0, Ieval),
    Res = debugged_cmd({apply,M,F,As}, Bs,Ieval#ieval{level=Le+1}),
    trace(return, {Ieval, {Res}}),
    pop(),
    Res;

%% Call to an operation
expr({op,Line,Op,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {As,Bs} = eval_list(As0, Bs0, Ieval),
    try apply(erlang,Op,As) of
	Value ->
	    {value,Value,Bs}
    catch
	Class:Reason ->
	    exception(Class, Reason, Bs, Ieval)
    end;

%% apply/2 (fun)
expr({apply_fun,Line,Fun0,As0}, Bs0, #ieval{level=Le}=Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    FunValue = case expr(Fun0, Bs0, Ieval) of
		   {value,{dbg_apply,Mx,Fx,Asx},Bsx} ->
		       debugged_cmd({apply,Mx,Fx,Asx},
				    Bsx, Ieval#ieval{level=Le+1});
		   OtherFunValue ->
		       OtherFunValue
	       end,
    case FunValue of
	{value,Fun,Bs1} when is_function(Fun) ->
	    {As,Bs} = eval_list(As0, Bs1, Ieval),
	    eval_function(undefined, Fun, As, Bs, extern, Ieval);
	{value,{M,F},Bs1} when is_atom(M), is_atom(F) ->
	    {As,Bs} = eval_list(As0, Bs1, Ieval),
	    eval_function(M, F, As, Bs, extern, Ieval);
	{value,BadFun,Bs1} ->
	    exception(error, {badfun,BadFun}, Bs1, Ieval)
    end;

%% apply/3
expr({apply,Line,As0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {[M,F,As],Bs} = eval_list(As0, Bs0, Ieval),
    eval_function(M, F, As, Bs, extern, Ieval);
    
%% Mod:module_info/0,1
expr({module_info_0,_,Mod}, Bs, _Ieval) ->
    {value,[{compile,module_info(Mod,compile)},
	    {attributes,module_info(Mod,attributes)},
	    {imports,module_info(Mod,imports)},
	    {exports,module_info(Mod,exports)}],Bs};
expr({module_info_1,Line,Mod,[As0]}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,What,Bs} = expr(As0, Bs0, Ieval),
    {value,module_info(Mod, What),Bs};

%% Receive statement
expr({'receive',Line,Cs}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    trace(receivex, {Ieval, {false, Bs0}}),
    eval_receive(get(self), Cs, Bs0, Ieval);

%% Receive..after statement
expr({'receive',Line,Cs,To,ToExprs}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,ToVal,ToBs} = expr(To, Bs0, Ieval#ieval{last_call=false}),
    trace(receivex, {Ieval, {true, Bs0}}),
    check_timeoutvalue(ToVal, ToBs, To, Ieval),
    {Stamp,_} = statistics(wall_clock),
    eval_receive(get(self), Cs, ToVal, ToExprs, ToBs, Bs0,
		 0, Stamp, Ieval);

%% Send (!)
expr({send,Line,To0,Msg0}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    Ieval1 = Ieval#ieval{last_call=false},
    {value,To,Bs1} = expr(To0, Bs0, Ieval1),
    {value,Msg,Bs2} = expr(Msg0, Bs0, Ieval1),
    Bs = merge_bindings(Bs2, Bs1, Ieval),
    eval_send(To, Msg, Bs, Ieval);

%% Binary
expr({bin,Line,Fs}, Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    eval_bits:expr_grp(Fs, Bs0,
		       fun (E, B) -> expr(E, B, Ieval) end,
		       [],
		       false);

%% List comprehension
expr({lc,_Line,E,Qs}, Bs, Ieval) ->
    eval_lc(E, Qs, Bs, Ieval);
expr({bc,_Line,E,Qs}, Bs, Ieval) ->
    eval_bc(E, Qs, Bs, Ieval);

%% Brutal exit on unknown expressions/clauses/values/etc.
expr(E, _Bs, _Ieval) ->
    erlang:error({'NYI',E}).

%% Interpreted fun() called from uninterpreted module, recurse
eval_fun(Cs, As, Bs, Info) ->
    erlide_dbg_debugged:eval(?MODULE, eval_fun, [Cs,As,Bs,Info]).

%% eval_lc(Expr,[Qualifier],Bindings,IevalState) ->
%%	{value,Value,Bindings}.
%% This is evaluating list comprehensions "straight out of the book".
%% Copied from rv's implementation in erl_eval.
eval_lc(E, Qs, Bs, Ieval) ->
    {value,eval_lc1(E, Qs, Bs, Ieval),Bs}.

eval_lc1(E, [{generate,Line,P,L0}|Qs], Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,L1,Bs1} = expr(L0, Bs0, Ieval#ieval{last_call=false}),
    flatmap(fun (V) ->
		    case catch match1(P, V, [], Bs0) of
			{match,Bsn} ->
			    Bs2 = add_bindings(Bsn, Bs1),
			    eval_lc1(E, Qs, Bs2, Ieval);
			nomatch -> []
		    end end,L1);
eval_lc1(E, [{b_generate,Line,P,L0}|Qs], Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,Bin,_} = expr(L0, Bs0, Ieval#ieval{last_call=false}),
    CompFun = fun(NewBs) -> eval_lc1(E, Qs, NewBs, Ieval) end,
    eval_b_generate(Bin, P, Bs0, CompFun);
eval_lc1(E, [{guard,Q}|Qs], Bs0, Ieval) ->
    case guard(Q, Bs0) of
	true -> eval_lc1(E, Qs, Bs0, Ieval);
	false -> []
    end;
eval_lc1(E, [Q|Qs], Bs0, Ieval) ->
    case expr(Q, Bs0, Ieval#ieval{last_call=false}) of
	{value,true,Bs} -> eval_lc1(E, Qs, Bs, Ieval);
	_ -> []
    end;
eval_lc1(E, [], Bs, Ieval) ->
    {value,V,_} = expr(E, Bs, Ieval#ieval{last_call=false}),
    [V].

%% eval_bc(Expr,[Qualifier],Bindings,IevalState) ->
%%	{value,Value,Bindings}.
%% This is evaluating list comprehensions "straight out of the book".
%% Copied from rv's implementation in erl_eval.
eval_bc(E, Qs, Bs, Ieval) ->
    Val = erlang:list_to_bitstring(eval_bc1(E, Qs, Bs, Ieval)),
    {value,Val,Bs}.

eval_bc1(E, [{generate,Line,P,L0}|Qs], Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,L1,Bs1} = expr(L0, Bs0, Ieval#ieval{last_call=false}),
    flatmap(fun (V) ->
		    case catch match1(P, V, [], Bs0) of
			{match,Bsn} ->
			    Bs2 = add_bindings(Bsn, Bs1),
			    eval_bc1(E, Qs, Bs2, Ieval);
			nomatch -> []
		    end end, L1);
eval_bc1(E, [{b_generate,Line,P,L0}|Qs], Bs0, Ieval0) ->
    Ieval = Ieval0#ieval{line=Line},
    {value,Bin,_} = expr(L0, Bs0, Ieval#ieval{last_call=false}),
    CompFun = fun(NewBs) -> eval_bc1(E, Qs, NewBs, Ieval) end,
    eval_b_generate(Bin, P, Bs0, CompFun);
eval_bc1(E, [{guard,Q}|Qs], Bs0, Ieval) ->
    case guard(Q, Bs0) of
	true -> eval_bc1(E, Qs, Bs0, Ieval);
	false -> []
    end;
eval_bc1(E, [Q|Qs], Bs0, Ieval) ->
    case expr(Q, Bs0, Ieval#ieval{last_call=false}) of
	{value,true,Bs} -> eval_bc1(E, Qs, Bs, Ieval);
	_ -> []
    end;
eval_bc1(E, [], Bs, Ieval) ->
    {value,V,_} = expr(E, Bs, Ieval#ieval{last_call=false}),
    [V].

eval_b_generate(<<_/binary>>=Bin, P, Bs0, CompFun) ->
    Mfun = fun(L, R, Bs) -> match1(L, R, Bs, Bs0) end,
    Efun = fun(Exp, Bs) -> expr(Exp, Bs, #ieval{}) end,
    case eval_bits:bin_gen(P, Bin, erl_eval:new_bindings(), Bs0, Mfun, Efun) of
	{match,Rest,Bs1} ->
	    Bs2 = add_bindings(Bs1, Bs0),
	    CompFun(Bs2) ++ eval_b_generate(Rest, P, Bs0, CompFun);
	{nomatch,Rest} ->
	    eval_b_generate(Rest, P, Bs0, CompFun);
	done ->
	    []
    end.

%% eval_b_generate(<<_/bitstring>>=Bin, P, Bs0, CompFun) ->
%%     Mfun = fun(L, R, Bs) -> match1(L, R, Bs, Bs0) end,
%%     Efun = fun(Exp, Bs) -> expr(Exp, Bs, #ieval{}) end,
%%     case eval_bits:bin_gen(P, Bin, erl_eval:new_bindings(), Bs0, Mfun, Efun) of
%% 	{match,Rest,Bs1} ->
%% 	    Bs2 = add_bindings(Bs1, Bs0),
%% 	    CompFun(Bs2) ++ eval_b_generate(Rest, P, Bs0, CompFun);
%% 	{nomatch,Rest} ->
%% 	    eval_b_generate(Rest, P, Bs0, CompFun);
%% 	done ->
%% 	    []
%%     end.

module_info(Mod, module) -> Mod;
module_info(_Mod, compile) -> [];
module_info(Mod, attributes) ->
    {ok, Attr} = erlide_dbg_iserver:call(get(int), {lookup, Mod, attributes}),
    Attr;
module_info(_Mod, imports) -> [];
module_info(Mod, exports) ->
    {ok, Exp} = erlide_dbg_iserver:call(get(int), {lookup, Mod, exports}),
    Exp;
module_info(_Mod, functions) -> [].

safe_bif(M, F, As, Bs, Ieval) ->
    try apply(M, F, As) of
       	Value ->
	    {value,Value,Bs}
    catch
	Class:Reason ->
	    exception(Class, Reason, Bs, Ieval)
    end.

eval_send(To, Msg, Bs, Ieval) ->
    try To ! Msg of
	Msg -> 
	    trace(send, {Ieval, {To, Msg, Bs}}),
	    {value,Msg,Bs}
    catch
	Class:Reason ->
	    exception(Class, Reason, Bs, Ieval)
    end.

%% Start tracing of messages before fetching current messages in
%% the queue to make sure that no messages are lost. 
eval_receive(Debugged, Cs, Bs0,
	     #ieval{module=M,line=Line,level=Le}=Ieval) ->
    %% To avoid private message passing protocol between META
    %% and interpreted process.
    erlang:trace(Debugged,true,['receive']),
    {_,Msgs} = erlang:process_info(Debugged,messages),
    case receive_clauses(Cs, Bs0, Msgs, Ieval) of
	nomatch ->
	    erlide_dbg_iserver:cast(get(int), {set_status, self(),waiting,{}}),
	    erlide_dbg_icmd:tell_attached({wait_at,M,Line,Le}),
	    eval_receive1(Debugged, Cs, Bs0, Ieval);
	{eval,B,Bs,Msg} ->
	    rec_mess(Debugged, Msg, Bs, Ieval),
	    seq(B, Bs, Ieval)
    end.

eval_receive1(Debugged, Cs, Bs0, Ieval) ->
    Msgs = do_receive(Debugged, Bs0, Ieval),
    case receive_clauses(Cs, Bs0, Msgs, Ieval) of
	nomatch ->
	    eval_receive1(Debugged, Cs, Bs0, Ieval);
	{eval,B,Bs,Msg} ->
	    rec_mess(Debugged, Msg, Bs0, Ieval),
	    erlide_dbg_iserver:cast(get(int), {set_status, self(),running,{}}),
	    erlide_dbg_icmd:tell_attached(running),
	    seq(B, Bs, Ieval)
    end.

check_timeoutvalue(ToVal,_,_,_) when is_integer(ToVal), ToVal>=0 -> true;
check_timeoutvalue(infinity,_,_,_) -> true;
check_timeoutvalue(_ToVal, ToBs, To, Ieval) ->
    Line = element(2, To),
    exception(error, timeout_value, ToBs, Ieval#ieval{line=Line}).

eval_receive(Debugged, Cs, 0, ToExprs, ToBs, Bs0, 0, _Stamp, Ieval) ->
    {_,Msgs} = erlang:process_info(Debugged,messages),
    case receive_clauses(Cs, Bs0, Msgs, Ieval) of
	nomatch ->
	    trace(received, {Ieval, {null}}),
	    seq(ToExprs, ToBs, Ieval);
	{eval,B,Bs,Msg} ->
	    rec_mess_no_trace(Debugged, Msg, Bs0, Ieval),
	    seq(B, Bs, Ieval)
    end;
eval_receive(Debugged, Cs, ToVal, ToExprs, ToBs, Bs0,
	     0, Stamp, #ieval{module=M,line=Line,level=Le}=Ieval)->
    erlang:trace(Debugged,true,['receive']),
    {_,Msgs} = erlang:process_info(Debugged,messages),
    case receive_clauses(Cs, Bs0, Msgs, Ieval) of
	nomatch ->
	    {Stamp1,Time1} = newtime(Stamp,ToVal),
	    erlide_dbg_iserver:cast(get(int), {set_status, self(),waiting,{}}),
	    erlide_dbg_icmd:tell_attached({wait_after_at,M,Line,Le}),
	    eval_receive(Debugged, Cs, Time1, ToExprs, ToBs, Bs0,
			 infinity,Stamp1, Ieval);
	{eval,B,Bs,Msg} ->
	    rec_mess(Debugged, Msg, Bs0, Ieval),
	    seq(B, Bs, Ieval)
    end;
eval_receive(Debugged, Cs, ToVal, ToExprs, ToBs, Bs0,
	     _, Stamp, Ieval) ->
    case do_receive(Debugged, ToVal, Stamp, Bs0, Ieval) of
	timeout ->
	    trace(received, {Ieval, {null}}),
	    rec_mess(Debugged),
	    erlide_dbg_iserver:cast(get(int), {set_status, self(),running,{}}),
	    erlide_dbg_icmd:tell_attached(running),
	    seq(ToExprs, ToBs, Ieval);
	Msgs ->
	    case receive_clauses(Cs, Bs0, Msgs, Ieval) of
		nomatch ->
		    {Stamp1,Time1} = newtime(Stamp,ToVal),
		    eval_receive(Debugged, Cs, Time1, ToExprs, ToBs,
				 Bs0, infinity,Stamp1, Ieval);
		{eval,B,Bs,Msg} ->
		    rec_mess(Debugged, Msg, Bs0, Ieval),
		    erlide_dbg_iserver:cast(get(int),
				     {set_status, self(), running, {}}),
		    erlide_dbg_icmd:tell_attached(running),
		    seq(B, Bs, Ieval)
	    end
    end.

do_receive(Debugged, Bs, Ieval) ->
    receive
	{trace,Debugged,'receive',Msg} ->
	    [Msg];
	Msg ->
	    check_exit_msg(Msg, Bs, Ieval),
	    erlide_dbg_icmd:handle_msg(Msg, wait_at, Bs, Ieval),
	    do_receive(Debugged, Bs, Ieval)
    end.

do_receive(Debugged, Time, Stamp, Bs, Ieval) ->
    receive
	{trace,Debugged,'receive',Msg} ->
	    [Msg];
	{user, timeout} ->
	    timeout;
	Msg ->
	    check_exit_msg(Msg, Bs, Ieval),
	    erlide_dbg_icmd:handle_msg(Msg, wait_after_at, Bs, Ieval),
	    {Stamp1,Time1} = newtime(Stamp,Time),
	    do_receive(Debugged, Time1, Stamp1, Bs, Ieval)
    after Time ->
	    timeout
    end.

newtime(Stamp,infinity) ->
    {Stamp,infinity};
newtime(Stamp,Time) ->
    {Stamp1,_} = statistics(wall_clock),
    case Time - (Stamp1 - Stamp) of
	NewTime when NewTime > 0 ->
	    {Stamp1,NewTime};
	_ ->
	    {Stamp1,0}
    end.

rec_mess(Debugged, Msg, Bs, Ieval) ->
    erlang:trace(Debugged, false, ['receive']),
    flush_traces(Debugged),
    Debugged ! {sys,self(),{'receive',Msg}},
    rec_ack(Debugged, Bs, Ieval).

rec_mess(Debugged) ->
    erlang:trace(Debugged, false, ['receive']),
    flush_traces(Debugged).

rec_mess_no_trace(Debugged, Msg, Bs, Ieval) ->
    Debugged ! {sys,self(),{'receive',Msg}},
    rec_ack(Debugged, Bs, Ieval).

rec_ack(Debugged, Bs, Ieval) ->
    receive
	{Debugged,rec_acked} ->
	    true;
	Msg ->
	    check_exit_msg(Msg, Bs, Ieval),
	    io:format("***WARNING*** Unexp msg ~p, ieval ~p~n",
		      [Msg, Ieval])
    end.

flush_traces(Debugged) ->
    receive
	{trace,Debugged,'receive',_} ->
	    flush_traces(Debugged)
    after 0 ->
	    true
    end.

%% eval_list(ExpressionList, Bindings, Ieval)
%%  Evaluate a list of expressions "in parallel" at the same level.
eval_list(Es, Bs, Ieval) ->
    eval_list(Es, [], Bs, Bs, Ieval).

eval_list([E|Es], Vs, BsOrig, Bs0, Ieval) ->
    {value,V,Bs1} = expr(E, BsOrig, Ieval#ieval{last_call=false}),
    eval_list(Es, [V|Vs], BsOrig, merge_bindings(Bs1,Bs0,Ieval), Ieval);
eval_list([], Vs, _, Bs, _Ieval) ->
    {lists:reverse(Vs,[]),Bs}.

%% if_clauses(Clauses, Bindings, Ieval)
if_clauses([{clause,_,[],G,B}|Cs], Bs, Ieval) ->
    case guard(G, Bs) of
	true ->
	    seq(B, Bs, Ieval);
	false ->
	    if_clauses(Cs, Bs, Ieval)
    end;
if_clauses([], Bs, Ieval) ->
    exception(error, if_clause, Bs, Ieval).

%% case_clauses(Value, Clauses, Bindings, Error, Ieval)
%%   Error = try_clause � case_clause
case_clauses(Val, [{clause,_,[P],G,B}|Cs], Bs0, Error, Ieval) ->
    case match(P, Val, Bs0) of
	{match,Bs} ->
	    case guard(G, Bs) of
		true ->
		    seq(B, Bs, Ieval);
		false ->
		    case_clauses(Val, Cs, Bs0, Error, Ieval)
	    end;
	nomatch ->
	    case_clauses(Val, Cs, Bs0, Error, Ieval)
    end;
case_clauses(Val,[], Bs, Error, Ieval) ->
    exception(error, {Error,Val}, Bs, Ieval).

%% catch_clauses(Exception, Clauses, Bindings, Ieval)
%%   Exception = {Class,Reason,[]}
catch_clauses(Exception, [{clause,_,[P],G,B}|CatchCs], Bs0, Ieval) ->
    case match(P, Exception, Bs0) of
	{match,Bs} ->
	    case guard(G, Bs) of
		true ->
		    %% Exception caught, reset exit info
		    put(exit_info, undefined),
		    pop(Ieval#ieval.level),
		    seq(B, Bs, Ieval);
		false ->
		    catch_clauses(Exception, CatchCs, Bs0, Ieval)
	    end;
	nomatch ->
	    catch_clauses(Exception, CatchCs, Bs0, Ieval)
    end;
catch_clauses({Class,Reason,[]}, [], _Bs, _Ieval) ->
    erlang:Class(Reason).

receive_clauses(Cs, Bs0, [Msg|Msgs], Ieval) ->
    case rec_clauses(Cs, Bs0, Msg, Ieval) of
	nomatch ->
	    receive_clauses(Cs, Bs0, Msgs, Ieval);
	{eval,B,Bs} ->
	    {eval,B,Bs,Msg}
    end;
receive_clauses(_, _, [], _Ieval) ->
    nomatch.

rec_clauses([{clause,_,Pars,G,B}|Cs], Bs0, Msg, Ieval) ->
    case rec_match(Pars, Msg, Bs0) of
	{match,Bs} ->
	    case guard(G, Bs) of
		true ->
		    trace(received, {Ieval, {Msg, Bs}}),
		    {eval,B,Bs};
		false ->
		    rec_clauses(Cs, Bs0, Msg, Ieval)
	    end;
	nomatch ->
	    rec_clauses(Cs, Bs0, Msg, Ieval)
    end;
rec_clauses([], _, _, _Ieval) ->
    nomatch.

%% guard(GuardTests,Bindings)
%%  Evaluate a list of guards.
guard([], _) -> true;
guard(Gs, Bs) -> or_guard(Gs, Bs).
    
or_guard([G|Gs], Bs) ->
    %% Short-circuit OR.
    case and_guard(G, Bs) of
	true -> true;
	false -> or_guard(Gs, Bs)
    end;
or_guard([], _) -> false.

and_guard([G|Gs], Bs) ->
    %% Short-circuit AND.
    case catch guard_expr(G, Bs) of
	{value,true} -> and_guard(Gs, Bs);
	_ -> false
    end;
and_guard([],_) -> true.

guard_exprs([A0|As0], Bs) ->
    {value,A} = guard_expr(A0, Bs),
    {values,As} = guard_exprs(As0, Bs),
    {values,[A|As]};
guard_exprs([], _) ->
    {values,[]}.

guard_expr({'andalso',_,E1,E2}, Bs) ->
    case guard_expr(E1, Bs) of
	{value,false}=Res -> Res;
	{value,true} ->
	    case guard_expr(E2, Bs) of
		{value,Bool}=Res when is_boolean(Bool) -> Res
	    end
    end;
guard_expr({'orelse',_,E1,E2}, Bs) ->
    case guard_expr(E1, Bs) of
	{value,true}=Res -> Res;
	{value,false} ->
	    case guard_expr(E2, Bs) of
		{value,Bool}=Res when is_boolean(Bool) -> Res
	    end
    end;
guard_expr({dbg,_,self,[]}, _) ->
    {value,get(self)};
guard_expr({safe_bif,_,erlang,'not',As0}, Bs) ->
    {values,As} = guard_exprs(As0, Bs),
    {value,apply(erlang, 'not', As)};
guard_expr({safe_bif,_,Mod,Func,As0}, Bs) ->
    {values,As} = guard_exprs(As0, Bs),
    {value,apply(Mod, Func, As)};
guard_expr({var,_,V}, Bs) ->
    {value,_} = binding(V, Bs);
guard_expr({value,_,Val}, _Bs) ->
    {value,Val};
guard_expr({cons,_,H0,T0}, Bs) ->
    {value,H} = guard_expr(H0, Bs),
    {value,T} = guard_expr(T0, Bs),
    {value,[H|T]};
guard_expr({tuple,_,Es0}, Bs) ->
    {values,Es} = guard_exprs(Es0, Bs),
    {value,list_to_tuple(Es)};
guard_expr({bin,_,Flds}, Bs) ->
    {value,V,_Bs} = 
	eval_bits:expr_grp(Flds, Bs,
			   fun(E,B) ->
				   {value,V} = guard_expr(E,B),
				   {value,V,B}
			   end, [], false),
    {value,V}.

%% match(Pattern,Term,Bs) -> {match,Bs} | nomatch
match(Pat, Term, Bs) ->
    try match1(Pat, Term, Bs, Bs)
    catch
	Result -> Result
    end.

match1({value,_,V}, V, Bs,_BBs) ->
    {match,Bs};
match1({var,_,'_'}, Term, Bs,_BBs) -> % Anonymous variable matches
    {match,add_anon(Term, Bs)};   % everything,save it anyway
match1({var,_,Name}, Term, Bs, _BBs) ->
    case binding(Name, Bs) of
	{value,Term} ->
	    {match,Bs};
	{value,_} ->
	    throw(nomatch);
	unbound ->
	    {match,[{Name,Term}|Bs]} % Add the new binding
    end;
match1({match,_,Pat1,Pat2}, Term, Bs0, BBs) ->
    {match,Bs1} = match1(Pat1, Term, Bs0, BBs),
    match1(Pat2, Term, Bs1, BBs);
match1({cons,_,H,T}, [H1|T1], Bs0, BBs) ->
    {match,Bs} = match1(H, H1, Bs0, BBs),
    match1(T, T1, Bs, BBs);
match1({tuple,_,Elts}, Tuple, Bs, BBs) 
  when length(Elts) =:= size(Tuple) ->
%%   when length(Elts) =:= tuple_size(Tuple) ->
    match_tuple(Elts, Tuple, 1, Bs, BBs);
match1({bin,_,Fs}, B, Bs0, BBs0) when is_binary(B) ->
%% match1({bin,_,Fs}, B, Bs0, BBs0) when is_bitstring(B) ->
    Bs1 = lists:sort(Bs0),  %Kludge.
    BBs = lists:sort(BBs0),
    try eval_bits:match_bits(Fs, B, Bs1, BBs,
			     fun(L, R, Bs) -> match1(L, R, Bs, BBs) end,
			     fun(E, Bs) -> expr(E, Bs, #ieval{}) end,
			     false) of
	Match -> Match
    catch
	_:_ -> throw(nomatch)
    end;
match1(_,_,_,_) ->
    throw(nomatch).

match_tuple([E|Es], Tuple, I, Bs0, BBs) ->
    {match,Bs} = match1(E, element(I, Tuple), Bs0, BBs),
    match_tuple(Es, Tuple, I+1, Bs, BBs);
match_tuple([], _, _, Bs, _BBs) ->
    {match,Bs}.

head_match([Par|Pars], [Arg|Args], Bs0, BBs) ->
    try match1(Par, Arg, Bs0, BBs) of
	{match,Bs} -> head_match(Pars, Args, Bs, BBs)
    catch 
	Result -> Result
    end;
head_match([],[],Bs,_) -> {match,Bs}.

rec_match([Par],Msg,Bs0) ->
    match(Par,Msg,Bs0).

binding(Name,[{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,_,{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,_,_,{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,_,_,_,{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,_,_,_,_,{Name,Val}|_]) ->
    {value,Val};
binding(Name,[_,_,_,_,_,_|Bs]) ->
    binding(Name,Bs);
binding(Name,[_,_,_,_,_|Bs]) ->
    binding(Name,Bs);
binding(Name,[_,_,_,_|Bs]) ->
    binding(Name,Bs);
binding(Name,[_,_,_|Bs]) ->
    binding(Name,Bs);
binding(Name,[_,_|Bs]) ->
    binding(Name,Bs);
binding(Name,[_|Bs]) ->
    binding(Name,Bs);
binding(_,[]) ->
    unbound.

add_anon(Val,[{'_',_}|Bs]) ->
    [{'_',Val}|Bs];
add_anon(Val,[B1,{'_',_}|Bs]) ->
    [B1,{'_',Val}|Bs];
add_anon(Val,[B1,B2,{'_',_}|Bs]) ->
    [B1,B2,{'_',Val}|Bs];
add_anon(Val,[B1,B2,B3,{'_',_}|Bs]) ->
    [B1,B2,B3,{'_',Val}|Bs];
add_anon(Val,[B1,B2,B3,B4,{'_',_}|Bs]) ->
    [B1,B2,B3,B4,{'_',Val}|Bs];
add_anon(Val,[B1,B2,B3,B4,B5,{'_',_}|Bs]) ->
    [B1,B2,B3,B4,B5,{'_',Val}|Bs];
add_anon(Val,[B1,B2,B3,B4,B5,B6|Bs]) ->
    [B1,B2,B3,B4,B5,B6|add_anon(Val,Bs)];
add_anon(Val,[B1,B2,B3,B4,B5|Bs]) ->
    [B1,B2,B3,B4,B5|add_anon(Val,Bs)];
add_anon(Val,[B1,B2,B3,B4|Bs]) ->
    [B1,B2,B3,B4|add_anon(Val,Bs)];
add_anon(Val,[B1,B2,B3|Bs]) ->
    [B1,B2,B3|add_anon(Val,Bs)];
add_anon(Val,[B1,B2|Bs]) ->
    [B1,B2|add_anon(Val,Bs)];
add_anon(Val,[B1|Bs]) ->
    [B1|add_anon(Val,Bs)];
add_anon(Val,[]) ->
    [{'_',Val}].

%% merge_bindings(Bindings1, Bindings2, Ieval)
%% Merge bindings detecting bad matches. 
%% Special case '_',save the new one !!!
%% Bindings1 is the newest bindings.
merge_bindings(Bs, Bs, _Ieval) ->
    Bs; % Identical bindings
merge_bindings([{Name,V}|B1s], B2s, Ieval) ->
    case binding(Name, B2s) of
	{value,V} -> % Already there, and the same
	    merge_bindings(B1s, B2s, Ieval);
	{value,_} when Name=='_' -> % Already there, but anonymous
	    B2s1 = lists:keydelete('_', 1, B2s),
	    [{Name,V}|merge_bindings(B1s, B2s1, Ieval)];
	{value,_} -> % Already there, but different => badmatch
	    exception(error, {badmatch,V}, B2s, Ieval);
	unbound -> % Not there,add it
	    [{Name,V}|merge_bindings(B1s, B2s, Ieval)]
    end;
merge_bindings([], B2s, _Ieval) ->
    B2s.

%% add_bindings(Bindings1,Bindings2)
%% Add Bindings1 to Bindings2. Bindings in
%% Bindings1 hides bindings in Bindings2.
%% Used in list comprehensions (and funs).
add_bindings(Bs1,[]) ->
    Bs1;
add_bindings([{Name,V}|Bs],ToBs0) ->
    ToBs = add_binding(Name,V,ToBs0),
    add_bindings(Bs,ToBs);
add_bindings([],ToBs) ->
    ToBs.

add_binding(N,Val,[{N,_}|Bs]) ->
    [{N,Val}|Bs];
add_binding(N,Val,[B1,{N,_}|Bs]) ->
    [B1,{N,Val}|Bs];
add_binding(N,Val,[B1,B2,{N,_}|Bs]) ->
    [B1,B2,{N,Val}|Bs];
add_binding(N,Val,[B1,B2,B3,{N,_}|Bs]) ->
    [B1,B2,B3,{N,Val}|Bs];
add_binding(N,Val,[B1,B2,B3,B4,{N,_}|Bs]) ->
    [B1,B2,B3,B4,{N,Val}|Bs];
add_binding(N,Val,[B1,B2,B3,B4,B5,{N,_}|Bs]) ->
    [B1,B2,B3,B4,B5,{N,Val}|Bs];
add_binding(N,Val,[B1,B2,B3,B4,B5,B6|Bs]) ->
    [B1,B2,B3,B4,B5,B6|add_binding(N,Val,Bs)];
add_binding(N,Val,[B1,B2,B3,B4,B5|Bs]) ->
    [B1,B2,B3,B4,B5|add_binding(N,Val,Bs)];
add_binding(N,Val,[B1,B2,B3,B4|Bs]) ->
    [B1,B2,B3,B4|add_binding(N,Val,Bs)];
add_binding(N,Val,[B1,B2,B3|Bs]) ->
    [B1,B2,B3|add_binding(N,Val,Bs)];
add_binding(N,Val,[B1,B2|Bs]) ->
    [B1,B2|add_binding(N,Val,Bs)];
add_binding(N,Val,[B1|Bs]) ->
    [B1|add_binding(N,Val,Bs)];
add_binding(N,Val,[]) ->
    [{N,Val}].

log(_) ->
    ok.
%% erlide_debug:log(E).
