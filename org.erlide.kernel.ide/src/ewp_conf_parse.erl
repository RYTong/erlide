%% Author: jcrom
%% Created: Jul 24, 2013
%% Description: TODO: Add description to ewp_conf_parse
-module(ewp_conf_parse).

%%
%% Include files
%%
-define(EWP, 1).

-include("erlide.hrl").

%%%===================================================================
%%% API
%%%===================================================================

%%
%% Exported Functions
%%
%%-compile([export_all]).
-export([parse_channel_config/2,
		 change_index/2,
		 edit_conf/5,
		 add_collection/2,
		 remove_collection/2,
		 remove_channel/2,
		 add_channel/2]).

%%
%% API Functions
%%
-define(CONF_FOLDER, "config").
-define(CHANNEL_CONF,"channel.conf").

-define(COLL, 'collections').
-define(CHA, 'channels').
-define(ID, 'id').
-define(ITEMS, 'items').
-define(ITEM_ID, 'item_id').
-define(ITEM_TYPE, 'item_type').
-define(MENU_ORDER, 'menu_order').
-define(MAX_INDEX, 99999).

-define(ITEM_CHA, 1).
-define(ITEM_COLL, 0).

%%--------------------------------------------------------------------
%% @doc
%% @spec %% add a new channel
%% @end
%%--------------------------------------------------------------------
add_channel(Key, Params) ->


	?ewp_log({params, Params}),

	%% {"testjc","ebank","test","channel_adapter","1",[{"encrypt_flag","1"},{"method","post"}]}
	CKey = binary_to_term((Key)),

	TmpChaList = proplists:get_value(?CHA, CKey),
	NewChannel = do_add_cha(Params),

	?ewp_log({newChannel, NewChannel}),
	ChaList =
		case erlang:element(1, Params) of
			"" -> TmpChaList;
			SId ->
				[X||X<-TmpChaList, proplists:get_value(id, X)/=SId]
		end,

	NewKey = proplists:delete(?CHA, CKey)++[{?CHA, [NewChannel|ChaList]}],
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].

do_add_cha({_SId, Id, App, Name, Entry, Views, State, Props}=Params) ->
	new_channel(Id, App, Name, Entry, Views, State, Props).

new_channel(Id, App, Name, Entry, Views, State, Props)->
	[{id, Id},
	 {app, App},
	 {name, Name},
	 {entry, check_params(Entry)},
	 {views, check_params(Views)},
	 {props, check_props(Props)},
	 {state, State}].

check_params("") ->
	undefined;
check_params(Else) ->
	Else.

check_props([])->
	[];
check_props(Props) ->
	check_props(Props, []).

