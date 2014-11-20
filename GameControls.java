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
	
	public GameControls(GameMap m){
		super();
		this.map = m;
		this.setLayout (new BoxLayout(this, BoxLayout.X_AXIS));
		defaultControls();
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
		ErlConnection erl = new ErlConnection("erlang", "cookie");
		MyButton butt = new MyButton("Disconnect", erl);
		this.add(butt);
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
}
