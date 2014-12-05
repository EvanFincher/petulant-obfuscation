ReadMe

Our Game

Evan Fincher and Dixon Minnick
______________________________
Files:

client.erl: Module serving as an intermediary between the various
possible clients (GUI, AI) and the server; contains various functions
that handle sending and receiving messages for triggering all the 
game functionality currently supported by the server.

server.erl: Module containing all of the actual game state, handled
the main server loop, which also processes incoming messages from the
client module functions, with the help of a number of un-exported 
functions.

compile, run, run-only:  These are bash scripts used to compile and
run the GUI, which is implemented in Java. run both compiles and runs
the GUI.

Everything Else: components of the Java gui, explained in more detail
within each file


Intructions:
First ensure all erlang files are compiled and accessible on all the
machines involved.  Particularly, the server module on the server
and everything else on the client.


To Play:

-via Graphical User Interface (GUI)
	1) Edit the RunTimeVars.java file to support your host configuration
		a) If you are running multiple GUI client nodes on the same Host, you will also need to edit the run-only bash script
			and change the sname to something else. Make sure these changes persist in your RunTimeVars.java
	2) Make sure you have a server Node in mind to connect to
		a) If you do not, open a shell in the game directory and run:
			erl -sname server -setcookie cookie
			>c(client).
			>c(server).
		  optional:
			>server:start(game).
		b) If a server is already started make note of the Node name and the game token
	3) In a seperate terminal in the same directory run:
		  bash run
		a) If you wish to compile and run seperately use:
		  bash compile
		  bash run-only
	4) Enter game server details and  join the game
		a) If a server node is running, but a game has not started, click 'New Game', then proceed to b)
		b) click 'Join'
		c) If you feel like pre-populating the gameBoard, click 'populate'
	5) Play the game using the buttons in the bottom right corner


-via Command Line
For interacting directly with back-end (as in the in-class demo):
Most of the testing was done using nodes with short names, as trying
to start a node with -name blows up Erlang everywhere I've tried it
except on homework; this seems to be a network configuration problem.
Of the systems I tried (including the RedHat 6 machines), only homework
returns a fully qualified domain name in reponse to 'hostname -f'; 
all the places it didn't work return just the machine name. However,
some -name testing was done with a client and server on separate nodes
on homework, and everything worked (except the GUI, mind). Also note
that any number of client/server nodes can run on the same machine;
multiple machines are more interesting, but not required.

server:
Start an erlang node on the server machine; for example:
erl -sname server -setcookie cookie

client:
Start an erlang node on each client machine; for example:
erl -sname evan -setcookie cookie

Only one client should do the following:
{_,Node} = client:start(game,’server@ubuntu’).   
client:populate(game,Node,preset,one). 

Every client can then join the game:              
{PName,_} = client:join(game,Node).

and proceed to act like in the following examples:
client:act(game,Node,{move,north},PName).
client:act(game,Node,{attack,east},PName).

Finally, one client should stop the game.
client:stop(game,Node).

also useful:
To simply request a new game board:
client:refresh(game,Node).

other utilities:
client:restart(game,Node).
client:ping(game,Node).


________________________________

Note: you can connect as many client nodes as you want to a game Server
