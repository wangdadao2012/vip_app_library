package www.comradesoftware.vip;

import www.comradesoftware.vip.activities.BaseActivity;
import www.comradesoftware.vip.activities.MyApplication;
import www.comradesoftware.vip.i.ISetting;
import www.comradesoftware.vip.utils.https.LogEx;

public class Global {

    public static final Boolean DEBUG = true;

    public static final String DEBUG_Http = "http://192.168.0.188/";

    public static final String DirImg = "img/";

    public static String AppID = "app";
    public static String AppName = "ERP会员";

    public static String Domain = "http://app.1m1m.cc/";
    // "http://192.168.0.188:84/";

    public static String UrlUpgradeApk = "http://cloudpower.comradesoft.com/comradepos/"
            + "EMaiBao/api/apk/upgrade/xa?t=1";

    public static LogEx log = new LogEx("Global");
    public static LogEx log2 = new LogEx("Global2");

    public static MyApplication Application = null;

    public static String UserNo = "";
    public static String UniqueID = "";

    public static BaseActivity currentCtx = null;
    public static Boolean Inited = false;

    public static String AppVer = "";

    public static void initApp(MyApplication ctx) {
        Application = ctx;
        try {
            Class aClass = Class.forName("www.comradesoftware.vip001.Setting");
            ISetting setting = (ISetting) aClass.newInstance();
            if (setting != null) {
                Global.AppID = setting.getAppID();
                Global.AppName = setting.getAppName();
                Global.Domain = setting.getDomain();
            }
        } catch (Exception ex) {
            new LogEx().e(ex);
        }
//        if (DEBUG)
//            Domain = "http://192.168.0.188:84/";
    }
}
