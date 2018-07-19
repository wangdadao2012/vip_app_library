package www.comradesoftware.vip.utils;

import android.webkit.JavascriptInterface;

import wendu.dsbridge.CompletionHandler;

/**
 * native注册函数，供js调用
 * @author summer
 */

public class JsApi {
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
}
