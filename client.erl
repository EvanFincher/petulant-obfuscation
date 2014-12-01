-module(client).
-export([start/2,stop/2,restart/2,join/2,populate/4,lookup/3,ping/2,refresh/2,act/4]).


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

%restart specified server
restart(SName,Node) ->
  stop(SName,Node),
  timer:sleep(100),
  start(SName,Node).

%join the specified server
%returns signature (PlayerName) needed for act function, lookup
join(SName,Node) ->
  {SName,Node} ! {join, self()},
  receive
    {joined, PlayerName, Board} -> {PlayerName, Board}
  end.

%signals the server to add stuff to the board
%e.g. (game,Node,preset,one)
populate(SName, Node, Type, Data) ->
  {SName, Node} ! {populate, Type, Data, self()},
  receive
    {populated, Board} -> Board
  end.

%fetch information for player 'PlayerName'
lookup(SName, Node, PlayerName) ->
  {SName, Node} ! {lookup, PlayerName, self()},
  receive
    {found, Player} -> Player
  end.

%ping the server and wait for a reply; timeout after 5000
%this wasn't working for me earlier
ping(SName,Node) ->
  {SName,Node} ! {ping, self()},
  receive
    {ok, SPid} -> {ok, SPid}
  after 5000 ->
    {error, timeout}
  end.


%asks the server for the current game board;
%should be called regularly (maybe every .5 seconds) by client
%in order to stay current
refresh(SName, Node) -> 
  {SName, Node} ! {refresh, self()},
  receive
    {latest, Board} -> Board
  end. 

%send an action to the server for processing
%e.g. {move, north}, or {attack, east}
%PlayerName was returned by join function 
act(SName, Node, Action, PlayerName) ->
  {SName, Node} ! {action, Action, PlayerName, self()},
  receive
    {acted, Board} -> {ok, Board};
    {failed, Board} -> {failed, Board}
  end.

%test(SName, Node) ->
%  start(SName, Node),
%  {PlayerName, _Board} = join(SName, Node),
%  Board1 = refresh(SName, Node),
%  {ok, Board2} = act(SName, Node, {move, north}, PlayerName),
%  L1 = [{refresh, ok}, {act, ok}, Board1].
  
