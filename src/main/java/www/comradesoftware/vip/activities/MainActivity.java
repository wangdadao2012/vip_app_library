package www.comradesoftware.vip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import org.litepal.crud.DataSupport;

import java.util.List;

import www.comradesoftware.vip.R;
import www.comradesoftware.vip.db.PageConfig;
import www.comradesoftware.vip.db.TabBar;
import www.comradesoftware.vip.db.TabList;
import www.comradesoftware.vip.utils.ApkInfoUtil;
import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.view.MyNavigationView;
import www.comradesoftware.vip.view.MyWebViews;

//首页
public class MainActivity extends BaseActivity implements MyNavigationView.OnTabClickListener {

    private MyNavigationView mNavigationView;
    private MyWebViews mMyWebViews;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        LogUtil.e("MainActivity","JS:"+ApkInfoUtil.getSysInfo(this));
    }

    private void initView() {
        mNavigationView = findViewById(R.id.navigation);
        PageConfig pageConfig = DataSupport.findLast(PageConfig.class);
        TabBar tabBar = DataSupport.findLast(TabBar.class);
        List<TabList> tabList = DataSupport.findAll(TabList.class);

        for (TabList tab : tabList) {
            mNavigationView.addTab(tab.getText(), Integer.parseInt(tab.getIconXe()));
        }
        mNavigationView.setAssetsTypefacePath("fonts/app.ttf");
        mNavigationView.setScaleFromXY(0.8f);
        mNavigationView.create();

        mNavigationView.setTabColor(pageConfig.getNavBarTextColor(), tabBar.getSelectColor());
        mNavigationView.setTabBgColor(pageConfig.getNavBarBgColor(), pageConfig.getNavBarBgColor());
        mNavigationView.setBackgroundColor(pageConfig.getNavBarBgColor());
        mNavigationView.setOnTabClickListener(this);

        mMyWebViews = findViewById(R.id.myWebViews);
        mNavigationView.setTabSelected(mMyWebViews.getLaunchIndex(), true);
    }

    /**
     *
     规则1：一个内部页面打开对应一个新的WebView
     规则2：所有内部页面的页面跳转都跳转，内部页面要跳转必须使用 navigateTo 和  redirectTo 接口进行操作

     navigateTo接口：
     只能跳到内部页，一个内部页面打开对应一个新的WebView，已存在的就显示，不存在就创建，加入队列

     redirectTo 接口：
     打开SubWebView第二层视图，二层视图包含标题栏，标题栏有个 关闭按钮 ， 后退按钮，标题，跳转都在当前webview完成

     navigateBack接口：
     如果页面是tab列表和preload列表的页面，则保持webview的生命，如果是preload页面既隐藏，返回显示上一个页面，如果是tab页面就不用做动作，因为伊已经是顶层

     * @param activity
     */
    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
//        activity.overridePendingTransition(R.anim.stand,R.anim.splash);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onTabClick(View v, int index) {
        mMyWebViews.showView(index);
    }
}
