import com.ericsson.otp.erlang.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;
 
 
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
     private OtpErlangAtom playerName = new OtpErlangAtom("0"); //initialize for now

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
        if(clientFunctionSync("join")){
          gameJoined = true;
        }
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
        OtpErlangObject actionObj[] = new OtpErlangObject[]{new OtpErlangAtom(action), new OtpErlangAtom(direction)};
        OtpErlangTuple actionTuple = new OtpErlangTuple(actionObj);
        action(actionTuple);
     }

     private void action(OtpErlangTuple action){
        OtpErlangObject args[] = new OtpErlangObject[]{sName, sNode, action, playerName};
        clientFunction("action", args);
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
          //OtpErlangTuple response = new OtpErlangTuple(received);
          OtpErlangTuple response = (OtpErlangTuple)received;
          String status = response.elementAt(0).toString();
          OtpErlangObject gameBoard = response.elementAt(1);
          System.out.println("status: " + status);
          board = parseGameBoard(gameBoard);
        }
        return board;
     }

     private GameBoard parseGameBoard(OtpErlangObject erlOtpBoard){
        GameBoard board = new GameBoard();
        System.out.println(erlOtpBoard.toString());
        return board;
     }

 
}