check_props([{Key, Value}|Next], Acc) ->
	NV =
		case Value of
			"" -> undefined;
			[$"|S]->
				[X||X<-S, X/=$"];
			[$<|B] ->
				list_to_binary([X||X<-B, X/=$<, X/=$>]);
			[T|L] when T ==$[ ,T == ${ ->
				"";
			AorI ->
				Fun = fun(X)->
							  if X >= $0 andalso X =< $9 ->
									 true;
								 X == $. ->
									 float;
								 true -> false
							  end
					  end,
				FList = [Fun(X)||X<-AorI],
				case lists:member(false, FList) of
					true -> list_to_atom(AorI);
					_ ->
						case lists:member(float, FList) of
							true ->
								list_to_float(AorI);
							_ ->
								list_to_integer(AorI)
						end
				end

		end,
	check_props(Next, [{Key, NV}|Acc]);
check_props([], Acc) ->
	lists:reverse(Acc).



%%--------------------------------------------------------------------
%% @doc
%% @spec %% remove a channel
%% @end
%%--------------------------------------------------------------------
remove_channel(Key, Id)->
    ?ewp_log({id, Id}),
	CKey = binary_to_term((Key)),
	ChaList = proplists:get_value(?CHA, CKey),
	NewChannels = do_remove_cha(ChaList, Id),

	CollList = proplists:get_value(?COLL, CKey),
	NewCollections = case check_coll_item(CollList, Id) of
						 CollList ->
							 ?ewp_log({nochange, true}),
							 CollList;
						 Else ->
							 ?ewp_log({nochange, false}),
							 ewp_check_conf:check_item(Else)
					 end,
	?ewp_log({newChaList, NewChannels}),
    ?ewp_log({newCollList, NewCollections}),
	NewKey = [{?CHA, NewChannels},{?COLL, NewCollections}],
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].

do_remove_cha(ChaList, Id) ->
	lists:foldr(fun(Cha, Acc) ->
						ItemId = proplists:get_value(?ID, Cha),
						case lists:member(ItemId, Id) of
							true -> Acc;
							_ -> [Cha|Acc]
						end
				end,
				[], ChaList).

check_coll_item(CollList, Id) ->
	check_coll_item(CollList, Id, []).
check_coll_item([Coll|Next], Id, Acc) ->
	case proplists:get_value(?ITEMS, Coll) of
		[] ->
			check_coll_item(Next, Id, [Coll|Acc]);
		Items ->
			NewItems = lists:foldr(fun(Item, Acc) ->
								ItemId = proplists:get_value(?ITEM_ID, Item),
								ItemType = proplists:get_value(?ITEM_TYPE, Item),
								Flag = lists:member(ItemId, Id),
								if Flag andalso ItemType == ?ITEM_CHA ->
									   Acc;
								   true ->
									   [Item|Acc]
								end
						end,
						[], Items),
			LeftCha = proplists:delete(?ITEMS, Coll),
			check_coll_item(Next, Id, [LeftCha++[{?ITEMS, NewItems}]|Acc])
	end;
check_coll_item([], Id, Acc) ->
	lists:reverse(Acc).


%%--------------------------------------------------------------------
%% @doc
%% @spec %% change the tree items order
%% @end
%%--------------------------------------------------------------------
remove_collection(Key, Id)->
    ?ewp_log({id, Id}),
	CKey = binary_to_term((Key)),
	CollList = proplists:get_value(?COLL, CKey),
	NewCollections = do_remove_cha(CollList, Id),
	?ewp_log({newCollList, NewCollections}),
    SortCollList = ewp_check_conf:check_item(NewCollections),
	?ewp_log({newChannelList, SortCollList}),
	NewKey = proplists:delete(?COLL, CKey)++[{?COLL, SortCollList}],
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].

%% do_remove_coll(CollList, Id) ->
%% 	lists:foldr(fun(Coll, Acc) ->
%% 						case proplists:get_value(?ID, Coll) of
%% 							Id -> Acc;
%% 							_ -> [Coll|Acc]
%% 						end
%% 				end,
%% 				[], CollList).

%%--------------------------------------------------------------------
%% @doc
%% @spec %% change the tree items order
%% @end
%%--------------------------------------------------------------------
add_collection(Key, Params)->
	%%{"test","ebank",[27979,35797],[],[],"1","0",[]}
	?ewp_log({params, Params}),
	CKey = binary_to_term((Key)),
	TmpCollList = proplists:get_value(?COLL, CKey),
	NewCollection = do_add_coll(Params),
	?ewp_log({newCollList, NewCollection}),

	CollList =
		case erlang:element(1, Params) of
			"" -> TmpCollList;
			SId ->
				[X||X<-TmpCollList, proplists:get_value(id, X)/=SId]
		end,

    SortCollList = ewp_check_conf:check_item([NewCollection|CollList]),
	?ewp_log({newChannelList, SortCollList}),
	NewKey = proplists:delete(?COLL, CKey)++[{?COLL, SortCollList}],
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].

do_add_coll({_SId, Id, App, Name, Url, Uid, Type, State, []=Item}=Params)->
	NewId = Id,
	NewApp = check_coll_app(App),
	NewName = check_coll_name(Name),
	NewUrl = check_coll_Url(Url),
	NewUid = check_coll_Uid(Uid),
	NewType = list_to_integer(Type),
	NewState = list_to_integer(State),
	new_collection(Id, NewApp, NewName, NewUrl, NewUid, NewType, NewState, Item);
do_add_coll({_SId, Id, App, Name, Url, Uid, Type, State, Item}=Params)->
	%%Item = [{"ebank_home","1",1},{"yecx1","1",2},{"jydl","1",3}],
	NewId = Id,
	NewApp = check_coll_app(App),
	NewName = check_coll_name(Name),
	NewUrl = check_coll_Url(Url),
	NewUid = check_coll_Uid(Uid),
	NewType = list_to_integer(Type),
	NewState = list_to_integer(State),
	%% new_item(Id, Type, order)
	Items = lists:foldr(fun({ItemId, ItemType, ItemOrder}, Acc)->
								[new_item(ItemId, ItemType, ItemOrder)|Acc]
						end,
						[], Item),
	new_collection(Id, NewApp, NewName, NewUrl, NewUid, NewType, NewState, Items).

check_coll_app(App) when is_list(App)->
	list_to_atom(App);
check_coll_app(App) ->
	App.

check_coll_name(Name) when is_list(Name) ->
	binary_to_list(unicode:characters_to_binary(Name)).

check_coll_Url("") ->
	undefined;
check_coll_Url(Url)->
	Url.

check_coll_Uid("") ->
	undefined;
check_coll_Uid(Uid)->
	Uid.

new_collection(Id, App, Name, Url, Uid, Type, State, Item)->
	[{id, Id},
	 {app, App},
	 {name, Name},
	 {url, Url},
	 {user_id, Uid},
	 {type, Type},
	 {state, State},
	 {items, Item}].

%%--------------------------------------------------------------------
%% @doc
%% @spec %% change the tree items order
%% @end
%%--------------------------------------------------------------------
change_index(Key, {OldPar, NewPar, Id, Type, Index}=Params) ->
	?ewp_log({params, Params}),

	CKey = binary_to_term((Key)),

	CollList = proplists:get_value(?COLL, CKey),
	NewCollList = edit_index(Params, CollList),
		?ewp_log({newCollList, NewCollList}),
    SortCollList = ewp_check_conf:check_item(NewCollList),
	?ewp_log({newChannelList, SortCollList}),
	NewKey = proplists:delete(?COLL, CKey)++[{?COLL, SortCollList}],
	%%?ewp_log({tre, NewKey}),
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].

