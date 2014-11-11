-module(client).
-export([start/2,stop/2,join/1,ping/2]).

start(SName,Node) ->
  Pid = spawn(Node,server,start,[SName]),
  {ok, Pid} = ping(SName,Node),
  {Pid,Node}.

stop(SName,Node) ->
  {SName,Node} ! {stop, self()},
  receive
    {stopping, Pid} -> {stopping, Pid}
  end.

join(Server) ->
  ok,
  ok.

ping(SName,Node) ->
  {SName,Node} ! {ping, self()},
  receive
    {ok, SPid} -> {ok, SPid}
  after 5000 ->
    {error, timeout}
  end.