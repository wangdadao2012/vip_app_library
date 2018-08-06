package www.comradesoftware.vip.api;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import wendu.dsbridge.CompletionHandler;
import www.comradesoftware.vip.activities.SubWebActivity;
import www.comradesoftware.vip.utils.ApkInfoUtil;
import www.comradesoftware.vip.utils.ToastUtil;

/**
 * native注册函数，供js调用
 * @author summer
 */

public class JsApi {
    Context context;
    public JsApi(Context context){
        this.context=context;
    }
    //同步
    @JavascriptInterface
    public String testSyn(Object msg)  {
        return msg + "［syn call］";
    }

    //异步回调
    @JavascriptInterface
    public void testAsyn(Object msg, CompletionHandler<String> handler){
        handler.complete(msg+" [ asyn call]");
    }




    /**
     * 外部地址,打开二层视图 App.redirectTo({ Url: "http://www.baidu.com" });
     * 参数：{ Url: "http://www.baidu.com"}
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void redirectTo(Object msg, CompletionHandler<String> handler){
        Bundle data=new Bundle();
        data.putString("URL",msg.toString());
        SubWebActivity.startSubWebActivity((Activity) context,data);
        handler.complete();
    }

    /**
     * 参数 { Page:"Main",Close:true } Close跳转并关闭当前WebView(如果可以关闭的话)
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void navigateTo(Object msg, CompletionHandler<String> handler){
        handler.complete();
    }

    /**
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void navigateBack(Object msg, CompletionHandler<String> handler){
        handler.complete();
    }

    /**
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void getSystemInfo(Object msg, CompletionHandler<String> handler){
        handler.complete(ApkInfoUtil.getSysInfo(context));
    }

}
