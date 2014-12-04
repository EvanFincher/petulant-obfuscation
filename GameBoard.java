import java.util.*;

public class GameBoard {
	private ArrayList<Player> playerList = new ArrayList<Player>();
	public GameBoard(ArrayList<Player> pList){
		this.playerList = pList;
	}
	public GameBoard(){
		//empty gameBoard
	}
	public ArrayList<Player> getPlayerList(){
		return playerList;
	}
	public Boolean isEmpty(){
		return (playerList.size() <= 0);
	}
	
}