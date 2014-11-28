-module(server).
-export([start/1]).

%For Testing uncomment this:
%-export([start/1, loop/2, join/2, joinBoard/2, isOccupied/3]).

-record(player, {name, pid, space}).
-record(tile, {space, name, type}).

%starts the server and registers it under the name provided
start(SName) ->
  random:seed(now()),
  register(SName, self()),
  UninitializedBoard = maps:new(),
  BoundsTile = #tile{space={10,10}, name=bounds, type=bounds},
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
      {NewPlayer, NewPlayers, NewBoard} = join(Sender, Players, Board),
      %NewBoard = joinBoard(NewPlayer, Board),
      Sender ! {joined, NewPlayer#player.name, NewBoard},
      loop(NewPlayers, NewBoard);
    {lookup, PlayerName, Sender} ->
      Player = getPlayer(PlayerName, Players),
      Sender ! {found, Player},
      loop(Players, Board);
    {refresh, Sender} ->
      Sender ! {latest, Board},
      loop(Players,Board);
    {action, Action, PlayerName, Sender} ->
      {Status, NewPlayers, NewBoard} = takeAction(Action, PlayerName,
                                                  Players, Board),
      Sender ! {Status, NewBoard},
      loop(NewPlayers, NewBoard);
    {stop, Sender} ->
      Sender ! {stopping, self()},
      ok
  end.

%returns Player of name 'PlayerName'
getPlayer(PlayerName, Players) ->
  maps:get(PlayerName, Players).

%moves the player to new location 'Loc'
move(Player, Loc, Players, Board) ->
  PlayerName = Player#player.name,
  OldLoc = Player#player.space,
  NewPlayer = Player#player{space = Loc},
  NewPlayers = maps:update(PlayerName, NewPlayer, Players),
  Tile = maps:get(OldLoc, Board),
  NewTile = Tile#tile{space=Loc},
  TempBoard = maps:remove(OldLoc,Board),
  NewBoard = maps:put(Loc, NewTile, TempBoard),
  {NewPlayers, NewBoard}. 
  

%handles action according to type (move, attack, etc)
takeAction({move, Dir}, PlayerName, Players, Board)->
  Player = getPlayer(PlayerName, Players),
  {X,Y} = Player#player.space,
  case Dir of
    north -> Loc = {X,Y+1};
    east -> Loc = {X+1,Y};
    south -> Loc = {X,Y-1};
    west -> Loc = {X-1,Y}
  end,
  InBounds = isInBounds(Loc,Board),
  Occupied = isOccupied(Loc,Board),
  if
    InBounds and (not Occupied) ->
      {NewPlayers, NewBoard} = move(Player, Loc, Players, Board),
      {acted, NewPlayers, NewBoard};
    true ->
      {failed, Players, Board}
  end.

%enters a new player into the system (Players and Board),
%assigns an identifier for the player,
%returns Player record, updated Players and Board
join(NewPlayerPid, Players, Board) ->
   PlayerName = maps:size(Players),
   Player = #player{name=PlayerName, pid=NewPlayerPid, 
                      space={-1,-1}},
   {NewPlayer,NewBoard} = joinBoard(Player, Board),
   NewPlayers = maps:put(PlayerName, NewPlayer, Players),
   {NewPlayer, NewPlayers, NewBoard}.

%returns true if 'Loc' is in bounds
isInBounds(Loc, Board) ->
  BoundsTile = maps:get(bounds,Board),
  {Xbound,Ybound} = BoundsTile#tile.space,
  {X,Y} = Loc,
  if
    ((X =< Xbound) and (X >= 0)) and ((Y =<Ybound) and (Y >= 0)) ->
      true;
    true -> false
  end.

%returns false if 'Loc' is unoccupied
isOccupied(Loc, Board) -> 
   maps:is_key(Loc, Board).

%places Object 'Name' of 'Type' at 'Loc' on 'Board' if 'Loc' is an
%unoccupied space; returns failed otherwise
place(Name, Type, Loc, Board) ->
  case isOccupied(Loc, Board) of
    false->
      NewTile = #tile{space=Loc, name=Name, type=Type},
      NewBoard = maps:put(Loc, NewTile, Board),
      {ok, NewBoard};
    true->
      {failed, Board}
  end.

%places a player in a random location on the board
joinBoard(Player, Board) ->
   PlayerName = Player#player.name,
   BoundsTile = maps:get(bounds, Board),
   {Xbound, Ybound} = BoundsTile#tile.space,
   X = random:uniform(Xbound),
   Y = random:uniform(Ybound),
   {Status, NewBoard} = place(PlayerName, player, {X,Y}, Board),
   case Status of
      ok ->
        NewPlayer = Player#player{space={X,Y}}, 
	      {NewPlayer, NewBoard};
      failed -> joinBoard(Player, Board)
   end.
