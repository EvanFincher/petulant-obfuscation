import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class myTextField extends JTextField implements ActionListener {
    
    
    public myTextField () {
		super ();
		addActionListener (this);
    }
    public myTextField (String s) {
		super (s, 25);
		addActionListener (this);
    }

    public void actionPerformed (ActionEvent e) {
		System.out.println ("Text: " + e.getActionCommand());
    }
}