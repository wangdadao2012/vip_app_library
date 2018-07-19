package www.comradesoftware.vip.utils.https;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.callback.Callback;
import com.okhttplib.callback.ProgressCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import www.comradesoftware.vip.Global;
import www.comradesoftware.vip.MyDialog.CustomProgressDialog;
import www.comradesoftware.vip.R;

import static www.comradesoftware.vip.activities.ActivityBase.Handler_What_CloseLoading;
import static www.comradesoftware.vip.activities.ActivityBase.Handler_What_ShowLoading;

/**
 * 网络访问辅助类
 * Created by Huan on 2017/9/14.
 */

public class HttpHelp {
    //    public static String app_host = "http://192.168.0.188:8080/";
    private HttpResponseListener httpResponseListener;
    private httpDownLoadFileListener httpDownLoadFileListener;
    private CustomProgressDialog dialog1 = null;
    private String progressText;
    private Context mContext;
    private DownloadFileInfo fileInfo;
    private String url;
    private String saveDir;
    private String saveName;

    public interface HttpResponseListener {
        void httpOnFailure(HttpInfo info) throws IOException;

        void httpOnSuccess(HttpInfo info) throws IOException;


    }

    public interface httpDownLoadFileListener{
        void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done);
        void httpOnSuccess(String filePath, HttpInfo info,String downloadStatus) ;
        void httpOnFailure(String filePath, HttpInfo info);
    }

    public void sethttpDownLoadFileListener(httpDownLoadFileListener httpDownLoadFileListener) {
        this.httpDownLoadFileListener = httpDownLoadFileListener;
    }

    public void setHttpResponseListener(HttpResponseListener httpResponseListener) {
        this.httpResponseListener = httpResponseListener;
    }




    public HttpHelp(Context context) {
        mContext = context;
    }


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Handler_What_ShowLoading:
                    showLoading(msg);
                    break;
                case Handler_What_CloseLoading:
                    if (dialog1 != null) {
                        dialog1.dismiss();
                        dialog1 = null;
                    }
                    break;
            }
        }
    };

    protected void showLoading(Message msg) {
        if (dialog1 != null)
            return;
        String content=Helper.obj2Str(msg.obj);
        dialog1 = new CustomProgressDialog(mContext,content ,R.drawable.loading_small);
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.9f;
        window.setAttributes(lp);
//        dialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置风格为圆形进度条
        dialog1.show();
//        点击对话框外的对话框地方消失
        dialog1.setCanceledOnTouchOutside(true);
//        是否可以 回退键取消
        dialog1.setCancelable(true);
    }

    public String sign(ApiParam param) {
        List<String> list = new ArrayList<String>();
        list.add("6666666666666666666666666");
        list.add(param.getNonceStr());
        list.add(param.getData());
        list.add(param.getSignType());
//		Helper.sortList(list);
        String key = "";
        for (String item : list) {
            key += item;
        }
        param.setSign(Helper.md5(key));
        return JsonUtil.toJson(param);
    }

    public ApiParam getForApiParamSign(Map<String, String> map) {
        ApiParam param = new ApiParam();
        param.setSignType("");
        param.setDataFromObj(map);
        List<String> list = new ArrayList<String>();
        list.add("6666666666666666666666666");
        list.add(param.getNonceStr());
        list.add(param.getData());
        list.add(param.getSignType());
        String key = "";
        for (String item : list) {
            key += item;
        }
        param.setSign(Helper.md5(key));

        return param;
    }

    /**
     * 普通的post异步请求
     *
     * @param url
     * @param param
     */
    public void doAsyncPost(final String url, ApiParam param, final boolean isShowProgressDiaLog, String progressText) {
        if (mContext!=null){
            if (isShowProgressDiaLog){
                Helper.sendHandler(handler, Handler_What_ShowLoading,progressText);
            }
        }
        Log.d("--异步请求Url-->", "Url= " + Global.Domain + url);
        OkHttpUtil.getDefault(mContext).doAsync(
                HttpInfo.Builder()
                        .setUrl(Global.Domain + url)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addParam("NonceStr", param.getNonceStr())//添加接口参数
                        .addParam("Sign", param.getSign())
                        .addParam("SignType", param.getSignType())
                        .addParam("Data", param.getData())
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        httpResponseListener.httpOnFailure(info);
                        if (mContext!=null){
                            if (isShowProgressDiaLog){
                                Helper.sendHandler(handler, Handler_What_CloseLoading);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        httpResponseListener.httpOnSuccess(info);
                        if (mContext!=null){
                            if (isShowProgressDiaLog){
                                Helper.sendHandler(handler, Handler_What_CloseLoading);
                            }
                        }
                    }
                });
    }

    public void doAsyncGet(String url, Map<String, String> param) {
        Set<String> keys = param.keySet();
        StringBuilder getParams = new StringBuilder();
        for (String key : keys) {
            getParams.append(key + "=" + param.get(key) + "&");
        }
        String paramsStr = getParams.toString();
        paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
        url = Global.Domain + url + "?" + paramsStr;
        Log.e("--异步get请求-->", "url= " + url);
        OkHttpUtil.getDefault(mContext).doGetAsync(
                HttpInfo.Builder().setUrl(url).build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        httpResponseListener.httpOnFailure(info);
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        httpResponseListener.httpOnSuccess(info);
                    }
                });
    }

    public String getApiResult(String result) throws Exception {
        ApiResult apiResult = (ApiResult) JsonUtil.toClass(result, ApiResult.class);
        String res = "";
        if (apiResult.Result.equalsIgnoreCase("success")) {

        } else if (apiResult.Result.equalsIgnoreCase("error")) {

        } else {
            res = "未知错误！";
        }
        return res;
    }

    public void doAsyncPostFile(String url, ApiParam param, String path) {
        HttpInfo info = HttpInfo.Builder()
                .setUrl(url)
                .addHead("MimeType", "image/x-png")
                .addParam("NonceStr", param.getNonceStr())//添加接口参数
                .addParam("Sign", param.getSign())
                .addParam("SignType", param.getSignType())
                .addParam("Data", param.getData())
                .addUploadFile("File1", path, new ProgressCallback() {
                    @Override
                    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
//                        uploadProgress.setProgress(percent);
//                        showRecordStateView("send");
//                        httpResponseListener.httpOnFailure(percent);
                        Log.e("上传中...", "上传进度：" + percent);
                    }

                    @Override
                    public void onResponseMain(String filePath, HttpInfo info) {
//                        tvResult.setText(info.getRetDetail());
                        Log.e("上传结果==>", "res：" + info.getRetDetail());
                        try {
                            JSONObject jsonObject = new JSONObject(info.getRetDetail());
                            if (jsonObject.getString("Result").equals("success")) {
                            }
                        } catch (Exception e) {
                            Log.e("解析上传结果异常==>", "res：" + e.getMessage());
                        }
                    }
                })
                .build();
        OkHttpUtil.getDefault(this).doUploadFileAsync(info);
    }

    public void downloadFile(String url,String saveDir,String saveName){
        if(null == fileInfo){
            this.url=url;
            this.saveDir=saveDir;
            this.saveName=saveName;
            fileInfo = new DownloadFileInfo(url,saveDir,saveName,new ProgressCallback(){
                @Override
                public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                    httpDownLoadFileListener.onProgressMain(percent, bytesWritten, contentLength, done);
//                    LogUtil.d("HttpHelp", "下载进度：" + percent);
                }
                @Override
                public void onResponseMain(String filePath, HttpInfo info) {
                    if(info.isSuccessful()){
                        httpDownLoadFileListener.httpOnSuccess(filePath, info,fileInfo.getDownloadStatus());
//                        tvResult.setText(info.getRetDetail()+"\n下载状态："+fileInfo.getDownloadStatus());
                    }else{
                        httpDownLoadFileListener.httpOnFailure(filePath, info);
                    }
                }
            });
            HttpInfo info = HttpInfo.Builder().addDownloadFile(fileInfo).build();
            OkHttpUtil.Builder().setReadTimeout(120).build(this).doDownloadFileAsync(info);
        }
    }

    public void pauseDownload(){
        if (fileInfo!=null)
            fileInfo.setDownloadStatus(DownloadStatus.PAUSE);
    }

    public void continueDownload(){
        downloadFile(url,saveDir,saveName);
    }
}
