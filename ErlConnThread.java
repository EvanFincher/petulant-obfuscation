//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
ErlConnection Thread Class

Operates a seperate thread that
contains a thread-safe blocking queue of server calls over the erlang  connection,
and handles the parsed response.

*/
import java.util.concurrent.*;
import com.ericsson.otp.erlang.*;
 
public class ErlConnThread implements Runnable {
 
     private Boolean gameOver = false;
     private ArrayBlockingQueue<ClientFunctionTuple> serverCalls;
     private ArrayBlockingQueue<GameBoard> boardUpdates;
     private GameMap map;
     private ErlConnection erlConn;
    
 
      public ErlConnThread(ErlConnection _erlConn, GameMap _map, ArrayBlockingQueue<ClientFunctionTuple> erlOps, ArrayBlockingQueue<GameBoard> boardUpdate) {
          serverCalls = erlOps;
          boardUpdates = boardUpdate;
          erlConn = _erlConn;
          map = _map;
      }
      public ErlConnThread(ErlConnection _erlConn, GameMap _map, ArrayBlockingQueue<ClientFunctionTuple> erlOps) {
          serverCalls = erlOps;
          erlConn = _erlConn;
          map = _map;
      }

      public void run(){
        try{
          while(!gameOver){
            ClientFunctionTuple call = serverCalls.take();
            erlConnectionCall(call);
          }
        }
        catch (InterruptedException ex){
          System.out.print("Interrupted ErlConnThread");
        }
      }
 
     private void erlConnectionCall(ClientFunctionTuple call){
        GameBoard board;
        board = erlConn.clientFunctionAsyncGameBoard(call);
        if(!board.isEmpty()){
          String playerName = board.getPlayerName();
          if(playerName != "not_set"){
            map.setMyPlayer(playerName);
          }
          map.pushPlayers(board.getPlayerList());
        }
     }
 
}
