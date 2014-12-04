//Dixon Minnick
//Dixon.Minnick@Tufts.edu

//Canvas class
//  used to draw different images on the canvas

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
    private Player myPlayer;
    private ImageIcon myPlayerIcon = new ImageIcon("m-scaled.gif");
    private ImageIcon playerIcon = new ImageIcon("nr-scaled.gif");
    private ImageIcon obstacleIcon = new ImageIcon("sis-scaled.gif");
    private static int scalar = 65;

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
    	//s.icon.paintIcon(this, g, s.x, s.y);
    	getIcon(s.type).paintIcon(this, g, s.x*scalar + 2, s.y*scalar + 2);
    }

    private void drawGrid(Graphics g){
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setColor(Color.blue);

    	int w = this.getWidth();
    	int h = this.getHeight();
    	for(int i=0; i<(w/scalar); i++){
    		g2d.drawLine(i*scalar, 0, i*scalar, h);
    	}
    	for(int i=0; i<(h/scalar); i++){
    		g2d.drawLine(0, i*scalar, w, i*scalar);
    	}
    }

    private ImageIcon getIcon(String type){
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
    
 //    private void move_ship(int i, int distance){
 //    	Ship s = (Ship)ships.get(i);
 //    	s.sail(distance);
 //    	this.repaint();
 //    }
    
 //    private void move_ship(Ship s, int distance){
 //    	s.sail(distance);
 //    	this.repaint();
 //    }
    
 // 	public void move_ship(Ship s){
 //    	move_ship(s, tick);
 //    }
    
 // 	public void move_ships(){
 //    	for(int i=0; i<num_ships; i++){
 //    		move_ship(i, tick);
 //    	}
 //    }
    
 //    public void changeScore(int deltaScore){
 //    	this.score += deltaScore;
 //    }
 //    public int score(){
 //    	return this.score;
 //    }
    
 //    private boolean collision(Ship s){
 //    	for(int i=0; i<num_ships; i++){
 //    		Ship s2 = (Ship)ships.get(i);
 //    		if(s == s2){
 //    			//do nothing; ships can't collide with themselves (I hope)
 //    		}
 //    		else if(s.isClicked(s2.longitude(), s2.lattitude())){
 //    			if(s.hasRightOfWayOver(s2)){
 //    				s.collision(s2);
 //    			}
 //    			else {
 //    				s2.collision(s);
 //    			}
 //    			return true;
 //    		}
 //    	}
 //    	return false;
 //    }
    
 //    public void setSpeed(int i){
 //    	tick = i;
 //    }
    
 //    public void tick(){
 //    	for(int i=0; i<num_ships; i++){
	// 		Ship s = (Ship)ships.get(i);
	// 		s.tick();
	// 	}
	// 	this.score--;
	// 	//System.out.println ("Score: " + this.score());
	// 	//System.out.println ("W: " + this.getWidth() + ", H: " + this.getHeight());
	// 	this.controls.tick();
	// }
	
	// public void levelUp(Ship player, Ship star){
	// 	remove(star);
	// 	int lat = this.generator.nextInt(this.getHeight());
	// 	int lon = this.generator.nextInt(this.getWidth());
	// 	star = new Star(this, "Target", lon, lat);
	// 	add_ship(star);
	// 	changeScore(1000);
	// 	this.level++;
	// 	if(level > 4){
	// 		//controller.enable("Four");
	// 		if(level > 9){
	// 			//controller.enable("Eight");
	// 		}
	// 	}
		
	// }
		
	
	// private Ship randomShip(){
	// 	int lat = this.generator.nextInt(this.getHeight());
	// 	int lon = this.generator.nextInt(this.getWidth());
	// 	int deg = this.generator.nextInt(360);
	// 	double speed = this.generator.nextDouble() * 3;
	// 	int rand = this.generator.nextInt(this.level);
	// 	switch(rand){
	// 		case 0:
	// 			return new Buoy(this, lon, lat, deg);
	// 		case 1:
	// 			return new Bomb(this, lon, lat);
	// 		case 2:
	// 			return new Minus(this, lon, lat);
	// 		case 3:
	// 			return new Plus(this, lon, lat);
	// 		case 4:
	// 			return new Pair(this, lon, lat, deg, speed);
	// 		case 5:
	// 			return new Four(this, lon, lat, deg, speed);
	// 		case 6:
	// 			return new Eight(this, lon, lat, deg, speed);
	// 		case 7:
	// 			return new Yacht(this, lon, lat, deg, speed);
	// 		case 8:
	// 			return new Torpedo(this, lon, lat, deg);
	// 		default:
	// 			return new Buoy(this, lon, lat, deg);
	// 	}
	// }
	// public void addRandomShip(){
	// 	this.add_ship(randomShip());
	// }
	// public void removeRandomShip(Ship s){
	// 	int rand = this.generator.nextInt(num_ships);
	// 	Ship toBeSunk = (Ship)ships.get(rand);
	// 	if(s != toBeSunk && toBeSunk.myShip() == false){
	// 		remove(toBeSunk);
	// 	}
	// }
		
    
 //    public void zoom(int z){
 //    	for(int i=0; i<num_ships; i++){
	// 		Ship s = (Ship)ships.get(i);
	// 		s.resize(1/z);
	// 	}
	// 	//tick = z*tick;
 //    }
    
 //    public void add_ship(Ship s){
 //    	ships.add(s);
 //    	num_ships = ships.size();
 //    	this.repaint();
 //    }
    
	// public void remove(Ship s){
	// 	ships.remove(s);
	// 	num_ships = ships.size();
	// 	controls.unsetBoat();
	// 	this.repaint();
	// }
	
	// public void remove(Ship s, String str){
	// 	ships.remove(s);
	// 	num_ships = ships.size();
	// 	controls.unsetBoat(str);
	// 	this.repaint();
	// }
	
	// public void changeShip(Ship s){
	// 	remove(this.playerShip);
	// 	this.playerShip = s;
	// 	add_ship(this.playerShip);
	// }
	
	// public void remove_all(){
	// 	ships.clear();
 //    	num_ships = ships.size();
	// 	this.repaint();
	// }
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
	// public int myShipLon(){return playerShip.longitude();}
	// public int myShipLat(){return playerShip.lattitude();}
	// public int myShipHead(){return playerShip.heading();}
	// public double myShipSpeed(){return playerShip.speed();}
	// public String myShipRig(){return playerShip.special("Rerig");}
	

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
