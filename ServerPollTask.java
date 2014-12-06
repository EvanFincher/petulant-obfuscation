//Dixon Minnick
//Dixon.Minnick@Tufts.edu

/*
ServerPollTask

A simple Timer Task that adds a board refresh request to the server queue
This implementation is not final, as there may be some concurrency issues yet to arise

*/

import java.util.Timer;
import java.util.TimerTask;

class ServerPollTask extends TimerTask {
	private ErlConnection erl;
 	public ServerPollTask(ErlConnection _erl){
 		super();
  		erl = _erl;
 	}
 		
 
    public void run() {
    	erl.refresh();
    }
}