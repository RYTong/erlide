%% Author: jcrom
%% Created: Aug 7, 2013
%% Description: TODO: Add description to ewp_check_conf
-module(ewp_check_conf).

%%
%% Include files
%%

-define(EWP, 1).

-include("erlide.hrl").

%%
%% Exported Functions
%%
-export([check_conf/1,
         check_item/1,
         string_to_term/1,
         utf8_to_ucs/1,
         safe_u28/1,
         ucs_to_utf8/1
         ]).

%%-define(jl(F, D), error_logger:info_msg(F, D)).
%%-define(jl(F), error_logger:info_msg(F)).
-define(ORDER_START, 1).



%%%===================================================================
%%% API
%%%===================================================================
check_conf(C) when is_list(C)->
    check_items(C);
check_conf(C) ->
    check_items([C]).

%%--------------------------------------------------------------------
%% @doc
%% @spec check_content(Content::String())
%% @end
%%--------------------------------------------------------------------
string_to_term(Content) ->
    Tokens = check_content(Content),
    parse_term(Tokens).

parse_term(error)->
    error;
parse_term([])->
    [];
parse_term(Tokens)->
    case erl_parse:parse_term(Tokens) of
        {ok, Result} ->
            Result;
        {error ,ErrorInfo} ->
            ?ewp_err({error_string_to_term, ErrorInfo}),
            error
    end.
check_content(Content)->
    {ok,Tokens,_} = erl_scan:string(Content),
    case Tokens of
        [] -> [];
        _ ->
            {ReTokens, DotList} = get_dot(Tokens),
            case hd(ReTokens) of
                {dot, _} ->
                    case length(DotList) of
                        1 -> Tokens;
                        _ -> reset_dot(Tokens)
                    end;
                _ ->
                    error
            end
    end.

get_dot(Tokens) ->
    loop_dot(Tokens, [], []).
loop_dot([{dot, L}=Dot|Next], Acc, DotAcc) ->
    loop_dot(Next, [Dot|Acc], [Dot|DotAcc]);
loop_dot([E|Next], Acc, DotAcc) ->
    loop_dot(Next, [E|Acc], DotAcc);
loop_dot([], Acc, DotAcc) ->
    {Acc, DotAcc}.

reset_dot(Tokens) ->
    loop_reset(Tokens, 1, length(Tokens), []).

loop_reset([FToken|Next], 1=Index, Len, Acc) ->
    Line = case FToken of
               {_, L} -> L;
               {_, L, _} -> L
           end,
    loop_reset(Next, Index+1, Len, [FToken, {'[', Line}|Acc]);
loop_reset([{dot, L}=Dot|Next], Len, Len, Acc) ->
    lists:reverse([Dot, {']', L}|Acc]);
loop_reset([{dot, L}= Dot|Next], Index, Len, Acc) ->
    loop_reset(Next, Index+1, Len, [{',', L}|Acc]) ;
loop_reset([E|Next], Index, Len, Acc) ->
    loop_reset(Next, Index+1, Len, [E|Acc]).


%%--------------------------------------------------------------------
%% @doc
%% @spec
%% @end
%%--------------------------------------------------------------------
check_item(CollList) ->
    lists:foldr(fun(Coll, Acc) ->
                        ItemList = proplists:get_value('items', Coll),
                        case ItemList of
                            undefined -> [Coll|Acc];
                            _ ->
                                NewItem = check_order(ItemList),
                                NewColl = proplists:delete('items', Coll),
                                [NewColl++[{'items', NewItem}]|Acc]
                        end
                end, [], CollList).

check_items(Conf) ->
    case proplists:get_value('collections', Conf) of
        undefined -> Conf;
        CollList ->
            NewCollList= lists:foldr(fun(Coll, Acc) ->
                                             ItemList = proplists:get_value('items', Coll),
                                             case ItemList of
                                                 undefined -> [Coll|Acc];
                                                 _ ->
                                                     NewItem = check_order(ItemList),
                                                     NewColl = proplists:delete('items', Coll),
                                                     [NewColl++[{'items', NewItem}]|Acc]
                                             end
                                     end, [], CollList),
            DelConf = proplists:delete('collections', Conf),
            DelConf++[{collections, NewCollList}]
    end.




check_order(ItemList) ->
    Re =  lists:foldr(fun(Item, Acc) ->
                              case proplists:get_value(menu_order, Item) of
                                  I when is_integer(I) -> [I|Acc];
                                  _ -> Acc
                              end
                      end, [], ItemList),
    case Re of
        [] -> ItemList;
        _ ->
            SetItem = lists:sort(fun(A, B) ->
                                         AO = proplists:get_value(menu_order, A),
                                         BO = proplists:get_value(menu_order, B),
                                         if AO < BO -> true;
                                            true -> false
                                         end
                                 end, ItemList),
            reset_order(SetItem, ?ORDER_START, [])

    end.