edit_index({OldPar, NewPar, Id, Type, Index}, CollList) when OldPar /= [] andalso NewPar == [] ->
	lists:foldr(fun(Coll, Acc)->
						case proplists:get_value(?ID, Coll) of
							OldPar ->
								ItemsList = proplists:get_value(?ITEMS, Coll),
								?ewp_log({itemsList, ItemsList}),
								NewItemsList = remove_items(ItemsList, Id),
								[proplists:delete(?ITEMS, Coll)++
									 [{?ITEMS, NewItemsList}]|Acc];
							_ ->
								[Coll|Acc]
						end
				end, [], CollList);
edit_index({OldPar, NewPar, Id, Type, Index}, CollList) when OldPar == [] andalso NewPar /= [] ->
	lists:foldl(fun(Coll, Acc)->
						case proplists:get_value(?ID, Coll) of
							NewPar ->
								ItemsList = proplists:get_value(?ITEMS, Coll),
								NewItemsList = add_items(ItemsList, Id, Type, Index),
								[proplists:delete(?ITEMS, Coll)++
									 [{?ITEMS, NewItemsList}]|Acc];
							_ ->
								[Coll|Acc]
						end
				end, [], CollList);
edit_index({OldPar, NewPar, Id, Type, Index}, CollList) when OldPar /= [] andalso NewPar /= [] ->

	lists:foldl(fun(Coll, Acc)->
						case proplists:get_value(?ID, Coll) of
							Some when NewPar==OldPar ->
								ItemsList = proplists:get_value(?ITEMS, Coll),
								NewItemsList = add_items(ItemsList, Id, Type, Index),
								[proplists:delete(?ITEMS, Coll)++
									 [{?ITEMS, NewItemsList}]|Acc];
							NewPar ->
								ItemsList = proplists:get_value(?ITEMS, Coll),
								NewItemsList = add_items(ItemsList, Id, Type, Index),
								[proplists:delete(?ITEMS, Coll)++
									 [{?ITEMS, NewItemsList}]|Acc];
							OldPar ->
								ItemsList = proplists:get_value(?ITEMS, Coll),
								NewItemsList = remove_items(ItemsList, Id),
								[proplists:delete(?ITEMS, Coll)++
									 [{?ITEMS, NewItemsList}]|Acc];
							_ ->
								[Coll|Acc]
						end
				end, [], CollList);
