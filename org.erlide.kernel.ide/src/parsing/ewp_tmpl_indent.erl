%% Author: caoxu
%% Created: 2012-6-7
%% Description: parse cs file to do the indentation
-module(ewp_tmpl_indent).

%%
%% Include files
%%
%% -define(DEBUG, 1).
%% -define(IO_FORMAT_DEBUG, 1).

%%-define(EWP, 1).

-include("erlide.hrl").

-define(Tab, 4).
%%
%% Exported Functions
%%
-export([indent_lines/3]).

%%
%% API Functions
%%
%% @FIXME 该函数并没有使用上一个标签的位置来格式化之后的代码，
%% 需要使用FIrst参数作为标示来计算格式化代码的位置
indent_lines(_Offset, _Length, Text) ->
    ?D(Text),
    Str = try
              re:replace(Text, "\\t", "    ", [global, {return, list}, unicode])
          catch _:_ ->
                    Text
          end,

    %%?ewp_log(Str),
    {First, FirstLineNum, Lines} = erlide_text:get_text_and_lines(Str, 0, length(Str)),
    ?D(Lines),
    do_indent_lines(Lines).

%%
%% Local Functions
%%

do_indent_lines(Lines) ->
    %% get whitespace length of the first line
    Base = case catch(re:run(hd(Lines), "^\\s*", [unicode])) of
               {match, [{0, Int}]} ->
                   Int;
               _ ->
                   0
           end,
    ?D(Base),
    indent(Lines, Base, false, []).

indent([], _, _IsLua, Res) ->
    ?D(Res),
    Res;
%% indent([Line|Rest], Length, true, Res) ->
%%     Striped = string:strip(Line, left),
%%     LineToken = parse_lua(Line),
%%     ?D({Line,LineToken}),
%%     {NewLine, NewLen, IsLua} = indent_line(LineToken, Length, true),
%%     ?D({NewLine, NewLen, IsLua}),
%%     indent(Rest, NewLen, IsLua, Res++NewLine);
indent([Line|Rest], Length, IsLua, Res) ->
    Striped = string:strip(Line, left),
    LineToken = case IsLua of
                    true ->
                        parse_lua(Striped);
                    _ ->
                        parse(Striped)
                end,
    ?D(LineToken),
    {NewLine, NewLen, NewIsLua} = indent_line(LineToken, Length, IsLua),
    ?D({NewLine, NewLen, NewIsLua}),
    indent(Rest, NewLen, NewIsLua, Res++NewLine).


%%     IndentNext = [cs_if_begin, cs_each_begin, cs_loop_begin, cs_def,
%%                   xml_tag_begin, lua_func_begin],
%%     Back1 = [cs_if_end, cs_def_end, cs_loop_end, cs_each_end, xml_tag_end, lua_end],
%%     Back2 = [cs_else, cs_elif],
%% {indent_begin, Line};
%% {indent_next, Line};
%% {indent_end, Line};
%% {literal, Line};

parse_lua("function"++ _ =Line) ->
    {indent_begin, Line};
parse_lua("if"++ _ =Line) ->
    {indent_begin, Line};
parse_lua("for"++ _ =Line) ->
    {indent_begin, Line};
parse_lua("do"++ _ =Line) ->
    {indent_begin, Line};
parse_lua("else"++ _ =Line) ->
    {indent_next, Line};
parse_lua("end"++ _=Line) ->
    %{lua_end, Line};
    {indent_end, Line};
parse_lua("]]>"++ _=Line) ->
    %{lua_end, Line};
    {cdata_end, Line};
parse_lua(Line) ->
    {literal, Line}.


parse(Line) ->
    case Line of
        "<?xml" ++ _ ->
            {xml_begin, Line};
        "<?cs" ++ _ ->
            parse_cs(Line);
        "<![CDATA[" ++ _ ->
            {cdata_begin, Line};
        %% XXX this branch should not be reached here,
        %% it should be parsed in parse_lua
        "]]>" ++ _ ->
            {cdata_end, Line};
        "</" ++ _ ->
            {indent_end, Line};
        "<!" ++ _ ->
            {literal, Line};
        "<?" ++ _ ->
            {literal, Line};
        "<" ++ _ ->
            parse_xml_tag(Line);
        _ ->
            {literal, Line}
    end.

parse_xml_tag(Line) ->
    try
        case re:run(Line, "(<.*/>|<.*>.*</.*>)", [unicode]) of
            {match, _} ->
                {literal, Line};
            _ ->
                {indent_begin, Line}
        %{xml_tag_begin, Line}
        end
    catch _:_ ->
              {literal, Line}
    end.


parse_cs("<?cs if" ++ _ = Line)  ->
    {indent_begin, Line};
parse_cs("<?cs else"++ _ = Line) ->
    {indent_next, Line};
parse_cs("<?cs elif"++_ = Line) ->
    {indent_next, Line};
parse_cs("<?cs /if"++_ =Line) ->
    {indent_end, Line};
parse_cs("<?cs def"++_ =Line) ->
    {indent_begin, Line};
parse_cs("<?cs /def"++_ =Line) ->
    {indent_end, Line};
parse_cs("<?cs each"++_ =Line) ->
    {indent_begin, Line};
parse_cs("<?cs /each"++_ =Line) ->
    {indent_end, Line};
parse_cs("<?cs loop"++_ =Line) ->
    {indent_begin, Line};
parse_cs("<?cs /loop"++_ =Line) ->
    {indent_end, Line};
parse_cs(Line) ->
    {literal, Line}.

whitespaces(Len) when Len > 0 ->
    lists:concat(lists:duplicate(Len, " "));
whitespaces(Len) ->
    [].

indent_line({xml_begin, Line}, _Length, IsLua) ->
    {Line, 0, IsLua};
indent_line({cdata_begin, Line} , Len, _IsLua) ->
    Head = whitespaces(Len),
    {Head++Line, Len, true};
indent_line({cdata_end, Line} , Len, _IsLua) ->
    Head =  whitespaces(Len),
    {Head ++ Line, Len, false};
indent_line({indent_begin, Line}, Len, IsLua) ->
    Head = whitespaces(Len),
    {Head ++ Line, Len + ?Tab, IsLua};
indent_line({indent_end, Line}, Len, IsLua) ->
    NewLen = Len-?Tab,
    Head = whitespaces(NewLen),
    {Head ++ Line, NewLen, IsLua};
indent_line({indent_next, Line}, Len, IsLua) ->
    HeadLen = Len-?Tab,
    Head = whitespaces(HeadLen),
    {Head ++ Line, Len, IsLua};
indent_line({literal, Line}, Len, IsLua) ->
    Head = whitespaces(Len),
    {Head ++ Line, Len, IsLua};
indent_line({_, Line}, Len, IsLua) ->
    Head = whitespaces(Len),
    {Head ++ Line, Len, IsLua}.