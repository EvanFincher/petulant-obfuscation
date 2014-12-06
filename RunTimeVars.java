//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
RunTime Variables data structure

For the time being, it will be necessary to set some configuration variables here in order to run the game

*/
class RunTimeVars {

	//usage: client@[YOUR HOST], eg: client@localhost or client@ubuntu
	public static String qualifiedPeer = "client@lab118g"; 

	//usage: [YOUR HOST NAME]
	public static String hostname = "lab118g";

	//usage: [SERVER NODE SHORT NAME]
	public static String sName = "server";

	//This is not imperative to change
	public static String defaultServerHost = sName + "@" + hostname;

	//Do Not change this. You will need to use it for your server
	public static String cookie = "cookie";
}