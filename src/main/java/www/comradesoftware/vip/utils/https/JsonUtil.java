package www.comradesoftware.vip.utils.https;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class JsonUtil {
	public static Gson gson=null;


	public static final TypeAdapter<Date> DATE = new TypeAdapter<Date>() {

		public Date read(JsonReader reader) {
			try {
				if (reader.peek() == JsonToken.NULL) {
					reader.nextNull();
					return null;
				}

				String val = reader.nextString();
				if (null == val || "".equals(val))
					return null;

				String fmtstr = null;
				if (val.indexOf(':') > 0) {
					fmtstr = "yyyy-MM-dd HH:mm:ss";
				} else {
					fmtstr = "yyyy-MM-dd";
				}
				SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
				return sdf.parse(val);
			} catch (Exception e) {
				e.printStackTrace();
				throw new JsonSyntaxException(e);
			}
		}

		@Override
		public void write(JsonWriter writer, Date value) throws IOException {
			try {
				String val="";
				if(value!=null)
					val=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
				writer.value(val);
			} catch (Exception e) {
				e.printStackTrace();
				writer.value("");
			}
		}
	};

	public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {

		public String read(JsonReader reader) {
			try {
				if (reader.peek() == JsonToken.NULL) {
					reader.nextNull();
					return null;
				}

				String val = reader.nextString();
				if (null == val || "".equals(val))
					return "";

				return val;
			} catch (Exception e) {
				e.printStackTrace();
				throw new JsonSyntaxException(e);
			}
		}

		@Override
		public void write(JsonWriter writer, String value) throws IOException {
			try {
				writer.value(value);
			} catch (Exception e) {
				e.printStackTrace();
				writer.value("");
			}
		}
	};

	static {
		GsonBuilder gsonBulder = new GsonBuilder();
		gsonBulder.registerTypeAdapter(Date.class, DATE);
		gsonBulder.registerTypeAdapter(String.class, STRING);
		//通过反射获取instanceCreators属性
//        try {
//            Class builder = (Class) gsonBulder.getClass();
//            Field f = builder.getDeclaredField("instanceCreators");
//            f.setAccessible(true);
//            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);//得到此属性的值
//            //注册数组的处理器
//            gsonBulder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new ConstructorConstructor(val)));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
		gson=gsonBulder.create();
	}


	public static JSONObject eval(String json){
		try {
			if(json.isEmpty())return null;
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
//			new LogEx("JsonUtil").e(e.getMessage(),e);
		}
		return null;
	}

	public static JSONArray evalArr(String json){
		try {
			if(json.isEmpty())return null;
			return new JSONArray(json);
		} catch (JSONException e) {
//			new LogEx("JsonUtil").e(e.getMessage(),e);
		}
		return null;
	}

	public static boolean exists(JSONObject jsonObj, String key){
		try{
			return jsonObj.has(key);
		}
		catch(Exception e){
			return false;
		}
	}

	public static String getString(JSONObject jsonObj,String key){
		try{
			return jsonObj.getString(key);
		}
		catch(Exception e){
//			new LogEx("JsonUtil").e(e.getMessage(),e);
			return "";
		}
	}

	public static int getInt(JSONObject jsonObj,String key){
		try{
			return jsonObj.getInt(key);
		}
		catch(Exception e){
//			new LogEx("JsonUtil").e(e.getMessage(),e);
			return 0;
		}
	}

	public static float getFloat(JSONObject jsonObj,String key){
		try{
			return (float)jsonObj.getDouble(key);
		}
		catch(Exception e){
//			new LogEx("JsonUtil").e(e.getMessage(),e);
			return 0f;
		}
	}

	public static Double getDouble(JSONObject jsonObj,String key){
		try{
			return jsonObj.getDouble(key);
		}
		catch(Exception e){
//			new LogEx("JsonUtil").e(e.getMessage(),e);
			return 0d;
		}
	}

	public static Boolean getBool(JSONObject jsonObj,String key){
		try{
			return jsonObj.getBoolean(key);
		}
		catch(Exception e){
//			new LogEx("JsonUtil").e(e.getMessage(),e);
			return false;
		}
	}

	public static JSONArray getJSONArray(JSONObject jsonObj,String key){
		try{
			return jsonObj.getJSONArray(key);
		}
		catch(Exception e){
			return null;
		}
	}

	public static JSONObject getJSONObject(JSONObject jsonObj,String key){
		try{
			return jsonObj.getJSONObject(key);
		}
		catch(Exception e){
			return null;
		}
	}

	public static String toJson(Object obj){
		try{
			if (obj instanceof  JSONArray)
				return ((JSONArray)obj).toString();

			return gson.toJson(obj);
		}
		catch(Exception e){
			new LogEx("JsonUtil").e(e.getMessage(),e);
			return null;
		}
	}

	public static Object toClass(String json , Class<?> cls){
		return  gson.fromJson(json, cls);
	}
	/***
	 * new TypeToken<ArrayList<ShopVO>>(){}
	 * @param json
	 * @param typeToken
	 * @return
	 */
	public static Object toClassList(String json , TypeToken<?> typeToken){
		return gson.fromJson(json,typeToken.getType());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}