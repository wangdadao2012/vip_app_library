package www.comradesoftware.vip.utils.https;


public class LogEx {
	private String TAG="ACU";
	
	public LogEx(){
	
	}
	
	public LogEx(String tag){
		if(Helper.isEmpty(tag)) return;
		
		this.TAG=tag;
	}
	
	public void i(Object msg){		
		android.util.Log.i(TAG,Helper.obj2Str(msg));
	}

	public void m(String method, Object... args){		
		StringBuilder sb=new StringBuilder();
		sb.append("--"+method+"--");
		for(Object item : args){
			sb.append("-["+item+"]-");
		}
		i(sb);
	}
	
	public void w(Object msg){
		android.util.Log.w(TAG,Helper.obj2Str(msg));
	}
	
	public void d(Object msg){
		android.util.Log.d(TAG,Helper.obj2Str(msg));
	}

	public void e(Throwable t){
		android.util.Log.e(TAG,Helper.obj2Str(t.getMessage()),t);		
	}
	
	public void e(Object msg,Throwable t){
		android.util.Log.e(TAG,Helper.obj2Str(msg),t);		
	}

	public static LogEx getInstance(){
		return new LogEx();
	}

	public static LogEx getInstance(String tag){
		return new LogEx(tag);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Log");
	}
	
}
