-module(javaConnect).
-export([start/2,runNode/0]).

-import(client, [start/2,stop/2,restart/2,join/2,lookup/3,ping/2,refresh/2,act/4,test/2]).
-import(server, [start/1]).


start(SName, Node) ->
  {Pid, ClientNode} = client:start(SName, Node),
  javaConnect:loop(SName, Node).

loop(SName, Node) ->
  receive
    {ping, Sender} ->
      {Msg, Pid} = client:ping(SName, Node),
      Sender ! {Msg, Pid},
      loop(SName, Node);
    {join, Sender} ->
      {PlayerName, Board} = client:join(SName, Node),
      loop(SName, Node);
    {lookup, Sender, PlayerName} ->
      Player = client:lookup(SName, Node, PlayerName),
      loop(SName, Node);
    {refresh, Sender} ->
      Board = client:refresh(SName, Node),
      loop(SName, Node);
    {action, Sender, PlayerName, Action} ->
      {Msg, Board} = client:action(SName, Node, Action, PlayerName),
      loop(SName, Node);
    {stop, Sender} ->
      {Msg, Pid} = client:stop(SName, Node),
      init:stop()
  end.

runNode() ->
  self().
