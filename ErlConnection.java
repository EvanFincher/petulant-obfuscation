//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
ErlConnection Class

Simulates an Erlang node,
connects and maintains connection to an actual erlang client node,
Parses the erlang responses into Java datatypes

*/

import com.ericsson.otp.erlang.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;
import java.util.*;
 
 
public class ErlConnection {
 
     private static OtpConnection conn;
     private OtpMbox mbox;
     public OtpErlangObject received;
     private final String peer;
     private final String cookie;
     private final String hostname;
     private final String qualifiedPeer;
     private Boolean isConnected = false;
     private Boolean erlangModulesLoaded = false;
     private Boolean gameJoined = false;

     private OtpErlangAtom sName;
     private OtpErlangAtom sNode;
     private OtpErlangObject playerName;

     private ArrayBlockingQueue<ClientFunctionTuple> serverCalls;
    
 
      public ErlConnection(String _peer, String _cookie, ArrayBlockingQueue<ClientFunctionTuple> _serverCalls) {
          serverCalls = _serverCalls;
          peer = _peer;
          cookie = _cookie;
          hostname = getHostName();
          //qualifiedPeer = peer + "@" + hostname;
          //qualifiedPeer = "client@ubuntu";
          qualifiedPeer = "client@lab118g";
          connect();
 
          if(!isConnected){
            //retryConnect(2);
          }
 
      }
 
      public void connect() {
       System.out.print("Please wait, connecting to "+peer+"....\n");
       System.out.print("Please wait, connecting to "+qualifiedPeer.trim()+"....\n");
 
       String javaClient ="java";
       try {
         OtpSelf self = new OtpSelf(javaClient, cookie.trim());
         OtpPeer other = new OtpPeer(qualifiedPeer.trim());
         //OtpPeer other = new OtpPeer(peer);

         

         conn = self.connect(other);

         isConnected = true;

         loadErlangModules();

         System.out.println("Connection Established with "+peer+"\n");
       }
       catch (UnknownHostException exp) {
         System.out.println("connection error is :" + exp.toString());
         exp.printStackTrace();
       }
       catch (OtpAuthException exp) {
         System.out.println("connection error is :" + exp.toString());
         exp.printStackTrace();
       }
       catch (Exception exp) {
         System.out.println("connection error is :" + exp.toString());
         exp.printStackTrace();
       }
 
     }

     private void retryConnect(int attemptsRemaining){
        if(attemptsRemaining > 0 && !isConnected){
          //connect();
          attemptsRemaining -= 1;
          retryConnect(attemptsRemaining);
        }
     }

 
     public void disconnect() {
       System.out.println("Disconnecting....");
       if(conn != null){
         conn.close();
       }
       isConnected = false;
       System.out.println("Successfuly Disconnected");
     }

     private void loadErlangModules() {
      if(!erlangModulesLoaded){
        try {
           conn.sendRPC("shell_default", "c", new OtpErlangObject[]{new OtpErlangAtom("client")});
           received = conn.receiveRPC();
           System.out.println(received.toString());
           conn.sendRPC("shell_default", "c", new OtpErlangObject[]{new OtpErlangAtom("server")});
           received = conn.receiveRPC();
           System.out.println(received.toString());
           conn.sendRPC("shell_default", "c", new OtpErlangObject[]{new OtpErlangAtom("javaClient")});
           received = conn.receiveRPC();
           System.out.println(received.toString());
           erlangModulesLoaded = true;
        }
        catch (Exception exp) {
           System.out.println("Load Module error is :" + exp.toString());
           exp.printStackTrace();
         }
       }
     }

     public void kill() {
        System.out.println("Killing Node");
        if(!isConnected){
          connect();
        }
        
       try {
         conn.sendRPC("init", "stop", new OtpErlangList());
         received = conn.receiveRPC();
         System.out.println(received.toString());
         erlangModulesLoaded = false;
       }
       catch (Exception exp) {
         System.out.println("kill error is :" + exp.toString());
         exp.printStackTrace();
       }
     }

     private String getHostName(){
        try{
          InetAddress addr = InetAddress.getLocalHost();
          String hostname = addr.getHostName();
          //return hostname + ".tufts.edu";
          return "localhost";
          //return hostname;
        }
        catch (Exception exp) {
         System.out.println("hostname error is :" + exp.toString());
         exp.printStackTrace();
         return "localhost";
       }
     }

     public void joinGame(String gameAccessToken, String serverHost){
        sName = new OtpErlangAtom(gameAccessToken);
        sNode = new OtpErlangAtom(serverHost);
        // if(clientFunctionSync("join")){
        //   gameJoined = true;
        // }
        clientFunction("join");
     }

     public void startServer(String gameAccessToken, String serverHost){
        sName = new OtpErlangAtom(gameAccessToken);
        sNode = new OtpErlangAtom(serverHost);
        start("start");
        //joinGame(gameAccessToken, serverHost);
      }
      public void restart(){
        start("restart");
      }

     private void start(String action){
        clientFunction(action);
     }

     public void stop(){
        clientFunction("stop");
     }

     public void ping(){
        clientFunction("ping");
     }

     public void refresh(){
        clientFunction("refresh");
     }

     public void populate(){
        populate("preset", "one");
     }

     private void populate(String _type, String _data){
        OtpErlangAtom type = new OtpErlangAtom(_type);
        OtpErlangAtom data = new OtpErlangAtom(_data);
        OtpErlangObject args[] = new OtpErlangObject[]{sName, sNode, type, data};
        clientFunction("populate", args);
     }

     public void move(String direction){
        action("move", direction);
     }

