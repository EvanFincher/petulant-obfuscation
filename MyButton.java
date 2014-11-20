//Dixon Minnick
//Dixon.Minnick@Tufts.edu

//Button Class
//Creates a general purpose button class that can perform different methods depending on
//	the constructor

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MyButton extends JButton implements ActionListener {
    public MyButton (String label, ErlConnection _erl) {
    	buttonText = label;
    	type = 2;
		setText (label);
		addActionListener (this);
		erl = _erl;
    }
    
    
    public void actionPerformed(ActionEvent e) {
	//consoleMessage = " said the fox.";
		// if(type == 1){
		// 	consoleMessage = buttonText + " " + buttonText + consoleMessage;
		// }
		if(type == 2){
			if(buttonText == "Connect"){
				erl.connect();
				buttonText = "Disconnect";
				this.setText(buttonText);
			}
			else if(buttonText == "Disconnect"){
				erl.disconnect();
				buttonText = "Connect";
				this.setText(buttonText);
			}
			//consoleMessage =  buttonText + " erlang node" + consoleMessage;
		}
		// if(type == 3){
		// 	go();
		// 	consoleMessage = "Have a look at " + buttonText + consoleMessage;
		// }
		//System.out.println (consoleMessage);
    }
    
    private ErlConnection erl;
    private int type;
    private String buttonText;
    private String consoleMessage;
    
}