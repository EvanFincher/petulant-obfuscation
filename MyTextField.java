import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MyTextField extends JTextField implements ActionListener {
    
    
    public MyTextField () {
		super ();
		addActionListener (this);
    }
    public MyTextField (String s) {
		super (s, 25);
		addActionListener (this);
    }

    public void actionPerformed (ActionEvent e) {
		System.out.println ("Text: " + e.getActionCommand());
    }
}