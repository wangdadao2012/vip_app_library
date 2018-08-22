package www.comradesoftware.vip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import www.comradesoftware.vip.R;
import www.comradesoftware.vip.db.PageConfig;
import www.comradesoftware.vip.db.TabBar;
import www.comradesoftware.vip.db.TabList;
import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.view.MyNavigationView;
import www.comradesoftware.vip.view.MyWebViews;
import www.comradesoftware.vip.view.PreloadWbViewPage;

//首页
public class MainActivity extends BaseActivity implements MyNavigationView.OnTabClickListener {

    private MyNavigationView mNavigationView;
    private Toolbar toolbar;
    private MyWebViews mMyWebViews;
    private PreloadWbViewPage mPreloadViewPage;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        JsApi jsApi=new JsApi(this);
//        JSONObject jsonObject =new JSONObject();
////          [NSString stringWithFormat:@"%@/WxMiniApp/api/%@/%@/%llu",[self getDomain],controller,method,ticks];
////        http://app.1m1m.cc/WxMiniApp/api/ProductList/app_CategoryList/?Data={EntID:%22fbf628a1c1ca43a4bfd675a76773990d%22}
//        try {
//            jsonObject.put("Controller","ProductList");
//            jsonObject.put("Method","app_CategoryList");
//            jsonObject.put("Param","EntID");
//            jsonObject.put("Url","");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String url=jsApi.getUrl(jsonObject);
//        jsApi.getApiData(url);
    }

    private void initView() {
        mNavigationView = findViewById(R.id.navigation);
        mMyWebViews = findViewById(R.id.myWebViews);
        toolbar=findViewById(R.id.toolbar);
        mPreloadViewPage =findViewById(R.id.preloadViewPage);

        setSupportActionBar(toolbar);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        PageConfig pageConfig = LitePal.findLast(PageConfig.class);
        TabBar tabBar = LitePal.findLast(TabBar.class);
        List<TabList> tabList = LitePal.findAll(TabList.class);

        int[] iconXe =new int[]{607,608,609};
        int i=0;
        for (TabList tab : tabList) {
            LogUtil.e("TabList",tab.getIconXe());

            mNavigationView.addTab(tab.getText(),iconXe[i]);
//            mNavigationView.addTab(tab.getText(),Integer.parseInt(tab.getIconXe()));
            i++;
        }

        mNavigationView.setAssetsTypefacePath("fonts/app.ttf");
        mNavigationView.setScaleFromXY(0.8f);
        mNavigationView.create();

//        mNavigationView.setTabColor(pageConfig.getNavBarTextColor(), tabBar.getSelectColor());
//        mNavigationView.setTabBgColor(pageConfig.getNavBarBgColor(), pageConfig.getNavBarBgColor());
//        mNavigationView.setBackgroundColor(pageConfig.getNavBarBgColor());
        mNavigationView.setOnTabClickListener(this);
        mNavigationView.setTabSelected(mMyWebViews.getLaunchIndex(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar跟menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    Toolbar.OnMenuItemClickListener onMenuItemClick=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.action_menu0) {
                mPreloadViewPage.setPreloadViewPageBack();
            } else if (i == R.id.action_menu1) {
                mPreloadViewPage.setPreloadViewPageTo("Main",false);
            } else if (i == R.id.action_menu2) {
                mPreloadViewPage. setPreloadViewPageTo("My",true);
            }
            else if (i == R.id.action_menu3) {
                mPreloadViewPage.setPreloadViewPageTo("Login",false);
            }
            else if (i == R.id.action_menu4) {
                mPreloadViewPage.setPreloadViewPageTo("Cart",false);
            }
            else if (i == R.id.action_menu5) {
                Bundle bundle=new Bundle();
                bundle.putString("URL","javascript:document.body.innerHTML=\"" + "测试页面哦" + "\"");
                SubWebActivity.startSubWebActivity(MainActivity.this,bundle);
            }
            return true;
        }
    };

    @Override
    public void onTabClick(View v, int index) {
        mMyWebViews.showView(index);
        mMyWebViews.setVisibility(View.VISIBLE);
        mPreloadViewPage.setVisibility(View.GONE);
    }

    public void setPreloadViewPageTo(String page,boolean close){
        mPreloadViewPage.setPreloadViewPageTo(page,close);
    }

    public void setPreloadViewPageBack(){
        mPreloadViewPage.setPreloadViewPageBack();
    }

    public void setMyWebViewsVisibility(int visibility){
        mMyWebViews.setVisibility(visibility);
    }


    /**
     * @param activity activity
     */
    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
//        activity.overridePendingTransition(R.anim.stand,R.anim.splash);
        activity.startActivity(intent);
        activity.finish();
    }
}