edit_index({OldPar, NewPar, Id, Type, Index}, CollList)  ->
	CollList.

check_index("") ->
	"";
check_index(Index) when is_list(Index)->
	list_to_integer(Index)+1;
check_index(Index) when is_integer(Index)->
	Index+1;
check_index(_) ->
	"".

check_index([Item|Next], Id, Index) ->
	case proplists:get_value(?ITEM_ID, Item) of
		Id ->
			NIndex = proplists:get_value(?MENU_ORDER, Item),
			check_index(Next,  Id, NIndex);
		_ ->
			check_index(Next,  Id, Index)
	end;
check_index([], _Id, Index) ->
	check_index(Index).

remove_items(ItemList, Id) ->
	remove_items(ItemList, Id, []).
remove_items([Item|Next], Id, Acc) ->
	case proplists:get_value(?ITEM_ID, Item) of
		Id ->
			remove_items(Next, Id, Acc);
		_ ->
		    remove_items(Next, Id, [Item|Acc])
	end;
remove_items([], _Id, Acc) ->
	lists:reverse(Acc).

%% FIXME redefine thie function when Index is "", we do not loop process the list
add_items([], Id, Type, Index) ->
	[new_item(Id, Type, Index)];
add_items([Item|Next]=ItemList, Id, Type, StrIndex) ->
	FlagList = lists:filter(fun(Item)->
									case proplists:get_value(?ITEM_ID, Item) of
										Id -> true;
										_ -> false
									end
							end, ItemList),
	Index = check_index(StrIndex),
	Len = length(ItemList),
	?ewp_log({newindex, StrIndex, Index}),
	case FlagList of
		[] ->
			%% we assume that the item list in collections is all in order.
            %% But when there is no order number in a item,
            %% we assume thers is no order number in all items.
			FlagIndex = proplists:get_value(?MENU_ORDER, Item),
			if Index=="" ->
				   ItemList++[new_item(Id, Type, Index)];
			   FlagIndex == undefined ->
				   {_I, ReList} = lists:foldl(fun(Item, {TmpIndex, Acc}) when TmpIndex == Index->
													  {TmpIndex+1, [Item, new_item(Id, Type, Index)|Acc]};
												 (Item, {TmpIndex, Acc}) ->
													  {TmpIndex+1, [Item|Acc]}
											  end, {1, []}, ItemList),
				   lists:reverse(ReList);
			   true ->
				   {_, _, ReList} =
					   lists:foldl(fun(Item, {TmpLen, 0, Acc}) when TmpLen==Len->
										   OIndex = proplists:get_value(?MENU_ORDER, Item),

										   if OIndex < Index ->
												  {TmpLen, 1, [new_item(Id, Type, Index), Item|Acc]};
											  true ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpLen+1, 1, [NItem++[{?MENU_ORDER, OIndex+1}], new_item(Id, Type, Index)|Acc]}
										   end;
									  (Item, {TmpIndex, Flag, Acc})->
										   OIndex = proplists:get_value(?MENU_ORDER, Item),

										   if OIndex == Index ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpIndex+1, 1, [NItem++[{?MENU_ORDER, OIndex+1}], new_item(Id, Type, Index)|Acc]};
											  OIndex > Index ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpIndex+1, Flag, [NItem++[{?MENU_ORDER, OIndex+1}]|Acc]};
											  true ->
												  {TmpIndex+1, Flag, [Item|Acc]}
										   end
								   end, {1, 0, []}, ItemList),
				   lists:reverse(ReList)
			end;
		_ ->
			if Index == "" ->
				   ItemList;
			   true ->
				   Olditem = hd(FlagList),
				   OOrder = proplists:get_value(?MENU_ORDER, Olditem),

				   {_, _, ReList} =
					   lists:foldl(fun(Item, {TmpLen, 0, Acc}) when TmpLen==Len->
										   {TmpLen, 1, [Item, new_item(Id, Type, Index)|Acc]};
									  (Item, {TmpIndex, Flag, Acc})->
										   OIndex = proplists:get_value(?MENU_ORDER, Item),
										   TmpId = proplists:get_value(?ITEM_ID, Item),
										   if TmpId == Id  ->
												  case Index of
													  OOrder ->
														  {TmpIndex+1, 1, [Item|Acc]};
													  _ ->
														  {TmpIndex+1, 0, Acc}
												  end;
											  OIndex == Index ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpIndex+1, 1, [NItem++[{?MENU_ORDER, OIndex+1}], new_item(Id, Type, Index)|Acc]};
											  OIndex < OOrder andalso OIndex > Index ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpIndex+1, Flag, [NItem++[{?MENU_ORDER, OIndex+1}]|Acc]};
											  OIndex > OOrder andalso OIndex < Index ->
												  NItem = proplists:delete(?MENU_ORDER, Item),
												  {TmpIndex+1, Flag, [NItem++[{?MENU_ORDER, OIndex-1}]|Acc]};
											  true ->
												  {TmpIndex+1, Flag, [Item|Acc]}
										   end
								   end, {1, 0, []}, ItemList),
				   lists:reverse(ReList)
			end
	end.


