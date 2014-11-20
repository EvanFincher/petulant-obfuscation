//Dixon Minnick
//Dixon.Minnick@Tufts.edu

//Main Program
//  creates all elements, and renders them in a suitable layout

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

public class Main extends JFrame {
    private GameMap harbor_ctrl;

    public static void main (String [] args) {
		new Main ();
    }
    
    public Main () {
	// Window setup
	super("Our Game");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize (800, 800);
	Container content = getContentPane();
	content.setLayout (new BorderLayout());
	
	harbor_ctrl = new GameMap();
	GameControls mControls = new GameControls(harbor_ctrl);
	PlayerControls controls = new PlayerControls(harbor_ctrl);
	harbor_ctrl.setControls(controls);
	
	JPanel pan = new JPanel();
	pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
	
	content.add (controls, BorderLayout.EAST);
	content.add (mControls, BorderLayout.NORTH);
	content.add (harbor_ctrl, BorderLayout.CENTER);
	content.add (pan, BorderLayout.SOUTH);
	
	// Show the window
	setVisible (true);
	}
}