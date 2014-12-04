-module(javaClient).
-export([start/2,stop/2,restart/2,join/2,populate/4,lookup/3,ping/2,refresh/2,act/4]).

-define(record_to_tuplelist(Rec, Ref), lists:zip(record_info(fields, Rec),tl(tuple_to_list(Ref)))).

call(Fun, SName, Node, Args) ->
  Response = client:ping(SName, Node),
  case Response of
    {error, timeout} -> {error, timeout};
    {ok, Pid} -> 
      case Fun of
        clientStop -> client:stop(SName, Node);
        clientRestart -> client:restart(SName, Node);
        clientJoin -> 
          ClientResponse = client:join(SName, Node),
          parseJoinResponse(ClientResponse);
        clientPopulate ->
          case Args of
            [Type, Data] -> 
              ClientResponse = client:populate(SName, Node, Type, Data),
              parsePopulateResponse(ClientResponse);
            _ ->
              {error, argumentError}
          end;
        clientLookup ->
          case Args of
            [PlayerName] ->
              ClientResponse = client:lookup(SName, Node, PlayerName),
              parseLookupResponse(ClientResponse);
            _ ->
              {error, argumentError}
          end;
        clientRefresh -> {ok, maps:to_list(client:refresh(SName, Node))};
        clientAct ->
          case Args of
            [Action, PlayerName] ->
              ClientResponse = client:act(SName, Node, Action, PlayerName),
              parseActResponse(ClientResponse);
            _ ->
              {error, argumentError}
          end
      end
  end.

%spawns a new server registered under SName; pings to verify success
%returns Pid, Node name of server
start(SName,Node) ->
  client:start(SName,Node),
  %client:ping(SName,Node).
  {ok, starting}.

%sends a halt message to the specified server; waits for verification
stop(SName,Node) ->
  call(clientStop, SName, Node, []).

%restart specified server
restart(SName,Node) ->
  call(clientRestart, SName, Node, []).

%join the specified server
%returns signature (PlayerName) needed for act function, lookup
join(SName,Node) ->
  call(clientJoin, SName, Node, []).
%{PlayerName, Board}

%signals the server to add stuff to the board
%e.g. (game,Node,preset,one)
populate(SName, Node, Type, Data) ->
  call(clientPopulate, SName, Node, [Type, Data]).
  %Board

%fetch information for player 'PlayerName'
lookup(SName, Node, PlayerName) ->
  call(clientLookup, SName, Node, [PlayerName]).
  %Player

%ping the server and wait for a reply; timeout after 5000
ping(SName,Node) ->
  client:ping(SName,Node).


%asks the server for the current game board;
%should be called regularly (maybe every .5 seconds) by client
%in order to stay current
refresh(SName, Node) -> 
  call(clientRefresh, SName, Node, []).
  %Board

%send an action to the server for processing
%e.g. {move, north}, or {attack, east}
%PlayerName was returned by join function 
act(SName, Node, Action, PlayerName) ->
  call(clientAct, SName, Node, [Action, PlayerName]).
    % {ok, Board};
    % {failed, Board}

parseActResponse(ClientResponse)->
  case ClientResponse of
    {Msg, Board} -> {Msg, maps:to_list(Board)}
  end.

parseJoinResponse(ClientResponse)->
  case ClientResponse of
    {PlayerName, Board} -> {PlayerName, maps:to_list(Board)}
  end.

parsePopulateResponse(Board)->
  {ok, maps:to_list(Board)}.

parseLookupResponse(Player)->
  Player.
