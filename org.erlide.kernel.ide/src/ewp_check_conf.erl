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
		 string_to_term/1]).

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