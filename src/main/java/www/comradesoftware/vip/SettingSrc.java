package www.comradesoftware.vip;

public class SettingSrc implements www.comradesoftware.vip.i.ISetting {
	
	@Override
	public String getAppID(){
		return "vip";
	}
	
	@Override
	public String getAppName(){
		return "会员app";
	}

	@Override
	public boolean getDebug() {
		return true;
	}

	@Override
	public String getToken() {
		return "token";
	}

	@Override
	public String getDomain() {
		return "http://app.1m1m.cc/";
	}

}
