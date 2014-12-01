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
    public MyButton (String label, GameControls _gCtrl) {
    	buttonText = label;
    	type = 2;
		setText (label);
		addActionListener (this);
		gCtrl = _gCtrl;
    }
    
    public MyButton (String label, PlayerControls _ctrl) {
    	buttonText = label;
    	type = 1;
		setText (label);
		addActionListener (this);
		ctrl = _ctrl;
    }
    
    public void actionPerformed(ActionEvent e) {
	//consoleMessage = " said the fox.";
		if(type == 1){
			System.out.println("|" + buttonText + "|");
			playerMove();
		}
		if(type == 2){
			gameControl();
		}
		// if(type == 3){
		// 	go();
		// 	consoleMessage = "Have a look at " + buttonText + consoleMessage;
		// }
		//System.out.println (consoleMessage);
    }

    private void playerMove(){
    	if(buttonText == " A "){
    		//nothing yet
    	} else if (buttonText == "Up "){
    		ctrl.playerMoveUp();
    	} else if(buttonText == " S "){
    		//nothing yet
    	} else if (buttonText == "<<"){
    		ctrl.playerMoveLeft();
    	} else if(buttonText == "{#}"){
    		//nothing yet
    	} else if (buttonText == ">>"){
    		ctrl.playerMoveRight();
    	} else if(buttonText == " Z "){
    		//nothing yet
    	} else if (buttonText == "Dn "){
    		ctrl.playerMoveDown();
    	} else if(buttonText == " X "){
    		//nothing yet
    	} else {
    		ctrl.otherButton(buttonText);
    	}
    }

    private void gameControl(){
    	if(buttonText == "Connect"){
			gCtrl.gameConnect();
			buttonText = "Disconnect";
			this.setText(buttonText);
		}
		else if(buttonText == "Disconnect"){
			gCtrl.gameDisconnect();
			buttonText = "Connect";
			this.setText(buttonText);
		}
		else if(buttonText == "Join Game"){
			gCtrl.gameJoin();
		} else if(buttonText == "New Game"){
			gCtrl.gameNew();
		} else if(buttonText == "Ping"){
			gCtrl.gamePing();
		} else if(buttonText == "End Game"){
			gCtrl.gameEnd();
		} else if(buttonText == "Populate"){
			gCtrl.gameBoardPopulate();
		} else if(buttonText == "Refresh"){
			gCtrl.gameBoardRefresh();
		} else if(buttonText == "Restart Game"){
			gCtrl.gameRestart();
		}
    }

    public Boolean show(Boolean _show){
    	show = _show;
    	return show;
    }
    public Boolean isShown(){
    	return show;
    }
    
    private GameControls gCtrl;
    private PlayerControls ctrl;
    private int type;
    private String buttonText;
    private String consoleMessage;
    private Boolean show = true;
    
}