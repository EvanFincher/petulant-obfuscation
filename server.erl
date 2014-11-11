-module(server).
-export([start/1,test/1]).

%starts the server and registers it under the name provided

start(SName) ->
  register(SName, self()),
  loop([]).


loop(PlayerList) ->
  receive
    {ping, Sender} ->
      Sender ! {ok, self()},
      loop(PlayerList);
    {stop, Sender} ->
      Sender ! {stopping, self()},
      ok
  end.


test(Server) ->
  ok.
