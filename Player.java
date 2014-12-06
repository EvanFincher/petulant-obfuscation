//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
Player Class

A simple data representation of an actor on the gameBoard
All members and methods are public because information hiding is not necessary

*/

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
		
	