new_item(Id, Type, Index)->
	[{item_id,Id},{item_type, to_integer(Type)},{menu_order,Index}].


%%--------------------------------------------------------------------
%% @doc
%% @spec %% a interface for edite a channel conf item
%% @end
%%--------------------------------------------------------------------
edit_conf(Key, Flag, ChaId, Id, Value) ->
	ConfFlag = case list_to_atom(Flag) of
				   ?COLL -> ?COLL;
				   ?CHA -> ?CHA
			   end,
	?ewp_log({params, Key, ChaId, Id, Value}),
	CKey = binary_to_term((Key)),
	ChaList = proplists:get_value(ConfFlag, CKey),
	NewChannelList = replace_conf(ChaList, ChaId, Id, Value),
	NewKey = proplists:delete(ConfFlag, CKey)++[{ConfFlag, NewChannelList}],
	?ewp_log({tre, NewKey}),
	[lists:flatten(io_lib:format("~p.~n~p.",NewKey)), term_to_binary(NewKey)].


replace_conf(ConfList, ConfId, Id, Value) ->
	replace_conf(ConfList, ConfId, Id, Value, []).
replace_conf([Conf|Next], ConfId, Id, Value, Acc) ->
	case proplists:get_value(id, Conf) of
		ConfId ->
			AId= list_to_atom(Id),
			?ewp_log({replace, {Conf, AId, Value}}),
			NewConf = replace_proplist(Conf, AId, Value, []),
			?ewp_log({newcoll, NewConf}),
			replace_conf(Next, ConfId, Id, Value, [NewConf|Acc]);
		_ -> replace_conf(Next, ConfId, Id, Value, [Conf|Acc])
	end;
replace_conf([], _ConfId, _Id, _Value, Acc) ->
	lists:reverse(Acc).

replace_proplist([{Id,Value1}|Next], Id, Value, Acc) ->
	NValue = convert(Value1, Value),
	%%?ewp_log({conver, {Value1, Value, NValue}}),
	replace_proplist(Next, Id, Value, [{Id,NValue}|Acc]);
replace_proplist([{Id1,Value1}|Next], Id, Value, Acc) ->
	replace_proplist(Next, Id, Value, [{Id1,Value1}|Acc]);
replace_proplist([], Id, Value, Acc)->
	lists:reverse(Acc).

convert(V1, []) ->
	undefined;
convert(V1, V) when is_list(V1)->
	V;
convert(V1, V) when is_integer(V1) ->
	list_to_integer(V);
