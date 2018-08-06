package www.comradesoftware.vip.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;

import www.comradesoftware.vip.db.GlobalConfig;

import static android.content.Context.TELEPHONY_SERVICE;

public class ApkInfoUtil {
    /**
     * 获取应用程序名称
     */
    private static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    private static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取当前本地apk的版本
     */
    private static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //TODO:是否模拟器
    private static boolean isSIMULATOR(Context context) {
        return true;
    }

    /**
     *获取状态栏高度
     * @return
     */
    private static int getStatusBarHeigth(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机IMSI号
     */
    private static String getIMSI(Context context) {
        String serial = null;
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    public static String getSysInfo(Context context){
        JSONObject object =new JSONObject();
        GlobalConfig globalConfig = DataSupport.findLast(GlobalConfig.class);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        try {
            object.put("CFBundleDisplayName",getAppName(context));
            Log.e("ApkInfo","CFBundleDisplayName:"+getAppName(context));

            object.put("CFBundleIdentifier",context.getPackageName());
            Log.e("ApkInfo","CFBundleIdentifier:"+context.getPackageName());

            object.put("CFBundleShortVersionString",getVerName(context));
            Log.e("ApkInfo","CFBundleShortVersionString:"+getVerName(context));

            object.put("CFBundleVersion",getVersionCode(context));
            Log.e("ApkInfo","CFBundleVersion:"+getVersionCode(context));

            object.put("CFBundleInfoDictionaryVersion",android.os.Build.VERSION.RELEASE);
            Log.e("ApkInfo","CFBundleInfoDictionaryVersion:"+android.os.Build.VERSION.RELEASE);

            object.put("PackageVersion",globalConfig.getVersion());
            Log.e("ApkInfo","PackageVersion:"+globalConfig.getVersion());

            object.put("PackageBuild",globalConfig.getBuild());
            Log.e("ApkInfo","PackageBuild:"+globalConfig.getBuild());

            object.put("LaunchPage",globalConfig.getLaunchPage());
            Log.e("ApkInfo","LaunchPage:"+globalConfig.getLaunchPage());

            object.put("RootPath",context.getPackageResourcePath());
            Log.e("ApkInfo","RootPath:"+context.getPackageResourcePath());

            object.put("DocumentsPath",FileUtils.ACUDATA_PATH(context));
            Log.e("ApkInfo","DocumentsPath:"+FileUtils.ACUDATA_PATH(context));

            object.put("UIDeviceWidth",w_screen);
            Log.e("ApkInfo","UIDeviceWidth:"+w_screen);

            object.put("UIDeviceHeight",h_screen);
            Log.e("ApkInfo","UIDeviceHeight:"+h_screen);

            object.put("isSIMULATOR",isSIMULATOR(context));
            Log.e("ApkInfo","isSIMULATOR:"+isSIMULATOR(context));

            object.put("分辨率width",w_screen);
            Log.e("ApkInfo","分辨率width:"+w_screen);

            object.put("分辨率height",h_screen);
            Log.e("ApkInfo","分辨率height:"+h_screen);

            object.put("状态栏高度",getStatusBarHeigth(context));
            Log.e("ApkInfo","状态栏高度:"+getStatusBarHeigth(context));

            object.put("系统","Android");
            Log.e("ApkInfo","系统:"+"Android");

            object.put("版本",android.os.Build.VERSION.RELEASE);
            Log.e("ApkInfo","版本:"+android.os.Build.VERSION.RELEASE);

            object.put("设备类型",android.os.Build.MODEL);
            Log.e("ApkInfo","设备类型:"+android.os.Build.MODEL);

            object.put("序列号",getIMSI(context));
            Log.e("ApkInfo","序列号:"+getIMSI(context));
        }catch (Exception e){
            e.getMessage();
        }
        return object.toString();
    }

}
