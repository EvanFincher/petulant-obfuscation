-module(server).
-export([start/1]).

%For Testing uncomment this:
%-export([start/1, loop/2, join/2, joinBoard/2, isOccupied/3]).

-record(player, {name, pid}).
-record(tile, {space, playerName}).

%starts the server and registers it under the name provided

start(SName) ->
  random:seed(now()),
  register(SName, self()),
  UninitializedBoard = maps:new(),
  BoundsTile = #tile{space={10,10}, playerName=bounds},
  Board = maps:put(bounds, BoundsTile, UninitializedBoard),
  Players = maps:new(),
  loop(Players, Board).

%main server loop; processes incoming messages
loop(Players, Board) ->
  receive
    {ping, Sender} ->
      Sender ! {ok, self()},
      loop(Players, Board);
    {join, Sender} ->
      {NewPlayer, NewPlayers} = join(Sender, Players),
      NewBoard = joinBoard(NewPlayer, Board),
      Sender ! {joined, {NewPlayers, NewBoard}},
      loop(NewPlayers, NewBoard);
    {refresh, Sender} ->
      Sender ! {latest, Board},
      loop(Players,Board);
    {action, Action, Sender} ->
      {Status, NewBoard} = takeAction(Action, Board),
      Sender ! {Status, NewBoard},
      loop(Players, NewBoard);
    {stop, Sender} ->
      Sender ! {stopping, self()},
      ok
  end.

%handles action according to type (move, attack, w/e)
takeAction({Type, Data}, Board)->
  {acted, Board}.


join(NewPlayerPid, Players) ->
   PlayerName = maps:size(Players),
   NewPlayer = #player{name=PlayerName, pid=NewPlayerPid},
   NewPlayers = maps:put(PlayerName, NewPlayer, Players),
   {NewPlayer, NewPlayers}.

isOccupied(X, Y, Board) -> 
   maps:is_key({X, Y}, Board).

joinBoard(Player, Board) ->
   PlayerName = Player#player.name,
   BoundsTile = maps:get(bounds, Board),
   {Xbound, Ybound} = BoundsTile#tile.space,
   X = random:uniform(Xbound),
   Y = random:uniform(Ybound),
   case isOccupied(X, Y, Board) of
      false ->
         NewTile = #tile{space={X,Y}, playerName=PlayerName}, 
         maps:put({X,Y}, NewTile, Board);
      true ->
         joinBoard(PlayerName, Board)
   end.
