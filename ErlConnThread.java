
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
          map.pushPlayers(board.getPlayerList());
        }
     }
 
}
