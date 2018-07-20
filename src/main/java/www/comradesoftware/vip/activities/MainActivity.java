package www.comradesoftware.vip.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;
import org.litepal.crud.DataSupport;

import java.io.FileInputStream;
import java.io.IOException;

import wendu.dsbridge.DWebView;
import www.comradesoftware.vip.R;
import www.comradesoftware.vip.db.GlobalConfig;
import www.comradesoftware.vip.db.PageConfig;
import www.comradesoftware.vip.db.TabBar;
import www.comradesoftware.vip.utils.FileUtils;
import www.comradesoftware.vip.utils.ToastUtil;
import www.comradesoftware.vip.view.MyNavigationView;

//首页
public class MainActivity extends ActivityBase implements MyNavigationView.OnTabClickListener {

    private MyNavigationView mNavigationView;
    private DWebView mWbvHome;
    private DWebView mWbvUser;

    private final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(){
        mNavigationView =findViewById(R.id.navigation);
        mNavigationView.addTab("首页",R.string.ic_home);
        mNavigationView.addTab("我的",R.string.ic_user);
        mNavigationView.setTypefacePath("fonts/app.ttf");
        mNavigationView.setScaleFromXY(0.8f);
        mNavigationView.create();
        mNavigationView.setOnTabClickListener(this);

        mWbvHome=findViewById(R.id.wbvHome);
        mWbvUser=findViewById(R.id.wbvUser);

        mWbvHome.setWebViewClient(new WebViewClient());
        mWbvUser.setWebViewClient(new WebViewClient());

        String htmlContent="about:blank";
        String launchPage=DataSupport.findLast(GlobalConfig.class).getLaunchPage();
        try {
            htmlContent=getHtmlContent(launchPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mWbvHome.getSettings().setJavaScriptEnabled(true);//设置js可用
        mWbvUser.getSettings().setJavaScriptEnabled(true);//设置js可用
        switch (launchPage){
            case "Main":
                mNavigationView.setTabSelected(0,true);
                mWbvHome.loadDataWithBaseURL("about:blank"
                        , htmlContent
                        , "text/html"
                        , "UTF-8"
                        ,"");
                break;
            case "My":
                mNavigationView.setTabSelected(1,true);
                mWbvUser.loadDataWithBaseURL("about:blank"
                        , htmlContent
                        , "text/html"
                        , "UTF-8"
                        ,"");
                mWbvHome.reload();
                break;
        }
    }

    private String getHtmlContent(String launchPage) throws IOException {
        //          获得要编辑的html文件的路径
        String filePathName= FileUtils.ACUDATA_PATH(this)+"/page/"+launchPage.toLowerCase()+"/index.htm";
        FileInputStream fis = new FileInputStream(filePathName);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        String res = EncodingUtils.getString(buffer, "UTF-8");
        fis.close();
        return res;
    }

    private void showWebView(int index){
        switch (index){
            case 0:
                mWbvHome.setVisibility(View.VISIBLE);
                mWbvUser.setVisibility(View.GONE);
                break;
            case 1:
                mWbvHome.setVisibility(View.GONE);
                mWbvUser.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.stand,R.anim.splash);
    }

    @Override
    public void onTabClick(View v, int index) {
        switch (index){
            case 0:
                showWebView(0);
                break;
            case 1:
                showWebView(1);
                break;
        }
    }
}
