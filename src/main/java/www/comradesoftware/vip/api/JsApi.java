package www.comradesoftware.vip.api;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.okhttplib.HttpInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import wendu.dsbridge.CompletionHandler;
import www.comradesoftware.vip.Global;
import www.comradesoftware.vip.activities.MainActivity;
import www.comradesoftware.vip.activities.SubWebActivity;
import www.comradesoftware.vip.utils.ApkInfoUtil;
import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.utils.ToastUtil;
import www.comradesoftware.vip.utils.https.ApiParam;
import www.comradesoftware.vip.utils.https.HttpHelp;

/**
 * native注册函数，供js调用
 * @author summer
 */

public class JsApi {
    private Context context;
    public JsApi(Context context){
        this.context=context;
    }
//    //同步
//    @JavascriptInterface
//    public String testSyn(Object msg)  {
//        return msg + "［syn call］";
//    }
//
//    //异步回调
//    @JavascriptInterface
//    public void testAsyn(Object msg, CompletionHandler<String> handler){
//        handler.complete(msg+" [ asyn call]");
//    }
    /**
     * 外部地址,打开二层视图 App.redirectTo({ Url: "http://www.baidu.com" });
     * 参数：{ Url: "http://www.baidu.com"}
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void redirectTo(Object msg, CompletionHandler<String> handler){
        ToastUtil.showToast(context,"redirectTo");
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
        ToastUtil.showToast(context,"navigateTo");
        try {
            JSONObject jsonObject=new JSONObject(msg.toString());
            String page=jsonObject.getString("Page");
            boolean close=jsonObject.getBoolean("Close");
            ((MainActivity)context).setPreloadViewPageTo(page,close);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.complete();
    }

    /**
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void navigateBack(Object msg, CompletionHandler<String> handler){
        ToastUtil.showToast(context,"navigateBack");
        ((MainActivity)context).setPreloadViewPageBack();
        handler.complete();
    }

    /**
     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void getSystemInfo(Object msg, CompletionHandler<String> handler){
        ToastUtil.showToast(context,"getSystemInfo");
        handler.complete(ApkInfoUtil.getSysInfo(context));
    }

    /**
     * 这个是服务器访问接口，web通过这个接口

     接口参数包含：Controller,Method,Url,Param
     判断Url是否为空，不为空直接使用Url通信，为空，使用Controller,Method拼接url，拼接的公式是根据不同客户而变动，做成可配置
     默认拼接公式为：
     [NSString stringWithFormat:@"%@/WxMiniApp/api/%@/%@/%llu",[self getDomain],controller,method,ticks];
     通讯任何结果都要返回给web页面

     测试地址为：
     http://app.1m1m.cc/WxMiniApp/api/ProductList/app_CategoryList/?Data={EntID:%22fbf628a1c1ca43a4bfd675a76773990d%22}

     * @param msg 参数
     * @param handler handler.complete(t);
     */
    @JavascriptInterface
    public void api(Object msg, CompletionHandler<String> handler){
        handler.complete(getApiData(getUrl(msg)));
    }

    public String getUrl(Object msg){
        try {
            JSONObject jsonObject=new JSONObject(msg.toString());
            String Url=jsonObject.getString("Url");
//            判断Url是否为空，不为空直接使用Url通信，为空，使用Controller,Method拼接url，拼接的公式是根据不同客户而变动，做成可配置
            if (TextUtils.isEmpty(Url)){
                String controller=jsonObject.getString("Controller");
                String method=jsonObject.getString("Method");
                String param=jsonObject.getString("Param");
//                 [NSString stringWithFormat:@"%@/WxMiniApp/api/%@/%@/%llu",[self getDomain],controller,method,ticks];
//                http://app.1m1m.cc/WxMiniApp/api/ProductList/app_CategoryList/?Data={EntID:%22fbf628a1c1ca43a4bfd675a76773990d%22}
                return String.format("WxMiniApp/api/%s/%s",controller,method);
            }else {
                return Url;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //获取api更新信息
    public String getApiData(String url) {
        final String[] result = new String[1];
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("EntID", "fbf628a1c1ca43a4bfd675a76773990d");
        HttpHelp httpHelp = new HttpHelp(context);
        ApiParam param = httpHelp.getForApiParamSign(map);
        httpHelp.doAsyncPost(url, param, false, null);
        httpHelp.setHttpResponseListener(new HttpHelp.HttpResponseListener() {
            @Override
            public void httpOnFailure(HttpInfo info) {
                result[0] = info.getRetDetail();
                LogUtil.e("--requestApiData-->", "异步请求失败:" + result[0]);
            }
            @Override
            public void httpOnSuccess(HttpInfo info) {
                result[0] = info.getRetDetail();
                LogUtil.e("--requestApiData-->", "异步请求成功:" + result[0]);
            }
        });
        return result[0];
    }
}
