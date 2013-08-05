%% Author: lu.jingbo@rytong.com
%% Created: 2013-8-5
%% Description: TODO: Add description to erlide_util
-module(erlide_util).

%%
%% Include files
%%

%%
%% Exported Functions
%%
-export([consult/1]).

%%
%% API Functions
%%

consult(FileName) ->
    case file:consult(FileName) of
        {error, {Line, Term, Err}} when is_list(Err) ->
                {error, {Line, Term, lists:flatten(Err)}};
        Other -> Other
    end.


%%
%% Local Functions
%%

