package www.comradesoftware.vip.utils.https;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiResult {
	public Object Data ;
    public String Error;
    public String Result;
 
    public String getJSON(){
    	try{
    		if(Data==null) return "";
    		if(Data instanceof JSONArray)
    			return Data.toString();
    		
    		if(Data instanceof JSONObject)
    			return Data.toString();
    		
    		return Data.toString();
    	}
    	catch(Exception ex){
    		return "";
    	}
    }
}
