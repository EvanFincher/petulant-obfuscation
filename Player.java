// // import java.lang.Math.*;
// import java.awt.Image;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Toolkit;
// import javax.swing.*;

// public class Player {
// 	public int x;
// 	public int y;
	
// 	public String name;
// 	public ImageIcon icon;

// 	public Player(String n, int x, int y, ImageIcon i){
// 		this.name = n;
// 		this.x = x;
// 		this.y = y;
// 		this.icon = i;
// 	}

	
// }

public class Player {
	public int x;
	public int y;
	public String name;
	public String type = "player";

	public Player(String n, int _x, int _y){
		this.name = n;
		this.x = _x;
		this.y = _y;
	}
	public Player(String n, int _x, int _y, String t){
		this.name = n;
		this.x = _x;
		this.y = _y;
		this.type = t;
	}
}
		
	