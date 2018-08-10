package www.comradesoftware.vip.activities;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import www.comradesoftware.vip.R;


public class BaseActivity extends AppCompatActivity {
    public static final int Handler_What_ShowLoading = 20010;
    public static final int Handler_What_CloseLoading = 20011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //    沉浸式状态栏
    protected void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 在代码中设置透明状态栏的方法也很简单，思路就是首先通过 WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS设置
     * 当前window为透明状态栏，这样标题栏就可以占据状态栏了，但是会出现布局中的内容和状态栏的内容重叠的问题，为了解决这
     * 个问题，我们应该获得状态栏的高度，然后设置标题栏的paddingTop为状态栏的高度，这样就可以实现透明效果的标题栏的
     * @param view
     */
    protected void setImmerseLayout(View view) {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }
}
