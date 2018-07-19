package www.comradesoftware.vip.utils.https;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class Helper {

	// >>>>>>>>>>>>>>>>>>>>>>常量>>>>>>>>>>>>>>>>>>>
	public static final int Handler_What_ShowLoading = 20010;
	public static final int Handler_What_CloseLoading = 20011;
	public static final int Handler_What_Toast = 20020;
	public static final int Handler_What_ShowInputSoft = 20030;
	public static final int Handler_What_HideInputSoft = 20031;
	// <<<<<<<<<<<<<<<<<<<<<<常量<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>变量>>>>>>>>>>>>>>>>>>>
	public static ProgressDialog dialog1 = null;
	// >>>>>>>>>>>>>>>>>>>>>>变量>>>>>>>>>>>>>>>>>>>

	// >>>>>>>>>>>>>>>>>>>>>>类型转换>>>>>>>>>>>>>>>>>>>
	public static String obj2Str(Object obj) {
		if (obj == null)
			return "";
		return String.format("%s", obj);
	}

	public static String obj2StrEx(Object obj) {
		if (obj == null)
			return "";
		return obj2Str(obj).trim();
	}

	public static int obj2Int(Object obj) {
		try {
			if (obj == null || obj2StrEx(obj).length() <= 0)
				return 0;

			if (Pattern.matches("^([0-9]*)$", obj2Str(obj)))
				return Integer.parseInt(obj2StrEx(obj));
			else
				return 0;
		} catch (Throwable ex) {
			return 0;
		}
	}

	public static long obj2Long(Object obj) {
		try {
			if (obj == null || obj2StrEx(obj).length() <= 0)
				return 0;

			if (Pattern.matches("^([0-9]*)$", obj2Str(obj)))
				return Long.parseLong(obj2StrEx(obj));
			else
				return 0;
		} catch (Throwable ex) {
			return 0;
		}
	}

	public static double obj2Double(Object obj) {
		try {
			if (obj == null || obj2StrEx(obj).length() <= 0)
				return 0;
			return Double.parseDouble(obj.toString());
		} catch (Throwable ex) {
			return 0;
		}
	}

	public static float obj2Float(Object obj) {
		try {
			if (obj == null || obj2StrEx(obj).length() <= 0)
				return 0;
			return Float.parseFloat(obj.toString());
		} catch (Throwable ex) {
			return 0;
		}
	}

	public static float obj2Money(float obj) {
		try {
			if (obj == 0)
				return 0f;
			return Math.round(obj * 100f) / 100f;
		} catch (Throwable ex) {
			return 0f;
		}
	}

	public static float obj2Money(String obj) {
		try {
			return Math.round(obj2Float(obj) * 100f) / 100f;
		} catch (Throwable ex) {
			return 0f;
		}
	}

	public static Bitmap obj2Bitmap(Object obj) {
		Bitmap result = null;
		try {
			if (obj == null)
				return null;
			if (obj instanceof byte[]) {
				byte[] data = (byte[]) obj;
				result = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		return new android.graphics.drawable.BitmapDrawable(bitmap);
	}
	public static byte[] str2Gbk(String txt) {
		byte[] data = null;
		try {
			data = txt.getBytes("GBK");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}

	/**
	 * 对象 转换 二进制数据
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] obj2Data(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> obj2Map1(Object obj) {
		Map<String, String> result = null;
		try {
			if (obj == null)
				return null;
			if (obj instanceof Map) {
				result = (Map<String, String>) obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static View obj2View(Object obj) {
		View result = null;
		try {
			if (obj != null && obj instanceof View)
				result = (View) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * C# Format方式
	 *
	 * @param fmt
	 *            "A{0}B{1}"
	 * @param args
	 * @return
	 */
	public static String format(String fmt, Object... args) {
		if (fmt == null)
			return "";

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				fmt = fmt.replace("{" + i + "}", "" + args[i]);
			}
		}

		return fmt;// MessageFormat.format(fmt.replace("'", "''"), args);
	}

	// <<<<<<<<<<<<<<<<<<<<<<类型转换<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>Map>>>>>>>>>>>>>>>>>>>
	public static Map<String, Object> makeMap(Object... args) {
		Map<String, Object> map = new TreeMap<String, Object>();
		try {
			String key = null;
			for (Object item : args) {
				if (key == null) {
					key = item.toString();
				} else {
					map.put(key, item);
					key = null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	public static Map<String, String> makeMap2(String... args) {
		Map<String, String> map = new TreeMap<String, String>();
		try {
			String key = null;
			for (String item : args) {
				if (key == null) {
					key = item;
				} else {
					map.put(key, item);
					key = null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}
	// <<<<<<<<<<<<<<<<<<<<<<Map<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>时间日期相关>>>>>>>>>>>>>>>>>>>

	public static String getYMDDateString(String oldDate) {
		String newDate = oldDate.replaceAll("年", "-");
		newDate = newDate.replaceAll("月", "-");
		newDate = newDate.replaceAll("日", "");
		return newDate.trim();
	}

	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {

				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	private String getNowTime() {
		return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()); // 设置日期格式
	}

	public static String fmtDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date); // 设置日期格式
	}

	public static Date now() {
		return Calendar.getInstance().getTime();
	}
	// <<<<<<<<<<<<<<<<<<<<<<时间日期相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>判断相关>>>>>>>>>>>>>>>>>>>
	public static boolean isInt(Object value) {
		if (value == null || Helper.obj2StrEx(value).length() <= 0)
			return false;

		return Pattern.matches("^([0-9]*)$", Helper.obj2Str(value));
	}

	/**
	 * 是否是浮点数
	 *
	 * @param value
	 * @return
	 */
	public static boolean isDouble(Object value) {
		if (value == null || Helper.obj2StrEx(value).length() <= 0)
			return false;

		return Pattern.matches("^[-+]?\\d+(\\.\\d+)?$", Helper.obj2Str(value));
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		try {
			if (value == null)
				return true;

			if (Helper.obj2StrEx(value).length() > 0) {
				return false;
			}
		} catch (Exception e) {

		}

		return true;
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	// <<<<<<<<<<<<<<<<<<<<<<判断相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>字符串相关>>>>>>>>>>>>>>>>>>
	public static String[] split(String str, String tag) {
		return str.split("\\" + tag);
	}
	// <<<<<<<<<<<<<<<<<<<<<<字符串相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>排序>>>>>>>>>>>>>>>>>>
	public static List<String> sortList(List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		return list;
	}
	// <<<<<<<<<<<<<<<<<<<<<<排序<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>文件 流 路径 相关>>>>>>>>>>>>>>>>>>>
	/**
	 * 获取路径所在目录路径
	 *
	 * @param filePath
	 * @return 尾部没有 /
	 */
	public static String getPathDir(String filePath) {
		try {
			return filePath.substring(0, filePath.lastIndexOf("/"));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取完整物理路径
	 * @param ctx
	 * @param filePath "/123/1.jpg"
	 * @return
	 */
	public static String getFullFilePath(Context ctx, String filePath) {
		String path = "" + ctx.getFilesDir();
		if (!filePath.startsWith("/"))
			path += "/";
		path += filePath;

		return path;
	}

	/**
	 * 改名文件
	 *
	 * @param filePath
	 */
	public static boolean renameFile(String filePath, String newFilePath) {
		try {
			File file = new File(filePath);
			File newFile = new File(newFilePath);
			file.renameTo(newFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 */
	public static void delFile(String filePath) {
		try {
			Helper.delFile(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 *
	 * @param file
	 */
	public static void delFile(File file) {
		try {

			if (file.exists())
				file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void saveBitmap(Bitmap bitmap, String pathFile) {
		BufferedOutputStream bos = null;
		try {
			String dir = Helper.getPathDir(pathFile);

			File dirFile = new File(dir);

			if (!dirFile.exists())
				dirFile.mkdir();

			File myCaptureFile = new File(pathFile);
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 读取文件
	 *
	 * @param filePath
	 * @return
	 */
	public static byte[] getFile2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public static void saveFile(byte[] buf, String dirPath, String fileName) {
		saveFile(buf, dirPath + File.separator + fileName);
	}

	/**
	 * 保存文件
	 *
	 * @param buf
	 * @param filePath
	 */
	public static void saveFile(byte[] buf, String filePath) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			String dir = Helper.getPathDir(filePath);

			File dirFile = new File(dir);

			if (!dirFile.exists())
				dirFile.mkdir();

			file = new File(filePath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static byte[] loadFile(String filePath) {
		return getFile2byte(filePath);
	}
	// <<<<<<<<<<<<<<<<<<<<<<文件 路径 相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>HTTP URL 相关>>>>>>>>>>>>>>>>>>>
	/***
	 * android4.0不能再主线程执行，否则报错
	 */
	public static String httpGet(String url, List<BasicNameValuePair> params) throws Exception {
		if (Helper.isEmpty(url))
			throw new Exception("参数错误");

		if (params != null && params.size() > 0) {
			String param = URLEncodedUtils.format(params, "UTF-8");
			if (url.indexOf("?") < 0)
				url += "?";
			url += param;
		}
		System.out.println("=======>url=" + url);
		HttpGet getMethod = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(getMethod); // 发起GET请求
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK)
			return EntityUtils.toString(response.getEntity(), "utf-8");
		else
			throw new Exception("请求失败Code:" + statusCode);
	}

	/***
	 * android4.0不能再主线程执行，否则报错
	 */
	public static String httpPost(String url, List<BasicNameValuePair> params) throws Exception {
		if (Helper.isEmpty(url))
			throw new Exception("参数错误");

		System.out.println("=======>url=" + url);
		HttpPost postMethod = new HttpPost(url);
		if (params != null && params.size() > 0) {
			postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
																				// Entity中

		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK)
			return EntityUtils.toString(response.getEntity(), "utf-8");
		else
			throw new Exception("请求失败Code:" + statusCode);
	}

	public static String getURLFileName(String url) {
		String filename = "";
		if (url.contains("?")) {
			String[] arr = url.split("\\" + "?");
			url = arr[0];
		}

		boolean isok = false;
		// 从UrlConnection中获取文件名称
		try {
			URL myURL = new URL(url);

			URLConnection conn = myURL.openConnection();
			if (conn == null) {
				return null;
			}
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return null;
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return null;
			}

			for (String skey : key) {
				List<String> values = hf.get(skey);
				for (String value : values) {
					String result;
					try {
						result = new String(value.getBytes("ISO-8859-1"), "GBK");
						int location = result.indexOf("filename");
						if (location >= 0) {
							result = result.substring(location + "filename".length());
							filename = result.substring(result.indexOf("=") + 1);
							isok = true;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if (isok) {
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 从路径中获取
		if (filename == null || "".equals(filename)) {
			filename = url.substring(url.lastIndexOf("/") + 1);
		}
		return filename;

	}

	// async

	public static void downloadAsync(final String url, final String filePath, final boolean overwrite) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					download(url, filePath, overwrite);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void download(final String url, String filePath, boolean overwrite) throws Exception {
		Boolean exists1 = new File(filePath).exists();

		if (!overwrite && exists1)
			return;

		byte[] data = null;

		URL url1 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; " +
                ".NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; " +
                ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		conn.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
                "application/x-shockwave-flash, application/xaml+xml, " +
                "application/vnd.ms-xpsdocument, application/x-ms-xbap, " +
                "application/x-ms-application, application/vnd.ms-excel, " +
                "application/vnd.ms-powerpoint, application/msword, */*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Charset", "UTF-8");

		conn.connect();
		int responseCode = conn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK){
			InputStream inStream = conn.getInputStream();
			data = readStream(inStream);
		}

		if (data == null)
			throwEx("获取数据失败");

		if (exists1)
			delFile(filePath);

		saveFile(data, filePath);
	}
	// <<<<<<<<<<<<<<<<<<<<<<HTTP URL 相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>转码>>>>>>>>>>>>>>>>>>>
	/***
	 * utf8转gbk
	 *
	 * @param txt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String utf8ToGBK(String txt) throws UnsupportedEncodingException {
		return new String(txt.getBytes("gbk"), "gbk");
	}

	/***
	 * gbk转utf8
	 *
	 * @param txt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String gbkToUtf8(String txt) throws UnsupportedEncodingException {
		return new String(txt.getBytes("utf-8"), "utf-8");
	}

	public static String jsEscape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String jsUnescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String strURLEncoder(String value){
		try {
			return java.net.URLEncoder.encode(value, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String strURLDecoder(String value){
		try {
			return java.net.URLDecoder.decode(value, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	// <<<<<<<<<<<<<<<<<<<<<<转码<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>加密解密>>>>>>>>>>>>>>>>>>>
	public final static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 16位MD5
	 *
	 * @param sourceStr
	 * @return
	 */
	public final static String md5_16b(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString().substring(8, 24);
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	/**
	  * 获取单个文件的MD5值！
	  * @return
	  */

	 public static String md5File(String filePath) {
		 File file = new File(filePath);

		if (!file.isFile()) {
			return "";
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	 }

	// <<<<<<<<<<<<<<<<<<<<<<加密解密<<<<<<<<<<<<<<<<<<<<

	public static void throwEx(String msg) throws Exception {
		throw new Exception(msg);
	}

	// >>>>>>>>>>>>>>>>>>>>>>反射>>>>>>>>>>>>>>>>>>>
	/***
	 * 反射实例化
	 *
	 * @param clazz
	 * @return
	 */
	public static Object newInstance(Class<?> clazz) {
		if (clazz == null)
			return null;
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFieldValue2String(Class<?> clazz, Object obj, String fieldName) {
		return Helper.obj2Str(getFieldValue(clazz, obj, fieldName));
	}

	public static Object getFieldValue(Class<?> clazz, Object obj, String fieldName) {
		if (obj == null)
			return null;

		try {
			Field field = clazz.getField(fieldName);
			field.setAccessible(true);// 修改访问权限
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获得某个类的所有的公共（public）的字段，包括父类。
	 * @return
	 */
	public static List<String> getFieldNames(Class<?> clazz) {
		List<String> result = new ArrayList<String>();
		if (clazz == null)
			return result;

		try {
			Field fields[] = clazz.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				result.add(field.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段
	 *
	 * @return
	 */
	public static List<String> getDeclaredFieldNames(Class<?> clazz) {
		List<String> result = new ArrayList<String>();
		if (clazz == null)
			return result;

		try {
			Field fields[] = clazz.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);// 修改访问权限
				result.add(field.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object setFieldsValue(Class<?> clazz, Map<String, Object> namesAndValues)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InstantiationException, InvocationTargetException {
		Object bean = clazz.newInstance();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String _fieldSetName = field.getName();
			StringBuilder sb = new StringBuilder();
			sb.append("set").append(_fieldSetName.substring(0, 1).toUpperCase())
					.append(_fieldSetName.substring(1, _fieldSetName.length()));

			Method fieldSetMet = clazz.getMethod(sb.toString(), field.getType());
			if (fieldSetMet == null || namesAndValues.get(_fieldSetName) == null)
				continue;

			Object value = namesAndValues.get(_fieldSetName);
			if (!isEmpty(value)) {
				String valueStr = value.toString();
				String fieldType = field.getType().getSimpleName();
				if ("String".equals(fieldType)) {
					fieldSetMet.invoke(bean, valueStr);
				} else if ("Date".equals(fieldType)) {
					Date temp = parseDate(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
					Integer intval = Integer.parseInt(valueStr);
					fieldSetMet.invoke(bean, intval);
				} else if ("Long".equalsIgnoreCase(fieldType)) {
					Long temp = Long.parseLong(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Double".equalsIgnoreCase(fieldType)) {
					Double temp = Double.parseDouble(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
					Boolean temp = Boolean.parseBoolean(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else {
					System.out.println("not supper type" + fieldType);
				}
			}
		}
		return bean;
	}

	public static void setFieldValue(Object bean, String fieldSetName, Object value) {
		Class<?> cls = bean.getClass();
		try {
			Field field = cls.getDeclaredField(fieldSetName);
			fieldSetName = "set" + fieldSetName.substring(0, 1).toUpperCase()
					+ fieldSetName.substring(1, fieldSetName.length());
			Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
			if (fieldSetMet == null || field == null) {
				return;
			}
			if (null != value && !"".equals(value)) {
				String valueStr = value.toString();
				String fieldType = field.getType().getSimpleName();
				if ("String".equals(fieldType)) {
					fieldSetMet.invoke(bean, valueStr);
				} else if ("Date".equals(fieldType)) {
					Date temp = parseDate(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
					Integer intval = Integer.parseInt(valueStr);
					fieldSetMet.invoke(bean, intval);
				} else if ("Long".equalsIgnoreCase(fieldType)) {
					Long temp = Long.parseLong(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Double".equalsIgnoreCase(fieldType)) {
					Double temp = Double.parseDouble(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
					Boolean temp = Boolean.parseBoolean(valueStr);
					fieldSetMet.invoke(bean, temp);
				} else {
					System.out.println("not supper type" + fieldType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	/***
	 * 属性值复制
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws Exception
	 */
	public static Object copyProperties(Object obj1, Object obj2) throws Exception {
		Method[] method1 = obj1.getClass().getMethods();
		Method[] method2 = obj2.getClass().getMethods();
		String methodName1;
		String methodFix1;
		String methodName2;
		String methodFix2;
		for (int i = 0; i < method1.length; i++) {
			methodName1 = method1[i].getName();
			methodFix1 = methodName1.substring(3, methodName1.length());
			if (methodName1.startsWith("get")) {
				for (int j = 0; j < method2.length; j++) {
					methodName2 = method2[j].getName();
					methodFix2 = methodName2.substring(3, methodName2.length());
					if (methodName2.startsWith("set")) {
						if (methodFix2.equals(methodFix1)) {
							Object[] objs1 = new Object[0];
							Object[] objs2 = new Object[1];
							objs2[0] = method1[i].invoke(obj1, objs1);
							method2[j].invoke(obj2, objs2);
							continue;
						}
					}
				}
			}
		}
		return obj2;
	}

	public static boolean checkSetMet(Class<?> clazz, String fieldSetMet) {
		Method[] methods = clazz.getMethods();
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public static String getSetter(String fieldSetName) {
		return "set" + fieldSetName.substring(0, 1).toUpperCase() + fieldSetName.substring(1, fieldSetName.length());
	}
	// <<<<<<<<<<<<<<<<<<<<<<反射<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>线程相关>>>>>>>>>>>>>>>>>>>
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void postDelayed(final Runnable code, int millisec) {
		setTimeout(code, millisec, false);
	}

	public static void setTimeout(final Runnable code, int millisec) {
		setTimeout(code, millisec, true);
	}

	/**
	 * 定时器 异步线程
	 *
	 * @param code
	 *            new Runnable() {public void run() {public void run() {...}}}
	 * @param millisec
	 *            1秒=1000
	 * @param async
	 *            异步线程
	 */
	public static void setTimeout(final Runnable code, int millisec, Boolean async) {

		if (async) {
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					new Thread(code).start();

					try {
						timer.cancel();

					} catch (Exception ex) {
						System.out.println(ex);
					} finally {

					}
				}
			}, millisec, 1);
		} else {
			// 主线程
			new Handler().postDelayed(code, millisec);
		}
	}

	/**
	 * 定时任务
	 *
	 * @param delay
	 *            延迟开始毫秒
	 * @param millisec
	 *            相隔时间
	 * @return
	 */
	public static Timer createTimer(final TimerTask task, int delay, int millisec) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, delay, millisec);
		return timer;
	}
	// <<<<<<<<<<<<<<<<<<<<<<线程相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>Android 相关>>>>>>>>>>>>>>>>>>>
	public static void toast_2s(Context ctx, String msg) {
		Helper.toast(ctx, msg, 2000);
	}

	public static void toast(Context ctx, String msg, int duration) {
		if (duration <= 0)
			duration = 1000;// 1秒

		Toast t = Toast.makeText(ctx, msg, duration);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	public static Builder alert(Context ctx, String msg) {
		return alert(ctx, msg, null);
	}

	public static Builder alert(Context ctx, String msg, AlertDialog.OnClickListener callback) {
		Builder builder = new Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle("提示");
		if (callback == null)
			callback = new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			};

		builder.setPositiveButton("确认", callback);
		builder.show();
		return builder;
	}

	public static Builder alert(Context ctx, String[] arr, AlertDialog.OnClickListener callback) {
		Builder builder = new Builder(ctx);

		if (callback == null) {
			callback = new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int selectIndex) {

				}
			};
		}

		builder.setItems(arr, callback);
		builder.setTitle("提示");
		builder.setPositiveButton("关闭", null);
		builder.show();
		return builder;
	}

	public static Builder confirm(Context ctx, String msg, AlertDialog.OnClickListener callbackSuccess) {
		Builder builder = new Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle("提示");
		if (callbackSuccess == null)
			callbackSuccess = new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			};

		builder.setPositiveButton("是", callbackSuccess);
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
		return builder;
	}

	public static void sendHandler(Handler handler, int what) {
		sendHandler(handler, what, null, 0, 0);
	}

	public static void sendHandler(Handler handler, int what, Object obj) {
		sendHandler(handler, what, obj, 0, 0);
	}

	public static void sendHandler(Handler handler, int what, Object obj, int arg1, int arg2) {
		Message message = new Message();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		message.obj = obj;
		handler.sendMessage(message);
	}

	/***
	 * 显示软键盘
	 * 
	 * @param ctx
	 * @param view
	 *            放Handler执行，600ms延迟建议
	 */
	public static void showSoftInput(Context ctx, final View view) {
		try {
			InputMethodManager mgr = (InputMethodManager) ctx.getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			try {
				if (view != null) {
					view.setFocusable(true);
					view.requestFocus();
				}
			} catch (Exception ex) {

			}
			mgr.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * 隐藏软键盘
	 * 
	 * @param ctx
	 * @param view
	 *            键盘聚焦的视图
	 */
	public static void hideSoftInput(Context ctx, View view) {
		try {
			if (view == null) {
				return;
			}

			InputMethodManager mgr = (InputMethodManager) ctx.getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * 隐藏或显示软键盘
	 * 
	 * @param ctx
	 * @param view
	 *            键盘聚焦的视图
	 */
	public static void toggleSoftInput(Context ctx, View view) {
		try {
			InputMethodManager mgr = (InputMethodManager) ctx.getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			try {
				if (view != null && !softInputIsOpen(ctx))
					view.requestFocus();
			} catch (Exception ex) {

			}

			mgr.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * 软件键盘是否显示
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean softInputIsOpen(Context ctx) {
		try {
			InputMethodManager mgr = (InputMethodManager) ctx.getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			return mgr.isActive();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static void startActivity(Context ctx, String toActivityPath) {
		Intent intent = new Intent();
		intent.setClassName(ctx, toActivityPath); // com.zy.XYZ
		ctx.startActivity(intent);
	}

	public static void startActivity(Context ctx, Class<?> toActivityClass) {
		Intent intent = new Intent(ctx, toActivityClass); // XYZ.class
		// intent.setComponent(new ComponentName(this, toActivityClass));
		ctx.startActivity(intent);
	}

	public static void startActivityForResult(Context ctx, Class<?> toActivityClass, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(ctx, toActivityClass);
		// Bundle bundle=new Bundle();
		// bundle.putString("key", "value");
		// intent.putExtras(bundle);
		ctx.startActivity(intent, bundle);// 这里采用startActivityForResult来做跳转，此处的0为一个依据，可以写其他的值，但一定要>=0
	}

	/***
	 * 是否有网络连接
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean networkIsConnect(Context ctx) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		return (mNetworkInfo != null && mNetworkInfo.isAvailable());
	}

	/***
	 * 获取root权限
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Process createSuProcess() {
		try {
			File rootUser = new File("/system/xbin/ru");
			if (rootUser.exists()) {
				return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
			} else {
				return Runtime.getRuntime().exec("su");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Process createSuProcess(String cmd) {
		Process process;
		try {
			DataOutputStream os = null;
			process = createSuProcess();
			if (process != null) {
				try {
					os = new DataOutputStream(process.getOutputStream());
					os.writeBytes(cmd + "\n");
					os.writeBytes("exit $?\n");
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return process;
	}

	/**
	 * 修改时钟系统ROOT权限
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	static void requestPermissionAlarm() {
		Process process = createSuProcess("chmod 666 /dev/alarm");
		try {
			if (process != null)
				process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 设置系统时钟
	 * 
	 * @param datetime
	 */
	public static void setSystemAlarm(Calendar datetime) throws Exception {
		requestPermissionAlarm();
		android.os.SystemClock.setCurrentTimeMillis(datetime.getTimeInMillis());
	}

	/***
	 * 获取16位唯一标识码 需要权限
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	 * <uses-permission android:name="android.permission.INTERNET" />
	 * 
	 * @return
	 */
	public static String getUniqueID(Context ctx) {
		String mIMEI = "";
		String mWLAN_MAC = "";
		try {
			TelephonyManager TelephonyMgr = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
			mIMEI = TelephonyMgr.getDeviceId();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		try {
			WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			mWLAN_MAC = wm.getConnectionInfo().getMacAddress();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		System.out.println("====mIMEI=" + mIMEI);
		System.out.println("====mWLAN_MAC=" + mWLAN_MAC);
		System.out.println("====Build.BOARD=" + Build.BOARD);
		System.out.println("====Build.SERIAL=" + Build.SERIAL);
		System.out.println("====Build.MODEL=" + Build.MODEL);

		String mUniqueID = Helper.md5_16b(mIMEI + Build.BOARD + Build.SERIAL + mWLAN_MAC + Build.MODEL).toUpperCase();

		System.out.println("====mUniqueID=" + mUniqueID);
		return mUniqueID;
	}

	/***
	 * 通过反射获取所有的字段信息
	 * 
	 * @return
	 */
	public String getDeviceInfo2() {
		StringBuilder sbBuilder = new StringBuilder();
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				sbBuilder.append("\n" + field.getName() + ":" + field.get(null).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return sbBuilder.toString();
	}

	/***
	 * 获取本机IP
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getLocAddressIPV4(Context ctx) {
		String ipaddress = "";

		try {
			WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			ipaddress = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("本机IP:" + ipaddress);

		return ipaddress;
	}

	/***
	 * 获取IP段位 192.160.0.
	 * 
	 * @param ipv4
	 * @return
	 */
	public static String getLocAddrIndex(String ipv4) {
		if (!ipv4.equals("")) {
			return ipv4.substring(0, ipv4.lastIndexOf(".") + 1);
		}

		return "";
	}

	public static Point getDisplaySize(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		Point size = new Point(dm.widthPixels, dm.heightPixels);
		return size;
	}

	public static void runOnUiThread(android.app.Activity ctx,Runnable method){
		ctx.runOnUiThread(method);
	}
	// <<<<<<<<<<<<<<<<<<<<<<Android 相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>图像相关 相关>>>>>>>>>>>>>>>>>>>

	public static Bitmap compressPic(Bitmap bitmapOrg) {
		return compressPic(bitmapOrg, bitmapOrg.getWidth(), bitmapOrg.getHeight());
	}

	/**
	 * 对图片进行压缩（去除透明度）
	 * 
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmapOrg, int newWidth, int newHeight) {
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		if (newWidth <= 0 || newWidth > 360) {
			newWidth = 360;
			int w1 = bitmapOrg.getWidth();
			int h1 = bitmapOrg.getHeight();
			newHeight = (int) (1.0d * newWidth * (1.0d * w1 / h1));
		}

		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}
	// <<<<<<<<<<<<<<<<<<<<<<图像相关 相关<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>>其他>>>>>>>>>>>>>>>>>>>
	public static String getGUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	// <<<<<<<<<<<<<<<<<<<<<<其他<<<<<<<<<<<<<<<<<<<<

	public static void main(String[] args) {
		System.out.println("Test1");
		// String fmt="'{0}''{2}'";
		// fmt=fmt.replace("'", "''");
		// String a = MessageFormat.format(fmt, 1,2,3);
		// System.out.println(a);

		String a = "001" + "202cb962ac59075b964b07152d234b70" + "ComradeSoft";
		try {
			System.out.println(Helper.md5(Helper.utf8ToGBK(a)));
			System.out.println(Helper.md5(Helper.gbkToUtf8(a)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
