
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

public class PlayerControls extends JPanel {
	private ArrayList<MyButton> buttons;
	// private ArrayList<MyScrollBar> bars;
	
	private Player myPlayer;
	private GameMap map;
	private JLabel playerName;
	private JLabel longitude;
	private JLabel lattitude;
	
	public PlayerControls(){
		super();
		buttons = new ArrayList<MyButton>();
		// bars = new ArrayList<MyScrollBar>();
		myPlayer = null;
		map = null;
		this.setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
		defaultControls();
		this.setBorder (new LineBorder(Color.YELLOW, 1));
	}
	public PlayerControls(GameMap m){
		super();
		buttons = new ArrayList<MyButton>();
		// bars = new ArrayList<MyScrollBar>();
		myPlayer = null;
		this.map = m;
		this.setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
		defaultControls();
		this.setBorder (new LineBorder(Color.ORANGE, 1));
	}
	public void add_w(MyButton butt){
		super.add(butt);
		buttons.add(butt);
	}
	// public void add_w(MyScrollBar sbarro){
	// 	super.add(sbarro);
	// 	bars.add(sbarro);
	// }
	public void setPlayer(Player s){
		myPlayer = s;
		playerName.setText("Player Name: " + s.name);
		lattitude.setText("Lattitude: " + s.y);
		longitude.setText("Longitude: " + s.x);
		// for(int i=0; i<buttons.size(); i++){
		// 	MyButton b = (MyButton)buttons.get(i);
		// 	b.setBoat(s);
		// }
		// for(int i=0; i<bars.size(); i++){
		// 	MyScrollBar b = (MyScrollBar)bars.get(i);
		// 	b.setBoat(s);
		// }
	}
	// public void unsetBoat(){
	// 	this.unsetBoat("no_boat");
	// }
	// public void unsetBoat(String str){
	// 	thisBoat = null;
	// 	shipName.setText("Status: " + str);
	// 	lattitude.setText("Lattitude: no_boat");
	// 	longitude.setText("Longitude: no_boat");
	// 	for(int i=0; i<buttons.size(); i++){
	// 		MyButton b = (MyButton)buttons.get(i);
	// 		b.unsetBoat();
	// 	}
	// 	for(int i=0; i<bars.size(); i++){
	// 		MyScrollBar b = (MyScrollBar)bars.get(i);
	// 		b.unsetBoat();
	// 	}
	// }
	// public void tick(){
	// 	if(thisBoat != null){
	// 		lattitude.setText("Lattitude: " + thisBoat.lattitude());
	// 		longitude.setText("Longitude: " + thisBoat.longitude());
	// 	}
	// }
	private void defaultControls(){
		playerName = new JLabel("Player Name: not yet joined");
		this.add(playerName);
		this.add(new JLabel("_________"));
		lattitude = new JLabel("Lattitude");
		longitude = new JLabel("Longitude");
		//JLabel temp = new JLabel("Heading");
		//MyScrollBar deg_b = new MyScrollBar(temp);
		this.add(lattitude);
		this.add(longitude);
		//this.add(temp);
		//this.add_w(deg_b);
		this.add(new JLabel("_________"));
		// temp = new JLabel("Sail");
		// MyScrollBar sail_b = new MyScrollBar(temp, this.map);
		// this.add(temp);
		// this.add_w(sail_b);
		// temp = new JLabel("Speed: 0");
		// this.add(temp);
		// MyButton speed_butt = new MyButton("+", temp);
		// this.add_w(speed_butt);
		// speed_butt = new MyButton("-", temp);
		// this.add_w(speed_butt);
		// this.add(new JLabel("_________"));
		// MyButton delButt = new MyButton("Delete", this.map, this);
		// this.add_w(delButt);
		//AddWidget w = new AddWidget(map, this);
		//this.add(w);
		JPanel temp2 = new JPanel();
		temp2.setLayout(new FlowLayout());
		this.add(temp2); //spacer
		//this.unsetBoat();
	}
	public void setGeo(int x, int y){
		lattitude.setText("Lattitude: " + y);
		longitude.setText("Longitude: " + x);
	}
}