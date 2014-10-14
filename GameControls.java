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
	private void defaultControls(){
		 JLabel lab = new JLabel("Label ");
		 this.add(lab);
		// lab = new JLabel("Simulation Speed");
		// MyScrollBar bar = new MyScrollBar(lab, map);
		// JPanel pan = new JPanel();
		// pan.setLayout (new BoxLayout(pan, BoxLayout.Y_AXIS));
		// pan.setBorder (new LineBorder(Color.ORANGE, 1));
		// pan.add(lab);
		// pan.add(bar);
		// this.add(pan);
		// ErlConnection erl = new ErlConnection("client", "cookie");
		// MyButton butt = new MyButton("Disconnect", erl);
		// this.add(butt);
		// bar = new MyScrollBar("Timer", map);
		// butt = new MyButton("Timer", bar);
		// JPanel pan2 = new JPanel();
		// pan2.setLayout (new BoxLayout(pan2, BoxLayout.X_AXIS));
		// pan2.add(butt);
		// butt = new MyButton("Halt", bar);
		// pan2.add(butt);
		// pan = new JPanel();
		// pan.setLayout (new BoxLayout(pan, BoxLayout.Y_AXIS));
		// pan.setBorder (new LineBorder(Color.ORANGE, 1));
		// pan.add(pan2);
		// pan.add(bar);
		// this.add(pan);
		// butt = new MyButton("Remove All", map, true);
		// this.add(butt);
	}

	private void masterControls(){
		connButt = new MyButton("Disconnect", this);
		this.add(connButt);
		JLabel lab = new JLabel("| Game:");
		this.add(lab);
		sName = new MyTextField("game");
		sNode = new MyTextField("server@localhost");
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
	}

	public void gameConnect(){
		game.connect();
	}
	public void gameDisconnect(){
		game.disconnect();
	}
	public void gameNew(){
		String gameAccessToken = sName.getText();
		String gameHost = sNode.getText();
		gameNew(gameAccessToken, gameHost);
	}
	public void gameNew(String gameAccessToken, String gameHost){
		game.startServer(gameAccessToken, gameHost);
	}
	public void gameJoin(){
		String gameAccessToken = sName.getText();
		String gameHost = sNode.getText();
		gameJoin(gameAccessToken, gameHost);
	}
	public void gameJoin(String gameAccessToken, String gameHost){
		game.joinGame(gameAccessToken, gameHost);
	}
	public void gameRestart(){
		game.restart();
	}
	public void gameEnd(){
		game.stop();
	}
	public void gamePing(){
		game.ping();
	}
	public void gameBoardPopulate(){
		game.populate();
	}
	public void gameBoardRefresh(){
		game.refresh();
	}
}
