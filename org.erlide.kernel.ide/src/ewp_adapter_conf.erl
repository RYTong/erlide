%% Author: jcrom
%% Created: Sep 10, 2013
%% Description: TODO: Add description to ewp_adapter_conf
-module(ewp_adapter_conf).

%%
%% Include files
%%
-define(EWP, 1).

-include("erlide.hrl").
%%
%% Exported Functions
%%
-compile([export_all]).
-export([]).

-define(ADP, adapter).
-define(PRO, procedure).
%%
%% API Functions
%%
add_adapter(Key, Params) ->
    ?ewp_log({params, Params}),
    CKey = binary_to_term((Key)),
    NewAdapter = new_adapter(Params),
    ?ewp_log({newAdapter, NewAdapter}),
    NewKey = CKey++NewAdapter,
    ouput_data(NewKey).

add_procedure(Key, Params) ->
    ?ewp_log({params, Params}),
    %%{ewp_adapter_conf,new_procedure,[{"test_p","test",xml,false,false,false,[],"test",[{"123","123"}]}
    CKey = binary_to_term((Key)),
    NewAdapter = new_procedure(Params),
    ?ewp_log({newProcedure, NewAdapter}),
    NewKey = CKey++NewAdapter,
    ouput_data(NewKey).

%% @doc when edit a adapter in eclipse ui, the java listener will call
%%      this function to change the erlang term content.
edit_adapter(Key, {Name, RId, RVal}) ->
    ?ewp_log({adapter, {Name, RId, RVal}}),
    %% {params,{"mb0104", name, "ttt"}}
    CKey = binary_to_term((Key)),

    TmpAdpList = proplists:get_all_values(?ADP, CKey),
    NewAdpList = check_adapter_element(TmpAdpList, Name, RId, RVal, []),
    NewKey =
        case RId of
            'name' ->
                ProList  = proplists:get_all_values(?PRO, CKey),
                NewPro = readpter(ProList, Name, RVal, []),
                NewPro ++ NewAdpList;
            _ ->
                proplists:delete(?ADP, CKey)++NewAdpList
        end,
    ouput_data(NewKey).

edit_procedure(Key, {Id, Adapter, RId, RVal}) ->
    ?ewp_log({params, {Id, Adapter, RId, RVal}}),
    %% {params,{"mb0104","simulator",sample_data,"public/test/mb01041.xml"}}
    CKey = binary_to_term((Key)),

    TmpAdpList = proplists:get_all_values(?PRO, CKey),
    NewAdpList = check_element(TmpAdpList, Id, Adapter, RId, RVal, []),
    NewKey = proplists:delete(?PRO, CKey)++NewAdpList,
    ouput_data(NewKey).


remove_adapter(Key, {Name, Children}) ->
    ?ewp_log({adapter, {Name, Children}}),
    %% {params,{"localhosr"}
    CKey = binary_to_term((Key)),

    TmpAdpList = proplists:get_all_values(?ADP, CKey),
    NewAdpList = [{?ADP, X}||X<-TmpAdpList, proplists:get_value(name, X) /= Name],
    TmpProList = proplists:get_all_values(?PRO, CKey),
    NewProList = lists:foldr(fun(Params, Acc) ->
                                     TmpId = proplists:get_value(id, Params),
                                     TmpAdapter = proplists:get_value(adapter, Params),
                                     case TmpAdapter of
                                         Name ->
                                             case lists:member(TmpId, Children) of
                                                 true ->
                                                     Acc;
                                                 _ ->
                                                     [{?PRO, Params}|Acc]
                                             end;
                                         _ ->
                                             [{?PRO, Params}|Acc]
                                     end
                             end, [], TmpProList),
    NewKey = NewAdpList++NewProList,
    ouput_data(NewKey).

remove_procedure(Key, {Id, Adapter}) ->
    ?ewp_log({params, {Id, Adapter}}),
    %% {params,{"mb0104","simulator"}}
    CKey = binary_to_term((Key)),

    TmpProList = proplists:get_all_values(?PRO, CKey),
    NewProList = lists:foldr(fun(Params, Acc) ->
                                     TmpId = proplists:get_value(id, Params),
                                     TmpAdapter = proplists:get_value(adapter, Params),
                                     if TmpId == Id andalso TmpAdapter == Adapter ->
                                            Acc;
                                        true ->
                                            [{?PRO, Params}|Acc]
                                     end
                             end, [], TmpProList),
    NewKey = proplists:delete(?PRO, CKey)++NewProList,
    ouput_data(NewKey).


new_procedure({Id, Adapter, Return_type, Log, Code, UseSample, SampleData, Path, Params}) ->
    %%{"test_p","test",xml,false,false,false,[],"test",[{"123","123"}]}
    [{procedure,[{id, Id},
                 {adapter, Adapter},
                 {path, Path},
                 {return_type, Return_type},
                 {gen_code, Code},
                 {gen_log, Log},
                 {use_sample_data, UseSample},
                 {data_sample, SampleData},
                 {parameters, Params}]}].

check_element([Element|Next], Id, Adapter, RId, RVal, Acc)->
    TId = proplists:get_value(id, Element),
    TAp = proplists:get_value(adapter, Element),

    if TId == Id andalso TAp == Adapter->
           ?ewp_log({target, RId, RVal}),
           RR = replace_element(Element, RId, RVal, []),
           ?ewp_log({target, RR}),
           check_element(Next, Id, Adapter, RId, RVal,
                         [{?PRO, RR}|Acc]);
       true ->
           check_element(Next, Id, Adapter, RId, RVal, [{?PRO, Element}|Acc])
    end;
check_element ([], _, _Adapter, _RId, _RVal, Acc) ->
    lists:reverse(Acc).

replace_element([{RId, _}|Next], RId, RVal, Acc) ->
    lists:reverse(Acc) ++[{RId, RVal}|Next];
replace_element([{Key, Value}|Next], RId, RVal, Acc) ->
    replace_element(Next, RId, RVal, [{Key, Value}|Acc]);
replace_element([], _, _, Acc) ->
    lists:reverse(Acc).

%%
%% Local Functions
%%
check_adapter_element([Element|Next], Id, RId, RVal, Acc)->
    TId = proplists:get_value(name, Element),
    if TId == Id ->
           ?ewp_log({target, RId, RVal}),
           RR = replace_element(Element, RId, RVal, []),
           ?ewp_log({target, RR}),
           check_adapter_element(Next, Id, RId, RVal,
                                 [{?ADP, RR}|Acc]);
       true ->
           check_adapter_element(Next, Id, RId, RVal, [{?ADP, Element}|Acc])
    end;
check_adapter_element ([], _, _RId, _RVal, Acc) ->
    lists:reverse(Acc).


ouput_data(NewKey) ->
    [lists:flatten(io_lib:format(form_output_format(length(NewKey)),NewKey)), term_to_binary(NewKey)].

form_output_format(Size) ->
    do_form_output_format(Size, "").
do_form_output_format(0, Acc) ->
    Acc;
do_form_output_format(Length, Acc) ->
    do_form_output_format(Length-1, "~p.~n"++Acc).

%% @doc when we edit a adapter name, we will change all the procedure under the new name adapter
%% @spec readpter(ProcedureList, AdapterName, NewAdapterNamae, Acc) -> NewProcedureList.
readpter([Element|Next], Name, RVal, Acc) ->
    TAp = proplists:get_value(?ADP, Element),
    case TAp of
        Name ->
           NewAdp = replace_element(Element, ?ADP, RVal, []),
           ?ewp_log({target, NewAdp}),
           readpter(Next, Name, RVal, [{?PRO, NewAdp}|Acc]);
        _ ->
            [Element|Acc]
    end;
readpter([], Name, RVal, Acc) ->
    lists:reverse(Acc).


%% create a new adapter struct
new_adapter({Name, Host, Port, Protocol, ReturnType}) ->
    [{adapter, [{name, Name},
                {host, Host},
                {protocol, Protocol}]++
          port_to_integer(Port)++
          [{return_type, ReturnType}]
     }].

port_to_integer([]) ->
    [];
port_to_integer(Port) when is_list(Port)->
    [{port,list_to_integer(Port)}].

