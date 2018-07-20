package www.comradesoftware.vip.activities;
import android.content.Context;

import org.litepal.LitePal;

import www.comradesoftware.vip.Global;

public class MyApplication extends android.app.Application {
    public static boolean SKIP_WELCOME;
    @Override
    public void onCreate() {
        SKIP_WELCOME = false;
        super.onCreate();
        LitePal.initialize(this);
        Global.initApp(this);
    }
}
