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

    public GameMap () {
		players = new ArrayList<Player>();
		num_players = 0;
		addMouseListener (this);
		this.setBorder (new LineBorder(Color.BLUE, 2));
		
    }
    
    public void startGame(){
  //   	this.playerShip = new Pair(this, true, true);
		// this.add_ship(this.playerShip);
		// Ship star = new Star(this, "Target");
		// this.add_ship(star);
	}

    public void paintComponent (Graphics g) {
		for(int i=0; i<num_players; i++){
			paintPlayer(i, g);
		}
    }
    
    private void paintPlayer (int i, Graphics g){
    	Player s = (Player)players.get(i);
    	s.icon.paintIcon(this, g, s.x, s.y);
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
}
