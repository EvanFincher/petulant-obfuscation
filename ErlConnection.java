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
    
 
      public ErlConnection(String _peer, String _cookie) {
          peer = _peer;
          cookie = _cookie;
          hostname = getHostName();
          qualifiedPeer = peer + "@" + hostname;
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
        }
        catch (Exception exp) {
         System.out.println("hostname error is :" + exp.toString());
         exp.printStackTrace();
         return "localhost";
       }
     }
 
}
