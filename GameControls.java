//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
GameControls Class

Contains, manages and renders all controls necessary for
Connecting to the game server and interacting with the
game itself

*/

import java.awt.Container;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameControls extends JPanel {

	private GameMap map;
	private ErlConnection game;
	private MyTextField sName;
	private MyTextField sNode;

	private MyButton startButt;
	private MyButton freshButt;
	private MyButton connButt;
	private MyButton joinButt;
	private MyButton stopButt;
	private MyButton restButt;
	private MyButton pingButt;
	private MyButton popButt;

	private MyButton[] shownButts;
	
	public GameControls(GameMap m){
		super();
		this.map = m;
		this.setLayout (new BoxLayout(this, BoxLayout.X_AXIS));
		//defaultControls();
		this.setBorder (new LineBorder(Color.ORANGE, 1));
	}
	public GameControls(GameMap m, ErlConnection erl){
		super();
		this.game = erl;
		this.map = m;
		this.setLayout (new BoxLayout(this, BoxLayout.X_AXIS));
		masterControls();
		this.setBorder (new LineBorder(Color.ORANGE, 1));
	}

	private void masterControls(){
		connButt = new MyButton("Disconnect", this);
		this.add(connButt);
		JLabel lab = new JLabel("| Game:");
		this.add(lab);
		sName = new MyTextField("game");
		//sNode = new MyTextField("server@localhost");
		sNode = new MyTextField("server@lab118g");
		this.add(sName);
		lab = new JLabel("Host:");
		this.add(lab);
		this.add(sNode);
		joinButt = new MyButton("Join Game", this);
		this.add(joinButt);
		startButt = new MyButton("New Game", this);
		this.add(startButt);
		pingButt = new MyButton("Ping", this);
		this.add(pingButt);
		stopButt = new MyButton("End Game", this);
		this.add(stopButt);
		restButt = new MyButton("Restart Game", this);
		this.add(restButt);
		freshButt = new MyButton("Refresh", this);
		this.add(freshButt);
		popButt = new MyButton("Populate", this);
		this.add(popButt);

		shownButts = new MyButton[]{startButt, freshButt, connButt, joinButt, stopButt, restButt, pingButt, popButt};
		showButts(new Boolean[]{true, false, true, true, false, false, false, false});
	}

	public void gameConnect(){
		game.connect();
	}
	public void gameDisconnect(){
		game.disconnect();
	}
	public void gameNew(){
		showButts(new Boolean[]{false, true, false, true, true, true, true, true}); //2-step
		//showButts(new Boolean[]{false, true, false, false, true, true, true, true}); //1-step
		String gameAccessToken = sName.getText();
		String gameHost = sNode.getText();
		gameNew(gameAccessToken, gameHost);
	}
	public void gameNew(String gameAccessToken, String gameHost){
		game.startServer(gameAccessToken, gameHost);
	}
	public void gameJoin(){
		showButts(new Boolean[]{false, true, false, false, true, true, true, popButt.isShown()});
		String gameAccessToken = sName.getText();
		String gameHost = sNode.getText();
		gameJoin(gameAccessToken, gameHost);
	}
	public void gameJoin(String gameAccessToken, String gameHost){
		game.joinGame(gameAccessToken, gameHost);
	}
	public void gameRestart(){
		showButts(new Boolean[]{false, true, false, true, true, true, true, true}); //2-step
		//showButts(new Boolean[]{false, true, false, false, true, true, true, true}); //1-step
		game.restart();
	}
	public void gameEnd(){
		showButts(new Boolean[]{true, false, true, true, true, false, true, false});
		game.stop();
	}
	public void gamePing(){
		game.ping();
	}
	public void gameBoardPopulate(){
		showButts(new Boolean[]{startButt.isShown(), freshButt.isShown(), false, joinButt.isShown(), stopButt.isShown(), restButt.isShown(), pingButt.isShown(), false});
		game.populate();
	}
	public void gameBoardRefresh(){
		game.refresh();
	}

	private void showButts(Boolean[] show){
		for(int i=0; i<8; i++){
			MyButton butt = shownButts[i];
			if(butt == null){
				return;
			}
			if(show[i] != butt.isShown()){
				if(show[i]){
					this.add(butt);
				} else {
					this.remove(butt);
				}
				butt.show(show[i]);
			}
		}
	}

	
}
