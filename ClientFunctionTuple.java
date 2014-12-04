import com.ericsson.otp.erlang.*;

public class ClientFunctionTuple {
  public String function;
  public OtpErlangObject[] args;
  public ClientFunctionTuple(String _function, OtpErlangObject[] _args){
    this.function = _function;
    this.args = _args;
  }
}