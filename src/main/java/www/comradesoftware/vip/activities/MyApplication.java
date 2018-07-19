package www.comradesoftware.vip.activities;
import android.content.Context;

import org.litepal.LitePal;

import www.comradesoftware.vip.Global;

public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Global.initApp(this);
    }
}
