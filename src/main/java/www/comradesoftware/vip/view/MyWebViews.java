package www.comradesoftware.vip.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.apache.http.util.EncodingUtils;
import org.litepal.crud.DataSupport;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wendu.dsbridge.DWebView;
import www.comradesoftware.vip.db.GlobalConfig;
import www.comradesoftware.vip.db.TabList;
import www.comradesoftware.vip.utils.FileUtils;
import www.comradesoftware.vip.api.JsApi;

//DWebView

public class MyWebViews extends FrameLayout {
    private Context context;
    private List<DWebView> mWebViewList = new ArrayList<>();
    private int mShowViewLastIndex = -1;

    public MyWebViews(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        addWabView();
    }

    //获取HTMl内容
    private String getHtmlContent(String path) throws IOException {
        //获得要编辑的html文件的路径
        String filePathName = FileUtils.ACUDATA_PATH(context) + "/page/" + path.toLowerCase() + "/index.htm";
        FileInputStream fis = new FileInputStream(filePathName);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        String res = EncodingUtils.getString(buffer, "UTF-8");
        fis.close();
        return res;
    }

    //设置数量
    @SuppressLint("SetJavaScriptEnabled")
    public void addWabView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        List<TabList> tabs = DataSupport.findAll(TabList.class);
        int size = tabs.size();
        for (int i = 0; i < size; i++) {
            DWebView webView = new DWebView(context);
            webView.setLayoutParams(params);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);//设置js可用(必要)
            settings.setAllowFileAccess(true);// 设置允许访问文件数据(必要)
            settings.setSupportZoom(true);//支持放大网页功能
            settings.setBuiltInZoomControls(true);//支持缩小网页
            webView.addJavascriptObject(new JsApi(context), "redirectTo");//添加API
            DWebView.setWebContentsDebuggingEnabled(true);

            addView(webView);
            mWebViewList.add(webView);


//           方式一：webview.loadUrl
//           获得js和css的所在文件夹
//            String filePathName= "file://"+FileUtils.ACUDATA_PATH(context)+"/page/"+tabs.get(i).getPage().toLowerCase()+"/index.htm";
////            webView.loadUrl(filePathName);

            //方式二：webView.loadDataBaseURL，如果用此方法必须去InitDataService的analysisDefaultJson方法里也得改用方式二拼接路径
            String contentPath = "file://" + FileUtils.ACUDATA_PATH(context) + "/content/";
            try {
                webView.loadDataWithBaseURL(contentPath
                        , getHtmlContent(tabs.get(i).getPage())
//                        ,filePathName
                        , "text/html"
                        , "UTF-8"
                        , "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            webView.setVisibility(INVISIBLE);
        }
    }

    public int getLaunchIndex() {
        List<TabList> tabs = DataSupport.findAll(TabList.class);
        GlobalConfig globalConfig = DataSupport.findLast(GlobalConfig.class);
        int size = tabs.size();
        for (int i = 0; i < size; i++) {
            if (tabs.get(i).getPage().equals(globalConfig.getLaunchPage())) {
                return i;
            }
        }
        return 1;
    }

    public void showView(int index) {
        if (mShowViewLastIndex != -1) {
            mWebViewList.get(mShowViewLastIndex).setVisibility(INVISIBLE);
        }
        mWebViewList.get(index).setVisibility(VISIBLE);
        mShowViewLastIndex = index;
    }

}
