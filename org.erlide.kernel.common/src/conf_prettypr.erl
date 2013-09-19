-module(conf_prettypr).

%-export([format_from_file/1]).
-export([format/1, format/2]).

-define(FORMAT_ADVICE, "%% @remove. ").


%% read from file.
% format_from_file(F) ->
%     {ok, B} = file:read_file(F),
%     format(binary_to_list(B)).

%% pretty print the conf string.
format(Str) when is_list(Str)->
    format(Str, [{nl, false}]).
format(Str, Options) ->
    %S = p0(Str),
    %file:write_file(p0, S),
    format(Str, [], [], none, -1, Options).

format([], Acc, _State, _NL, _Indent, _Options) ->
    %file:write_file(rst, lists:flatten(lists:reverse(Acc))),
    Str = lists:flatten(lists:reverse(Acc)),
    {ok, Str};
format(Str = ?FORMAT_ADVICE++_, Acc, State, _NL, Indent, Options) ->
    case string:str(Str, "\n") of
	0 ->
	    format([], Acc, State, nl, Indent, Options);
	N ->
	    S = string:substr(Str, N+1),
	    format(S, Acc, State, nl, Indent, Options)
    end;
format(Str = [$%|_], Acc, State, NL, Indent, Options) ->
    IndStr = indent_string(Indent),
    NLStr = case NL of
             nl -> "\n"++IndStr;
             none -> ""
         end,
    case string:str(Str, "\n") of
    	0 ->
    	    format([], [NLStr++Str | Acc], State, none, Indent, Options);
    	N ->
    	    S0 = string:substr(Str, 1, N),
    	    S1 = string:substr(Str, N+1),
    	    format(S1, [NLStr++S0 | Acc], State, none, Indent, Options)
    end;
format([${|Rest], Acc, State, _NL, Indent, Options) ->
    IndStr = indent_string(Indent+1),
    format(Rest, [nl_left(Acc)++IndStr++"{" | Acc], [brace|State], none, Indent+1, Options);
format([$[|Rest], Acc, State, _NL, Indent, Options) ->
    IndStr = indent_string(Indent+1),
    format(Rest, [nl_left(Acc)++IndStr++"[" | Acc], [bracket|State], none, Indent+1, Options);
format(Str = [$"|_], Acc, State, _NL, Indent, Options) ->
    case re:run(Str, "\".*?\"", [dotall]) of
	{match, [{0, Len}]} ->
	    S0 = string:substr(Str, 1, Len),
	    S1 = string:substr(Str, Len+1),
	    format(S1, [S0 | Acc], State, none, Indent, Options);
	nomatch ->
	    format([], [Str | Acc], State, none, Indent, Options)
    end;
format([$}|Rest], Acc, [brace|Rest2], _NL, Indent, Options) ->
    NL = nl_right(Options, Indent),
    format(Rest, [NL++"}" | Acc], Rest2, none, Indent-1, Options);
format([$}|Rest], Acc, State, _NL, Indent, Options) ->
    Advice = create_advice(Indent, "}"),
    format([], [Rest, Advice | Acc], State, none, Indent, Options);
format([$]|Rest], Acc, [bracket|Rest2], _NL, Indent, Options) ->
    NL = nl_right(Options, Indent),
    format(Rest, [NL++"]" | Acc], Rest2, none, Indent-1, Options);
format([$]|Rest], Acc, State, _NL, Indent, Options) ->
    Advice = create_advice(Indent, "]"),
    format([], [Rest, Advice | Acc], State, none, Indent, Options);
format([$,|Rest], Acc, State, _NL, Indent, Options) ->
    format(Rest, [", " | Acc], State, none, Indent, Options);
format([$\t|Rest], Acc, State, NL, Indent, Options) ->
    format(Rest, Acc, State, NL, Indent, Options);
format([$\r, $\n|Rest], Acc, State, _NL, Indent, Options) ->
    format(Rest, Acc, State, nl, Indent, Options);
format([$\n|Rest], Acc, State, _NL, Indent, Options) ->
    format(Rest, Acc, State, nl, Indent, Options);
format([32|Rest], Acc, State, NL, Indent, Options) ->
    format(Rest, Acc, State, NL, Indent, Options);
format(Str, Acc, State, NL, Indent, Options) ->
    {match, [{0, Len}]} = re:run(Str, "[^\\[\\]\\{\\}\\s,]*"),
    %io:format("str:~p~n", [Str]),
    S0 = string:substr(Str, 1, Len),
    S1 = string:substr(Str, Len+1),
    format(S1, [S0 | Acc], State, NL, Indent, Options).
    
 

%% Internal Functions.
nl_left([]) -> "";
nl_left([H|_]) ->
    case hd(lists:reverse(H)) of
	$\n -> "";
	_ -> "\n"
    end.

nl_right(Options, Indent) ->
    case proplists:get_value(nl, Options) of
	true -> "\n"++indent_string(Indent);
	false -> ""
    end.

create_advice(Indent, Tag) when is_integer(Indent), is_list(Tag) ->
    Pair = case Tag of
        "]" -> "[";
        "}" -> "{"
        end,
    IndStr = indent_string(Indent),
    "\n"++?FORMAT_ADVICE++">>>>>>>>>>>>>I am lonely lonely lonely..."++
    "\n"++IndStr++Tag ++
    "\n"++?FORMAT_ADVICE++"<<<<<<<<<<<<<Where is my '"++Pair++"' !"++
    "\n\n\n".

indent_string(N) when is_integer(N), N > 0 ->
    lists:duplicate(N, $\t);
indent_string(_N) ->
    "".


% p0(Str) ->
%     p0(Str, [], text).   
% p0([], Acc, _) ->
%     lists:reverse(Acc);
% p0([32|Rest], Acc, text) ->
%     p0(Rest, Acc, text);
% p0([$\n|Rest], Acc, text) ->
%     p0(Rest, Acc, text);
% p0([$\t|Rest], Acc, text) ->
%     p0(Rest, Acc, text);
% p0([$"|Rest], Acc, text) ->
%     p0(Rest, [$"|Acc], quote);
% p0([$"|Rest], Acc, quote) ->
%     p0(Rest, [$"|Acc], text);
% p0([C|Rest], Acc, S) ->
%     p0(Rest, [C|Acc], S).

