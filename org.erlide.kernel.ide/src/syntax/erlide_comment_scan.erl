%% =====================================================================
%% Reading comment lines from Erlang source code.
%%
%% Copyright (C) 1997-2001 Richard Carlsson
%%
%% This library is free software; you can redistribute it and/or modify
%% it under the terms of the GNU Lesser General Public License as
%% published by the Free Software Foundation; either version 2 of the
%% License, or (at your option) any later version.
%%
%% This library is distributed in the hope that it will be useful, but
%% WITHOUT ANY WARRANTY; without even the implied warranty of
%% MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
%% Lesser General Public License for more details.
%%
%% You should have received a copy of the GNU Lesser General Public
%% License along with this library; if not, write to the Free Software
%% Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
%% USA
%%
%% Author contact: richardc@csd.uu.se
%%
%% $Id$
%%
%% =====================================================================
%%
%% @doc Functions for reading comment lines from Erlang source code.

-module(erlide_comment_scan).

-export([file/1, join_lines/1, scan_lines/1, string/1]).


%% =====================================================================
%% @spec file(FileName::file:filename()) -> [Comment]
%%
%%	    Comment = {Line, Column, Indentation, Text}
%%	    Line = integer()
%%          Column = integer()
%%          Indentation = integer()
%%          Text = [string()]
%%
%% @doc Extracts comments from an Erlang source code file. Returns a
%% list of entries representing <em>multi-line</em> comments, listed in
%% order of increasing line-numbers. For each entry, <code>Text</code>
%% is a list of strings representing the consecutive comment lines in
%% top-down order; the strings contain <em>all</em> characters following
%% (but not including) the first comment-introducing "<code>%</code>"
%% character on the line, up to (but not including) the line-terminating
%% newline.
%%
%% <p>Furthermore, <code>Line</code> is the line number and
%% <code>Column</code> the left column of the comment (i.e., the column
%% of the comment-introducing <code>%</code> character).
%% <code>Indent</code> is the indentation (or padding), measured in
%% character positions between the last non-whitespace character before
%% the comment (or the left margin), and the left column of the comment.
%% <code>Line</code> and <code>Column</code> are always positive
%% integers, and <code>Indentation</code> is a nonnegative integer.</p>
%%
%% <p>Evaluation exits with reason <code>{read, Reason}</code> if a read
%% error occurred, where <code>Reason</code> is an atom corresponding to
%% a Posix error code; see the module <code>file</code> for details.</p>
%%
%% @see file

file(Name) ->
    Name1 = filename(Name),
    case catch {ok, file:read_file(Name1)} of
	{ok, V} ->
	    case V of
		{ok, B} ->
		    string(binary_to_list(B));
		{error, E} ->
		    error_read_file(Name1),
		    exit({read, E})
	    end;
	{'EXIT', E} ->
	    error_read_file(Name1),
	    exit(E);
	R ->
	    error_read_file(Name1),
	    throw(R)
    end.


%% =====================================================================
%% string(string()) -> [Comment]
%%
%%	    Comment = {Line, Column, Indentation, Text}
%%	    Line = integer()
%%          Column = integer()
%%          Indentation = integer()
%%          Text = [string()]
%%
%% @doc Extracts comments from a string containing Erlang source code.
%% Except for reading directly from a string, the behaviour is the same
%% as for <code>file/1</code>.
%%
%% @see file/1

string(Text) ->
    lists:reverse(join_lines(scan_lines(Text))).


%% =====================================================================
%% @spec scan_lines(string()) -> [CommentLine]
%%
%%	    CommentLine = {{{Line,Offset},Length} Column, Indent, Text}
%%	    Line = integer()
%%		Offset = integer()
%%		Length
%%	    Column = integer()
%%	    Indent = integer()
%%	    Text = string()
%%
%% @doc Extracts individual comment lines from a source code string.
%% Returns a list of comment lines found in the text, listed in order of
%% <em>decreasing</em> line-numbers, i.e., the last comment line in the
%% input is first in the resulting list. <code>Text</code> is a single
%% string, containing all characters following (but not including) the
%% first comment-introducing "<code>%</code>" character on the line, up
%% to (but not including) the line-terminating newline. For details on
%% <code>Line</code>, <code>Column</code> and <code>Indent</code>, see
%% <code>file/1</code>.
%%
%% @see file/1

scan_lines(Text) ->
    scan_lines(Text, {1,0}, 0, 0, []).

