//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
GameBoard Class

A simple data structure representation of a GameBoard object recieved from the server

*/
import java.util.*;

public class GameBoard {
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private String playerName = "not_set";
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
	public void setMyPlayer(String _playerName){
		playerName = _playerName;
	}
	public String getPlayerName(){
		return playerName;
	}
	
}