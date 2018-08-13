package www.comradesoftware.vip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import www.comradesoftware.vip.R;
import www.comradesoftware.vip.api.JsApi;
import www.comradesoftware.vip.db.PageConfig;
import www.comradesoftware.vip.db.TabBar;
import www.comradesoftware.vip.db.TabList;
import www.comradesoftware.vip.utils.ApkInfoUtil;
import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.utils.ToastUtil;
import www.comradesoftware.vip.view.MyNavigationView;
import www.comradesoftware.vip.view.MyWebViews;

//首页
public class MainActivity extends BaseActivity implements MyNavigationView.OnTabClickListener {

    private MyNavigationView mNavigationView;
    private Toolbar toolbar;
    private MyWebViews mMyWebViews;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        JsApi jsApi=new JsApi(this);
        JSONObject jsonObject =new JSONObject();
//          [NSString stringWithFormat:@"%@/WxMiniApp/api/%@/%@/%llu",[self getDomain],controller,method,ticks];
//        http://app.1m1m.cc/WxMiniApp/api/ProductList/app_CategoryList/?Data={EntID:%22fbf628a1c1ca43a4bfd675a76773990d%22}
        try {
            jsonObject.put("Controller","ProductList");
            jsonObject.put("Method","app_CategoryList");
            jsonObject.put("Param","EntID");
            jsonObject.put("Url","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url=jsApi.getUrl(jsonObject);
        jsApi.getApiData(url);
    }

    private void initView() {
        mNavigationView = findViewById(R.id.navigation);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar跟menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getActionBar();
        return true;
    }

    Toolbar.OnMenuItemClickListener onMenuItemClick=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String msg = "";
            int i = item.getItemId();
            if (i == R.id.action_edit) {
                msg += "Click edit";

            } else if (i == R.id.action_share) {
                msg += "Click share";

            } else if (i == R.id.action_settings) {
                msg += "Click setting";
            }
            if(!msg.equals("")) {
                ToastUtil.showToast(MainActivity.this, msg);
            }
            return true;
        }
    };

    /**
     规则1：一个内部页面打开对应一个新的WebView
     规则2：所有内部页面的页面跳转都跳转，内部页面要跳转必须使用 navigateTo 和  redirectTo 接口进行操作

     navigateTo接口：
     只能跳到内部页，一个内部页面打开对应一个新的WebView，已存在的就显示，不存在就创建，加入队列

     build.json preload元素，但不存在
     "preload":["Main","My","Login","Cart"]
     进入【主界面】后，加载【启动页】后，后台线程根据preload创建对应webview并且隐藏

     * @param activity activity
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
