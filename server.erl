-module(server).
-export([start/1]).

-record(player, {name, pid}).
-record(tile, {space, playerName}).

%starts the server and registers it under the name provided

start(SName) ->
  register(SName, self()),
  loop(maps:new(), maps:new()).


loop(Players, Board) ->
  receive
    {join, Sender} ->
      NewPlayers = join(Sender, Players),
      Sender ! {joined, NewPlayers},
      loop(NewPlayers, Board);
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