convert(V1, V) when is_atom(V1) ->
	list_to_atom(V);
convert(V1, V) when is_float(V1) ->
	list_to_float(V);
convert(V1, V) when is_binary(V1) ->
	list_to_binary(V);
convert(V1, V) when is_tuple(V1) ->
	list_to_tuple(V).

%%--------------------------------------------------------------------
%% @doc
%% @spec parse conf content from java
%% @end
%%--------------------------------------------------------------------
-define(TYPE_TEXT, "text").
-define(TYPE_PATH, "path").
parse_channel_config(?TYPE_TEXT, Conf) ->
	%%?ewp_log({test, Conf}),
	try
		UtfConf = binary_to_list(unicode:characters_to_binary(Conf)),
		case ewp_check_conf:string_to_term(UtfConf) of
			error -> throw("String to term error");
			Term ->
				NewContent = ewp_check_conf:check_conf(Term),
				Con = parse_conf(NewContent),
				%%?ewp_log({parse_over, Con}),
				Re = [unicode:characters_to_list(list_to_binary(Con)), term_to_binary(Term)],
				%%?ewp_log({result_text, Re}),
				Re
		end

	catch Type:Error ->
			  ?ewp_log({error, Type,Error}),
			  %%?ewp_log({error, erlang:get_stacktrace()}),
			  [[],<<>>]
	end;
%%--------------------------------------------------------------------
%% @doc
%% @spec  parse conf content from file path
%% @end
%%--------------------------------------------------------------------
parse_channel_config(?TYPE_PATH, FilePath) ->
	?ewp_log({path, FilePath}),
	case file:consult(FilePath) of
		{ok, Content} ->
			NewContent = ewp_check_conf:check_conf(Content),
			Con = parse_conf(NewContent),
			Re = [unicode:characters_to_list(list_to_binary(Con)), term_to_binary(Content)],
			?ewp_log({result, Re}),
			Re;
		{error, Error} ->
			?ewp_log({error_path, Error}),
			[[],<<>>]
	end.

%%--------------------------------------------------------------------

-record(xml_element, {element,
                      value="",
                      children=[],
                      attribute=[]
                     }).

-record(xml_attribute,{name=[],
                       value=[]}).

