ReadMe

Our Game

Evan Fincher and Dixon Minnick
______________________________

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
	1) //Left for Evan

________________________________

Note: you can connect as many client nodes as you want to a game Server