reset_order([Item|Next], Index, Acc) ->
    DelItem = proplists:delete(menu_order, Item),
    reset_order(Next, Index+1, [DelItem++[{menu_order, Index}]|Acc]);
reset_order([], _Index, Acc) ->
    lists:reverse(Acc).

%%-----------------------------------------------------------------
%% Function: utf8_2_unicode/1
%% Purpose: transform character set from utf-8 to unicode
%% Args: List::list() the charset of the list should be utf8 or unicode
%% Returns: a unicode list
%% NOTE:
%%       UTF-8  <----> Unicode
%%       11110vvv 10vvvvvv 10vvvvvv 10vvvvvv
%%-----------------------------------------------------------------
utf8_to_ucs(List) ->
    do_utf8_2_ucs(List, []).
do_utf8_2_ucs([A,B,C,D|Rest], Acc) when A band 16#f8 =:= 16#f0,
                                        B band 16#c0 =:= 16#80,
                                        C band 16#c0 =:= 16#80,
                                        D band 16#c0 =:= 16#80 ->
    %% 11110vvv 10vvvvvv 10vvvvvv 10vvvvvv
    case ((D band 16#3f) bor ((C band 16#3f) bsl 6) bor
              ((B band 16#3f) bsl 12) bor ((A band 16#07) bsl 18)) of
        Ch when Ch >= 16#10000 -> %% we should add some other charset check
            do_utf8_2_ucs(Rest, [Ch|Acc]);
        Ch ->
            do_utf8_2_ucs(Rest, [Ch|Acc])
    end;
do_utf8_2_ucs([A,B,C|Rest], Acc) when A band 16#f0 =:= 16#e0,
                                      B band 16#c0 =:= 16#80,
                                      C band 16#c0 =:= 16#80 ->
    %% 1110vvvv 10vvvvvv 10vvvvvv
    case ((C band 16#3f) bor ((B band 16#3f) bsl 6) bor
              ((A band 16#0f) bsl 12)) of
        Ch when Ch >= 16#800 -> %% we should add some other charset check
            do_utf8_2_ucs(Rest, [Ch|Acc]);
        Ch ->
            do_utf8_2_ucs(Rest, [Ch|Acc])
    end;
do_utf8_2_ucs([A,B|Rest], Acc) when A band 16#e0 =:= 16#c0,
                                    B band 16#c0 =:= 16#80 ->
    %% 110vvvvv 10vvvvvv
    case ((B band 16#3f) bor ((A band 16#1f) bsl 6)) of
        Ch when Ch >= 16#80 -> %% we should add some other charset check
            do_utf8_2_ucs(Rest, [Ch|Acc]);
        Ch ->
            do_utf8_2_ucs(Rest, [Ch|Acc])
    end;

do_utf8_2_ucs([A|Rest], Acc) ->
    do_utf8_2_ucs(Rest, [A|Acc]);
do_utf8_2_ucs([], Acc) ->
    lists:reverse(Acc).

%% @doc We check the term type of the parameters ,
%%       before we change a utf-8 string to unicode
sage_82u(Params) when is_list(Params)->
    utf8_to_ucs(Params);
sage_82u(Params) ->
    Params.


%% unicode to utf8
safe_u28(Params) when is_list(Params) ->
    ucs_to_utf8(Params);
safe_u28(Params) ->
    Params.

ucs_to_utf8(List) ->
    lists:concat(do_ucs_to_utf8(List)).

do_ucs_to_utf8([]) ->
    [];
do_ucs_to_utf8([Ch|Rest]) ->
    [ucs_2_utf8(Ch)|do_ucs_to_utf8(Rest)].

ucs_2_utf8(Ch) when Ch < 16#0080 ->
    [Ch];
ucs_2_utf8(Ch) when Ch< 16#0800 ->
    [((Ch band 16#07C0) bsr 6 ) bor 16#C0,
    (Ch band 16#003F) bor 16#80];
ucs_2_utf8(Ch) when Ch< 16#10000 ->
    [((Ch bsr 12) band 16#000F) bor 16#E0,
     ((Ch bsr 6) band 16#003F) bor 16#0080,
     (Ch band 16#003F) bor 16#0080];
ucs_2_utf8(Ch) when Ch < 16#20000->
    [((Ch bsr 18) band 16#0007) bor 16#F0,
     ((Ch bsr 12) band 16#003F) bor 16#0080,
     ((Ch bsr 6) band 16#003F) bor 16#0080,
     (Ch band 16#003F) bor 16#0080].