-define(XML, #xml_element{element="root"}).
-define(Data_Type(Value), [#xml_attribute{name="data_type", value=Value}]).


parse_conf(TList)  ->
    case length(TList) of
        Int when Int > 0 ->
            %% AllKeys = proplists:get_keys(TList),
            Result = do_parse(TList, ?XML),
            %%?ewp_log({result, Result}),
            to_xml(Result);
        _ -> [] %% FIXME
    end.

do_parse([Params|Next], XML)->
    %%?ewp_log({params, Params}),
    Re = parse(Params),
    %%?ewp_log({re, Re}),
    case Re of
        [] ->
            do_parse(Next, XML);
        Children ->
            XMLChildren = XML#xml_element.children,
            do_parse(Next, XML#xml_element{children=[Children|XMLChildren]})
    end;
do_parse([], Acc) ->
    Acc.


-define(VALUE, 0).
-define(CHILDREN, 1).

-define(STR,    0).
-define(INT,    1).
-define(FLO,    2).
-define(BIN,    3).
-define(ATOM,   4).
-define(TUP,    5).


%% FIXME {1,2,3,4,...}
parse({Key, Value}) ->
    %% IniKey = #xml_element{element=Key},
    case parse_value(Value) of
        {?VALUE, Attr, PValue} ->
            #xml_element{element=Key, value=PValue, attribute=?Data_Type(Attr)};
        {?CHILDREN, _, PValue} ->
            #xml_element{element=Key, children=PValue}
    end;
parse(List) when is_list(List) ->
    do_parse_list(List, []);
parse(_) ->
    "".

do_parse_list([Params|Next], Acc)->
    Children = parse(Params),
    do_parse_list(Next, [Children|Acc]);
do_parse_list([], Acc) ->
    lists:reverse(Acc).


parse_value([Int|_]=List) when is_integer(Int) ->
    {?VALUE, ?STR, List};
parse_value(List) when is_list(List) ->
    {?CHILDREN, "", parse(List)};
parse_value(Tuple) when is_tuple(Tuple) ->
    {?VALUE, ?TUP, lists:flatten(io_lib:format("~p",[Tuple]))};
parse_value(Atom) when is_atom(Atom) ->
    {?VALUE, ?ATOM, Atom};
parse_value(Binary) when is_binary(Binary) ->
    {?VALUE, ?BIN, Binary};
parse_value(Int) when is_integer(Int) ->
    {?VALUE, ?INT, Int};
parse_value(Float) when is_float(Float) ->
    {?VALUE, ?FLO, Float};
parse_value(_) ->
    {?VALUE, ?STR, ""}.







%% do_parse([List|Next]=TList, XML) when is_list(List) ->
%%     "";
%% do_parse([{Key, Value}|Next]=TList, XML)->
%%     Re = #xml_element{element=Key, value=Value},
%%     Children = XML#xml_element.children,
%%     do_parse(Next, XML#xml_element{children=[Re|Children]});
%% do_parse([Else|Next]=TList, Acc) ->
%%     do_parse(Next, Acc);
%% do_parse([], Acc) ->
%%     "".


%%--------------------------------------------------------------------
%% @doc
%% @spec
%% @end
%%--------------------------------------------------------------------

%%%===================================================================
%%% Internal functions
%%%===================================================================
-define(ADD, 1).
-define(UNADD, 0).

to_xml(Record) ->
	{_, Content}= to_xml(Record, []),
	Content.
to_xml(Record, _Parent) when is_record(Record, xml_element)->
    Element = Record#xml_element.element,
    Attribute = to_attribute(Record#xml_element.attribute),
    case Record#xml_element.children of
        [] ->
            Value = Record#xml_element.value,
            {?ADD, lists:concat(["<",Element, Attribute, ">", Value, "</",Element, ">"])};
        Children ->
            {?ADD, child_to_xml(Children, Element, ?ADD, [])}
            %%lists:concat(["<",Element, Attribute, ">", ChildXml, "</",Element, ">"])
    end;

to_xml([Record|_]=List, Parent) when is_list(List),is_record(Record,xml_element) ->
    {?UNADD, child_to_xml(List, Parent, ?ADD, [])};
to_xml(String, _Parent) when is_list(String) ->
    {?ADD, String};
to_xml(_, _Parent) ->
    {?ADD, ""}.

child_to_xml([Record|Next], ParentElement, Flag, Acc) ->
    {NFlag, Result} = to_xml(Record, ParentElement),
    child_to_xml(Next, ParentElement, NFlag, [Result|Acc]);
child_to_xml([], ParentElement, ?ADD, Acc) ->
    Result = lists:concat(lists:reverse(Acc)),
	lists:concat(["<", ParentElement, ">", Result, "</", ParentElement, ">"]);
child_to_xml([], ParentElement, ?UNADD, Acc) ->
    lists:concat(lists:reverse(Acc)).



to_attribute([]) ->
    "";
to_attribute(Attribute) ->
    do_to_attribute(Attribute, "").

do_to_attribute([Attribute|Next], Acc) when is_record(Attribute, xml_attribute)->
    Name = Attribute#xml_attribute.name,
    Value = Attribute#xml_attribute.value,
    case Name of
        [] ->
            do_to_attribute(Next, Acc);
        _ ->
            AttStr = lists:concat([" ", Name, "='", Value, "'"]),
            do_to_attribute(Next, AttStr++Acc)
    end;
do_to_attribute([_|Next], Acc) ->
    do_to_attribute(Next, Acc);
do_to_attribute([], Acc) ->
    Acc.

to_integer(P) ->
	case P of
		L when is_list(L) ->
			list_to_integer(L);
		I when is_integer(I) ->
			I;
		B when is_binary(B) ->
			list_to_integer(binary_to_list(B));
		A when is_atom(A) ->
			list_to_integer(atom_to_list(A));
		true ->
			P
	end.