     public void attack(String direction){
        action("attack", direction);
     }

     private void action(String action, String direction){
        OtpErlangObject args[] = new OtpErlangObject[]{sName, sNode, new OtpErlangAtom(action), new OtpErlangAtom(direction), playerName};
        clientFunction("act", args);
     }

     public void lookup(){
        OtpErlangObject args[] = new OtpErlangObject[]{sName, sNode, playerName};
        clientFunction("lookup", args);
     }

     private void clientFunction(String function, OtpErlangObject[] args){
        ClientFunctionTuple tuple = new ClientFunctionTuple(function, args);
        try{
          System.out.println("Put");
          serverCalls.put(tuple);
        }
        catch (InterruptedException ex){
          System.out.println("Too many server calls");
        }
     }
     private void clientFunction(String function){
        if(sName == null || sNode == null){
          return;
        }
        OtpErlangObject stdArgs[] = new OtpErlangObject[]{sName, sNode};
        clientFunction(function, stdArgs);
     }
     public GameBoard clientFunctionAsyncGameBoard(ClientFunctionTuple call){
        String function = call.function;
        OtpErlangObject[] args = call.args;
        GameBoard board  = new GameBoard();
        if(!checkPermission(function)){
          return board;
        }
        try {
           conn.sendRPC("javaClient", function, args);
           received = conn.receiveRPC();
           System.out.println(function + ":");
           System.out.println(received.toString());
           board = parseResponse(function);
        }
        catch (Exception exp) {
           System.out.println(function + " error is :" + exp.toString());
           exp.printStackTrace();
         }
         System.out.println("\n");
         return board;
     }

     private Boolean clientFunctionSync(String function){
        if(sName == null || sNode == null){
          return false;
        }
        OtpErlangObject stdArgs[] = new OtpErlangObject[]{sName, sNode};
        return clientFunctionSync(function, stdArgs);
     }
     private Boolean clientFunctionSync(String function, OtpErlangObject[] args){
        Boolean success = false;
        if(!checkPermission(function)){
          return false;
        }
        try {
           conn.sendRPC("javaClient", function, args);
           received = conn.receiveRPC();
           System.out.println(function + ":");
           System.out.println(received.toString());
           success = true;
        }
        catch (Exception exp) {
           System.out.println(function + " error is :" + exp.toString());
           exp.printStackTrace();
         }
         System.out.println("\n");
         return success;
     }

     private Boolean checkPermission(String function){
        if(gameJoined){
          return true;
        }
        else if(function == "join" || function == "start" || function == "stop" || function == "ping" || function == "restart" || function == "refresh" || function == "populate"){
          return true;
        }
        return false;
     }

     private GameBoard parseResponse(String function){
        GameBoard board = new GameBoard();
        if(function == "start" || function == "stop" || function == "restart" || function == "lookup" || function == "ping"){
          //handle other stuff
        }
        else{
          OtpErlangTuple response = (OtpErlangTuple)received;
          String status = response.elementAt(0).toString();
          OtpErlangObject gameBoard = response.elementAt(1);
          System.out.println("status: " + status);
          board = parseGameBoard(gameBoard);
          if(function == "join"){
            playerName = response.elementAt(0);
            board.setMyPlayer(status);
            gameJoined = true;
          }
        }
        return board;
     }

     private GameBoard parseGameBoard(OtpErlangObject erlOtpBoard){
        GameBoard board = new GameBoard();
        //System.out.println(erlOtpBoard.toString() + "\n");
        OtpErlangList erlOtpPlayerList = (OtpErlangList)erlOtpBoard;
        System.out.println(erlOtpPlayerList.elementAt(0).toString() + "\n");
        ArrayList<Player> playerList = new ArrayList<Player>();
        for(int i=0; i<erlOtpPlayerList.arity(); i++){
          Player p = parsePlayer(erlOtpPlayerList.elementAt(i));
          if(p.type != "bounds"){
            playerList.add(p);
          }
        }
        board = new GameBoard(playerList);
        return board;
     }

     private Player parsePlayer(OtpErlangObject erlOtpPlayerTile){
        OtpErlangTuple playerTile = (OtpErlangTuple)erlOtpPlayerTile;
        OtpErlangTuple player = (OtpErlangTuple)playerTile.elementAt(1);
        OtpErlangTuple location = (OtpErlangTuple)player.elementAt(1);
        
        OtpErlangObject erlOtpPlayerName = player.elementAt(2);
        OtpErlangAtom erlOtpPlayerType = (OtpErlangAtom)player.elementAt(3);
        String playerType = parsePlayerTypeString(erlOtpPlayerType, erlOtpPlayerName);
        String playerName = erlOtpPlayerName.toString();
        //String playerType = player.elementAt(3).toString();
        int x = Integer.parseInt(location.elementAt(0).toString());
        int y = Integer.parseInt(location.elementAt(1).toString());
        System.out.println(playerType + ": " + playerName + " at " + x + "," + y);
        Player p = new Player(playerName.trim(), x, y, playerType.trim());
        return p;
     }

     private String parsePlayerTypeString(OtpErlangAtom erlOtpPlayerType, OtpErlangObject erlOtpPlayerName){
        if(erlOtpPlayerType.equals(new OtpErlangAtom("bounds"))){
          return "bounds";
        }
        else if(playerName != null){
          if(playerName.equals(erlOtpPlayerName)){
            return "myPlayer";
          }
        }
        if(erlOtpPlayerType.equals(new OtpErlangAtom("player"))){
          return "player";
        }
        return "object";
     }

     
 
}
