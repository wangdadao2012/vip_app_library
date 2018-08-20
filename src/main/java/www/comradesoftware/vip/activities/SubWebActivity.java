package www.comradesoftware.vip.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import wendu.dsbridge.DWebView;
import www.comradesoftware.vip.R;
import www.comradesoftware.vip.MyDialog.MyAlertDialog;
import www.comradesoftware.vip.utils.ToastUtil;
import www.comradesoftware.vip.view.MyToolbar;

public class SubWebActivity extends BaseActivity implements MyToolbar.OnItemClickListener{
    private DWebView mDWebView;
    private MyToolbar mToolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_web);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mToolbar=findViewById(R.id.toolbar);
        mToolbar.setOnItemClickListener(this);
        progressBar=findViewById(R.id.progressBar);

        mDWebView =findViewById(R.id.dWebView);
        mDWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                showProgress(newProgress);
            }
        });

        mDWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //用javascript隐藏系统定义的404页面信息
                String data = "页面未找到！";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

        mDWebView.loadUrl(getUrl());
        WebSettings settings = mDWebView.getSettings();
        settings.setJavaScriptEnabled(true);//设置js可用(必要)
        settings.setAllowFileAccess(true);// 设置允许访问文件数据(必要)
        settings.setSupportZoom(true);//支持放大网页功能
        settings.setBuiltInZoomControls(true);//支持缩小网页
        settings.setSupportZoom(true);
    }

    private String getUrl(){
        Intent intent=getIntent();
        Bundle data = intent.getExtras();
        if (data != null) {
            String url = data.getString("URL");
            if (!TextUtils.isEmpty(url))
                return url;
        }
        return "https://www.baidu.com/";
    }

    private void showProgress(int progress){
        if(progress==100){
            progressBar.setVisibility(View.INVISIBLE);//加载完网页进度条消失
        }
        else{
            progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
            progressBar.setProgress(progress);//设置进度值
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(View v, int index) {
        switch (index){
            case 0:
                goBack();
                break;
            case 1:
                ToastUtil.showToast(this,"点击了标题");
                break;
            case 2:
                mDWebView.reload();
                break;
            case 3:
                finishDialog();
                break;
        }
    }

    private void finishDialog() {
        Dialog dialog = MyAlertDialog.createConfirmDialog(this, "提示","要关闭本页面吗", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.show();
    }

    private void goBack(){
        if (mDWebView.canGoBack())
            mDWebView.goBack();
        else
            finishDialog();
    }

    public static void startSubWebActivity(Activity activity,Bundle data) {
        Intent intent = new Intent(activity, SubWebActivity.class);
        intent.putExtras(data);
//        activity.overridePendingTransition(R.anim.stand,R.anim.splash);
        activity.startActivity(intent);
//        activity.finish();
    }
}
