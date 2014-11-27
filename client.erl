-module(client).
-export([start/2,stop/2,join/2,ping/2,refresh/2,act/3,test/2]).


%spawns a new server registered under SName; pings to verify success
%returns Pid, Node name of server
start(SName,Node) ->
  Pid = spawn(Node,server,start,[SName]),
  %{ok, Pid} = ping(SName,Node),
  {Pid,Node}.

%sends a halt message to the specified server; waits for verification
stop(SName,Node) ->
  {SName,Node} ! {stop, self()},
  receive
    {stopping, Pid} -> {stopping, Pid}
  end.

%synchronous; join the specified server
join(SName,Node) ->
  {SName,Node} ! {join, self()},
  receive
    {joined, Players} -> Players
  end.

%ping the server and wait for a reply; timeout after 5000
ping(SName,Node) ->
  {SName,Node} ! {ping, self()},
  receive
    {ok, SPid} -> {ok, SPid}
  after 5000 ->
    {error, timeout}
  end.


%asks the server for the current game board
refresh(SName, Node) -> 
  {SName, Node} ! {refresh, self()},
  receive
    {latest, Board} -> Board
  end. 

%send an action to the server for processing 
act(SName, Node, Action) ->
  {SName, Node} ! {action, Action, self()},
  receive
    {acted, Board} -> {ok, Board};
    {invalid, Board} -> {invalid, Board}
  end.

%maybe break out a separate move fun to simplify server logic

test(SName, Node) ->
  start(SName, Node),
  join(SName, Node),
  Board1 = refresh(SName, Node),
  {ok, Board2} = act(SName, Node, {move, left}),
  L1 = [{refresh, ok}, {act, ok}, Board1].
  
