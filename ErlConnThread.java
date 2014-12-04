import java.util.concurrent;
import com.ericsson.otp.erlang.*;
 
public class ErlConnThread implements Runnable {
 
     private Boolean gameOver = false;
     private BlockingQueue<ClientFunctionTuple> serverCalls;
     private BlockingQueue<GameBoard> boardUpdates;
     private GameMap map;
     private ErlConnection erlConn;
    
 
      public ErlConnThread(ErlConnection _erlConn, GameMap _map, BlockingQueue<ClientFunctionTuple> erlOps, BlockingQueue<GameBoard> boardUpdate) {
          serverCalls = erlOps;
          boardUpdates = boardUpdate;
          erlConn = _erlConn;
          map = _map;
      }
      public ErlConnThread(ErlConnection _erlConn, GameMap _map, BlockingQueue<ClientFunctionTuple> erlOps) {
          serverCalls = erlOps;
          boardUpdates = boardUpdate;
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
 
     private void erlConnectionCall(String call){
        GameBoard board;
        //BEGIN WORK HERE
        if(board != null){
          map.pushPlayers(board.getPlayerList());
        }
     }
 
}
