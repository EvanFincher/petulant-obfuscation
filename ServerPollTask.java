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