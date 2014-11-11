-module(server).
-export([start/1]).

-record(player, {name, pid}).
-record(tile, {space, playerName}).

%starts the server and registers it under the name provided

start(SName) ->
  random:seed(now()),
  register(SName, self()),
  UninitializedBoard = maps:new(),
  Players = maps: new(),
  Board = maps:put(bounds, {10,10}, UninitializedBoard),
  loop(Players, Board).


loop(Players, Board) ->
  receive
    {join, Sender} ->
      NewPlayers = join(Sender, Players),
      NewBoard = joinBoard(Sender, Board),
      Sender ! {joined, {NewPlayers, NewBoard}},
      loop(NewPlayers, NewBoard);
    {ping, Sender} ->
      Sender ! {ok, self()},
      loop(Players, Board);
    {stop, Sender} ->
      Sender ! {stopping, self()},
      ok
  end.

join(NewPlayerPid, Players) ->
   PlayerName = maps:size(Players),
   NewPlayer = #player{name=PlayerName, pid=NewPlayerPid},
   maps:put(PlayerName, NewPlayer, Players).

isOccupied(X, Y, Board) -> 
   map:is_key({X, Y}, Board).

joinBoard(PlayerName, Board) ->
   {Xbound, Ybound} = maps:get(bounds, Board),
   X = random:uniform(Xbound),
   Y = random:uniform(Ybound),
   case isOccupied(X, Y, Board) of
      false ->
         NewTile = #tile{space={X,Y}, playerName=PlayerName}, 
         map:put({X,Y}, NewTile, Board);
      true ->
         joinBoard(PlayerName, Board)
   end.