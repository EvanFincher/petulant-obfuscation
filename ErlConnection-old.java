import com.ericsson.otp.erlang.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
 
 
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
    
 
      public ErlConnection(String _peer, String _cookie) {
          peer = _peer;
          cookie = _cookie;
          hostname = getHostName();
          //qualifiedPeer = peer + "@" + hostname;
          //qualifiedPeer = "client@ubuntu";
          qualifiedPeer = "client@wr-130-64-196-52";
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
        if(clientFunction("join")){
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

     private Boolean clientFunction(String function){
        OtpErlangObject stdArgs[] = new OtpErlangObject[]{sName, sNode};
        return clientFunction(function, stdArgs);
     }
     private Boolean clientFunction(String function, OtpErlangObject[] args){
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
        else if(function == "join" || function == "start" || function == "stop" || function == "ping" || function == "restart" || function == "refresh"){
          return true;
        }
        return false;
     }
 
}
