-module(client).
-export([start/2,stop/2,join/2,ping/2]).

start(SName,Node) ->
  Pid = spawn(Node,server,start,[SName]),
  {ok, Pid} = ping(SName,Node),
  {Pid,Node}.

stop(SName,Node) ->
  {SName,Node} ! {stop, self()},
  receive
    {stopping, Pid} -> {stopping, Pid}
  end.

join(SName,Node) ->
  {SName,Node} ! {join, self()},
  receive
    {joined, Players} -> Players
  end.

ping(SName,Node) ->
  {SName,Node} ! {ping, self()},
  receive
    {ok, SPid} -> {ok, SPid}
  after 5000 ->
    {error, timeout}
  end.
  

%
%client functions:
%synchronous request_update
%send_action