scan_lines([$\040 | Cs], {L,O}, Col, M, Ack) ->
    scan_lines(Cs, {L,O+1},Col + 1, M, Ack);
scan_lines([$\t | Cs], {L,O}, Col, M, Ack) ->
    scan_lines(Cs, {L,O+1}, tab(Col), M, Ack);
scan_lines([$\n | Cs], {L,O}, _Col, _M, Ack) ->
    scan_lines(Cs, {L + 1,O+1}, 0, 0, Ack);
scan_lines([$\r, $\n | Cs], {L,O}, _Col, _M, Ack) ->
    scan_lines(Cs, {L + 1,O+2}, 0, 0, Ack);
scan_lines([$\r | Cs], {L,O}, _Col, _M, Ack) ->
    scan_lines(Cs, {L + 1,O+1}, 0, 0, Ack);
scan_lines([$% | Cs], {L,O}, Col, M, Ack) ->
    scan_comment(Cs, "%", {{L,O}, 1}, L, Col, M, Ack);
scan_lines([$$ | Cs], {L,O}, Col, _M, Ack) ->
    scan_char(Cs, {L,O+1}, Col + 1, Ack);
scan_lines([$" | Cs], {L,O}, Col, _M, Ack) ->
    scan_string(Cs, $", {L,O+1}, Col + 1, Ack);
scan_lines([$' | Cs], {L,O}, Col, _M, Ack) ->
    scan_string(Cs, $', {L,O+1}, Col + 1, Ack);
scan_lines([_C | Cs], {L,O}, Col, _M, Ack) ->
    N = Col + 1,
    scan_lines(Cs, {L,O+1}, N, N, Ack);
scan_lines([], _L, _Col, _M, Ack) ->
    Ack.

tab(Col) ->
    Col - (Col rem 8) + 8.

scan_comment([$\n, $% | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    scan_comment(Cs, [$%, $\n | Cs1], {{L,O},Len+2}, L1+1, Col, M, Ack);
scan_comment([$\r, $\n, $% | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    scan_comment(Cs, [$%, $\r, $\n | Cs1], {{L,O},Len+3}, L1+1, Col, M, Ack);
scan_comment([$\r, $% | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    scan_comment(Cs, [$%, $\r | Cs1], {{L,O},Len+2}, L1+1, Col, M, Ack);
scan_comment([$\n | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    seen_comment(Cs, Cs1, {{L,O},Len+1}, L1, Col, M, Ack);
scan_comment([$\r, $\n | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    seen_comment(Cs, Cs1, {{L,O},Len+2}, L1, Col, M, Ack);
scan_comment([$\r | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    seen_comment(Cs, Cs1, {{L,O},Len+1}, L1, Col, M, Ack);
scan_comment([C | Cs], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    scan_comment(Cs, [C | Cs1], {{L,O},Len+1}, L1, Col, M, Ack);
scan_comment([], Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    seen_comment([], Cs1, {{L,O},Len}, L1, Col, M, Ack).

%% Add a comment line to the ackumulator and return to normal
%% scanning. Note that we compute column positions starting at 0
%% internally, but the column values in the comment descriptors
%% should start at 1.

seen_comment(Cs, Cs1, {{L,O},Len}, L1, Col, M, Ack) ->
    %% Compute indentation and strip trailing spaces
    N = Col - M,
    Text = lists:reverse(string:strip(Cs1, left)),
    Ack1 = [{{{L,O},Len}, Col + 1, N, Text} | Ack],
    scan_lines(Cs, {L1 + 1, O + Len}, 0, 0, Ack1).

scan_string([Quote | Cs], Quote, {L,O}, Col, Ack) ->
    N = Col + 1,
    scan_lines(Cs, {L,O+1}, N, N, Ack);
scan_string([$\t | Cs], Quote, {L,O}, Col, Ack) ->
    scan_string(Cs, Quote, {L,O+1}, tab(Col), Ack);
scan_string([$\n | Cs], Quote, {L,O}, _Col, Ack) ->
    %% Newlines should really not occur in strings/atoms, but we
    %% want to be well behaved even if the input is not.
    scan_string(Cs, Quote, {L+1,O+1}, 0, Ack);
scan_string([$\r, $\n | Cs], Quote, {L,O}, _Col, Ack) ->
    scan_string(Cs, Quote, {L + 1,O + 2}, 0, Ack);
scan_string([$\r | Cs], Quote, {L,O}, _Col, Ack) ->
    scan_string(Cs, Quote, {L + 1,O+1}, 0, Ack);
scan_string([$\\, _C | Cs], Quote, {L,O}, Col, Ack) ->
    scan_string(Cs, Quote, {L,O+2}, Col + 2, Ack);  % ignore character C
scan_string([_C | Cs], Quote, {L,O}, Col, Ack) ->
    scan_string(Cs, Quote, {L,O+1}, Col + 1, Ack);
scan_string([], _Quote, _L, _Col, Ack) ->
    %% Finish quietly.
    Ack.

scan_char([$\t | Cs], {L,O}, Col, Ack) ->
    N = tab(Col),
    scan_lines(Cs, {L,O+1}, N, N, Ack);    % this is not just any whitespace
scan_char([$\n | Cs], {L,O}, _Col, Ack) ->
    scan_lines(Cs, {L+1,O+1}, 0, 0, Ack);    % handle this, just in case
scan_char([$\r, $\n | Cs], {L,O}, _Col, Ack) ->
    scan_lines(Cs, {L+1,O+2}, 0, 0, Ack);
scan_char([$\r | Cs], {L,O}, _Col, Ack) ->
    scan_lines(Cs, {L+1,O+1}, 0, 0, Ack);
scan_char([$\\, _C | Cs], {L,O}, Col, Ack) ->
    N = Col + 2,    % character C must be ignored
    scan_lines(Cs, {L,O+2}, N, N, Ack);
scan_char([_C | Cs], {L,O}, Col, Ack) ->
    N = Col + 1,    % character C must be ignored
    scan_lines(Cs, {L,O+1}, N, N, Ack);
scan_char([], _L, _Col, Ack) ->
    %% Finish quietly.
    Ack.


%% =====================================================================
%% @spec join_lines([CommentLine]) -> [Comment]
%%
%%	    CommentLine = {Line, Column, Indent, string()}
%%	    Line = integer()
%%	    Column = integer()
%%	    Indent = integer()
%%	    Comment = {Line, Column, Indent, Text}
%%	    Text = [string()]
%%
%% @doc Joins individual comment lines into multi-line comments. The
%% input is a list of entries representing individual comment lines,
%% <em>in order of decreasing line-numbers</em>; see
%% <code>scan_lines/1</code> for details. The result is a list of
%% entries representing <em>multi-line</em> comments, <em>still listed
%% in order of decreasing line-numbers</em>, but where for each entry,
%% <code>Text</code> is a list of consecutive comment lines in order of
%% <em>increasing</em> line-numbers (i.e., top-down).
%%
%% @see scan_lines/1

join_lines([{L, Col, Ind, Txt} | Lines]) ->
    join_lines(Lines, [Txt], L, Col, Ind);
join_lines([]) ->
    [].

%% In the following, we assume that the current `Txt' is never empty.
%% Recall that the list is in reverse line-number order.

join_lines([{L1, Col1, Ind1, Txt1} | Lines], Txt, L, Col, Ind) ->
    if L1 == L - 1, Col1 == Col, Ind + 1 == Col ->
	    %% The last test above checks that the previous
	    %% comment was alone on its line; otherwise it won't
	    %% be joined with the current; this is not always what
	    %% one wants, but works well in general.
	    join_lines(Lines, [Txt1 | Txt], L1, Col1, Ind1);
       true ->
	    %% Finish the current comment and let the new line
	    %% start the next one.
	    [{L, Col, Ind, Txt}
	     | join_lines(Lines, [Txt1], L1, Col1, Ind1)]
    end;
join_lines([], Txt, L, Col, Ind) ->
    [{L, Col, Ind, Txt}].


%% =====================================================================
%% Utility functions for internal use

filename([C | T]) when is_integer(C), C > 0, C =< 255 ->
    [C | filename(T)];
filename([H|T]) ->
    filename(H) ++ filename(T);
filename([]) ->
    [];
filename(N) when is_atom(N) ->
    atom_to_list(N);
filename(N) ->
    report_error("bad filename: `~P'.", [N, 25]),
    exit(error).

error_read_file(Name) ->
    report_error("error reading file `~s'.", [Name]).

report_error(S, Vs) ->
    error_logger:error_msg(lists:concat([?MODULE, ": ", S, "\n"]),
			   Vs).


%% =====================================================================
