// import java.lang.Math.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.*;

public class Player {
	public int x;
	public int y;
	
	public String name;
	public ImageIcon icon;

	public Player(String n, int x, int y, ImageIcon i){
		this.name = n;
		this.x = x;
		this.y = y;
		this.icon = i;
	}

	// public void draw(Graphics g){
	// 	Graphics2D g2d=(Graphics2D)g;
 //        int map_x = posx - rw;
 //       	int map_y = posy - r;
 //       	RotatedIcon i = new RotatedIcon(img, deg);
 //       	i.paintIcon(map, g, map_x, map_y);
 //       	g2d.drawString(name, posx+10, posy+10);
 //    }
}

		
	