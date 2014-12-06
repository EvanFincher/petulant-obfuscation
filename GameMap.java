//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
GameMap class

Contains and manages a representation of the game map,
including all drawing methods

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;


public class GameMap extends JComponent implements MouseListener {
    private ArrayList<Player> players;
    private int num_players;
    private PlayerControls controls;
    private GameControls controller;
    private int lattitude = 0;
    private int longitude = 0;
    private Random generator = new Random();
    private Player myPlayer = new Player("0", 6, 6, "myPlayer");
    private ImageIcon myPlayerIcon = new ImageIcon("m-scaled.gif");
    private ImageIcon playerIcon = new ImageIcon("nr-scaled.gif");
    private ImageIcon obstacleIcon = new ImageIcon("sis-scaled.gif");
    private static int scalar = 65;
    private static int xBound = 10;
    private static int yBound = 10;

    public GameMap () {
		players = new ArrayList<Player>();
		num_players = 0;
		addMouseListener (this);
		addKeyListener (new MyKeyListener());
		this.setBorder (new LineBorder(Color.BLUE, 2));
		
    }
    
    public void startGame(){
    	this.repaint();
	}

    public void paintComponent (Graphics g) {
    	drawGrid(g);
		for(int i=0; i<num_players; i++){
			paintPlayer(i, g);
		}
    }
    
    private void paintPlayer (int i, Graphics g){
    	Player s = (Player)players.get(i);
    	if(s.name == myPlayer.name){
    		s.type = myPlayer.type;
    		myPlayer = s;
    	}
    	int x = s.x*scalar;
    	int y = orientationFix(s.y)*scalar;
    	getIcon(s.type).paintIcon(this, g, x + 2, y + 2);
    }

    private void drawGrid(Graphics g){
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setColor(Color.blue);

    	// int w = this.getWidth();
    	// int h = this.getHeight();
    	int w = (xBound + 2)*scalar;
    	int h = (yBound + 2)*scalar;
    	for(int i=0; i<(w/scalar); i++){
    		g2d.drawLine(i*scalar, 0, i*scalar, h);
    	}
    	for(int i=0; i<(h/scalar); i++){
    		g2d.drawLine(0, i*scalar, w, i*scalar);
    	}
    }

    private ImageIcon getIcon(String type){
    	System.out.println(type);
    	if(type == "player"){
    		return playerIcon;
    	}
    	else if(type == "myPlayer"){
    		return myPlayerIcon;
    	}
    	else{
    		return obstacleIcon;
    	}
    }

    public void paintTest(){
    	Player s = randomPlayer("myPlayer");
    	setMyPlayer(s);
    	addPlayer(s);
    	addPlayer(randomPlayer());
    	addPlayer(randomPlayer());
    	addPlayer(randomObject());
    	addPlayer(randomObject());
    }

    private int orientationFix(int y){
    	return yBound - y;
    }

    public void pushPlayers(ArrayList<Player> _players){
    	players = _players;
    	num_players = players.size();
    	this.repaint();
    }

    public Player setMyPlayer(Player player){
    	myPlayer = player;
    	myPlayer.type = "myPlayer";
    	return myPlayer;
    }
    public Player setMyPlayer(String playerName){
    	myPlayer = new Player(playerName, 0, 0, "myPlayer");
    	return myPlayer;
    }

    public void addPlayer(Player player){
    	players.add(player);
    	num_players = players.size();
    	this.repaint();
    }

    public Player randomObject(){
    	return randomPlayer("object");
    }
    public Player randomPlayer(){
    	return randomPlayer("player");
    }
    public Player randomPlayer(String t){
    	System.out.println(this.getHeight());
    	System.out.println(this.getWidth());
    	int y = this.generator.nextInt(this.getHeight())/scalar;
		int x = this.generator.nextInt(this.getWidth())/scalar;
		String n = "rand" + String.valueOf(this.generator.nextInt(this.getWidth() * this.getHeight()));
		Player p = new Player(n, x, y, t);
		return p;
    }

	public void setControls(PlayerControls c){
		this.controls = c;
	}
	public void setController(GameControls c){
		this.controller = c;
	}
	public int getGeoLong(){return longitude;}
	public int getGeoLat(){return lattitude;}
	public void setGeo(int x, int y){
		if(controls != null){
			controls.setGeo(x, y);
		}
	}
	

    /** MouseListener callbacks */
    public void mousePressed (MouseEvent event) {	
		System.out.println ("Mouse down at " + event.getPoint().x + ", " + event.getPoint().y);
		int x = event.getPoint().x;
		int y = event.getPoint().y;
		if(x < this.getWidth() && y < this.getHeight()){
			longitude = x;
			lattitude = y;
		}
		// for(int i=0; i<num_ships; i++){
		// 	Ship s = ships.get(i);
		// 	if(s.isClicked(x, y)){
		// 		controls.setBoat(s);
		// 		return;
		// 	}
		// }
		controls.setGeo(longitude, lattitude);
    }
    public void mouseClicked (MouseEvent event) {}
    public void mouseReleased (MouseEvent event) {}
    public void mouseEntered (MouseEvent event) {}
    public void mouseExited (MouseEvent event) {}

 	public class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));
		}
	}
}
