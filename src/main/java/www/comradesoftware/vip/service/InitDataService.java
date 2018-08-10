package www.comradesoftware.vip.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;

import www.comradesoftware.vip.activities.SplashActivity;
import www.comradesoftware.vip.db.DataDownload;
import www.comradesoftware.vip.db.GlobalConfig;
import www.comradesoftware.vip.db.HtmlTags;
import www.comradesoftware.vip.db.Page;
import www.comradesoftware.vip.db.PageConfig;
import www.comradesoftware.vip.db.Preload;
import www.comradesoftware.vip.db.TabBar;
import www.comradesoftware.vip.db.TabList;
import www.comradesoftware.vip.receiver.InitDataReceiver;
import www.comradesoftware.vip.utils.FileUtils;
import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.utils.RandomUntil;
import www.comradesoftware.vip.utils.SharedTool;
import www.comradesoftware.vip.utils.ToastUtil;
import www.comradesoftware.vip.utils.https.ApiParam;
import www.comradesoftware.vip.utils.https.HttpHelp;
import www.comradesoftware.vip.utils.zip.ZipProgressUtil;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//后台初始化数据服务
public class InitDataService extends Service {
    public HttpHelp httpHelp;
    private SplashActivity activity;
    private Intent mIntent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setActivity(SplashActivity activity) {
        this.activity = activity;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        installDefaultData();
        return super.onStartCommand(intent, flags, startId);
    }

    public void installDefaultData() {
        mIntent = new Intent();
        mIntent.setAction(InitDataReceiver.ACTION_INIT_DATA_UI);

        //判断ACUDATA目录是否存在
        if (!FileUtils.isACUDATAExist(this)) {
            //目录不存在，在外置存储创建acudata目录
            FileUtils.createACUDATAPath(this);
        }
        //没有初始化数据
        if (!SharedTool.getString(this, "unZipDefaultData").equals("1")) {
            //解压初始数据包
            unZipDefaultData(FileUtils.ACUDATA_PATH(this));
            //解析ACUDATA文件夹下build.json文件
            analysisDefaultJson();
        }
        //检查更新
        requestUpdateInfo(getMd5());
    }

    //根据更新记录返回md5的值
    private String getMd5() {
        List<DataDownload> dataDownloads = DataSupport.findAll(DataDownload.class);
        if (dataDownloads.size() > 0) {
            //检测数据库更新记录，Upgrade=1更新成功，0失败
            if (dataDownloads.get(dataDownloads.size() - 1).getUpgrade() == 1) {
//                上次更新成功，返回数据库的md5
                return dataDownloads.get(dataDownloads.size() - 1).getMD5();
            } else {
//                上次更新失败返回""
                return "";
            }
        } else {
            return "";
        }
    }

    //解压默认数据包
    private void unZipDefaultData(String outputDir) {
        try {
            sendBroadcast("解压初始数据包", 0);
            Log.e("--解压默认数据包-->", "解压初始数据包：0%");
            FileUtils.unZipInAsset(this, "www_base.zip", outputDir);
            Log.e("--解压默认数据包-->", "解压初始数据包成功");
            sendBroadcast("解压初始数据包", 100);
            SharedTool.putString(this, "unZipDefaultData", "1");
        } catch (IOException e) {
            e.printStackTrace();
            sendBroadcast("解压初始数据包失败", 0);
            defaultFail();
        }
    }

    //发送广播给SplashActivity，更新UI
    private void sendBroadcast(String content, int progress) {
        mIntent.putExtra("content", content);
        mIntent.putExtra("progress", progress);
        sendBroadcast(mIntent);
    }

    //解析ACUDATA文件夹下build.json文件,写入数据库
    private void analysisDefaultJson() {
        try {
            String jsonStr = FileUtils.getJson(FileUtils.ACUDATA_PATH(this) + "/build.json");
            JSONObject object = new JSONObject(jsonStr);
            List<Page> pages = new Gson().fromJson(object.getString("page")
                    , new TypeToken<List<Page>>() {
                    }.getType());
            DataSupport.deleteAll(Page.class);
            for (Page page : pages) {
                if (page != null)
                    page.save();
            }

            List<String> preloads = new Gson().fromJson(object.getString("preload")
                    , new TypeToken<List<String>>() {
                    }.getType());
            DataSupport.deleteAll(Preload.class);
            for (String preload : preloads) {
                if (preload != null){
                    Preload p=new Preload();
                    p.setValue(preload);
                    p.save();
                }
            }

            DataSupport.deleteAll(GlobalConfig.class);
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setVersion(object.getString("version"));
            globalConfig.setLaunchPage(object.getString("launchPage"));
            globalConfig.setPages(pages);
            globalConfig.setBuild(object.getString("build"));
            globalConfig.save();

            //tabBar
            JSONObject tabBarObject = new JSONObject(object.getString("tabBar"));
            List<TabList> tabLists = new Gson().fromJson(tabBarObject.getString("list")
                    , new TypeToken<List<TabList>>() {
                    }.getType());
            DataSupport.deleteAll(TabList.class);
            for (TabList tab : tabLists) {
                if (tab != null)
                    tab.save();
            }

            DataSupport.deleteAll(TabBar.class);
            TabBar tabBar = new TabBar();
            tabBar.setColor(tabBarObject.getString("color"));
            tabBar.setSelectColor(tabBarObject.getString("selectedColor"));
            tabBar.setPosition(tabBarObject.getString("position"));
            tabBar.setTabLists(tabLists);
            tabBar.save();

            DataSupport.deleteAll(PageConfig.class);
            PageConfig pageConfig = new PageConfig();
            //navigationBar
            JSONObject navigationBarObject = new JSONObject(object.getString("navigationBar"));
            pageConfig.setNavBarBgColor(navigationBarObject.getString("backgroundColor"));
            pageConfig.setNavBarTextColor(navigationBarObject.getString("textColor"));
            pageConfig.setTabBar(tabBar);
            pageConfig.save();

//            获得js和css名称（包括后缀）
            String[] htmlTagStres = new Gson().fromJson(object.getString("html_tags")
                    , new TypeToken<String[]>() {
                    }.getType());
//            获得js和css的所在文件夹
            String contentPath = FileUtils.ACUDATA_PATH(this) + "/content/";
            //用于拼接载入aHtmlTags列表里的js和css的html代码
            StringBuilder tagSB = new StringBuilder();
//            先清空表
            DataSupport.deleteAll(HtmlTags.class);
            for (String aHtmlTags : htmlTagStres) {
//              把名称写入表
                if (aHtmlTags == null)
                    return;
                HtmlTags htmlTags = new HtmlTags();
                htmlTags.setTag(aHtmlTags);
                htmlTags.save();

                //如果字符串末端两个字符包含js,说明是js文件，否则css，据此拼接html代码，更改方式的话MyWebView里的addWabView方法必须也得改方式
                //方式一：webView.loadUrl
//                if (aHtmlTags.substring(aHtmlTags.length()-2,aHtmlTags.length()).contains("js")){
//                    tagSB.append("<script src=\"").append("file://").append(contentPath).append(aHtmlTags).append("\"></script>").append("\n");
//                }else {
//                    tagSB.append("<link href=\"").append("file://").append(contentPath).append(aHtmlTags).append("\" rel=\"stylesheet\">").append("\n");
//                }

//                方式二：webView.loadDataBaseURL
                if (aHtmlTags.substring(aHtmlTags.length() - 2, aHtmlTags.length()).contains("js")) {
                    tagSB.append("<script src=\"").append("file://").append(aHtmlTags).append("\"></script>").append("\n");
                } else {
                    tagSB.append("<link href=\"").append("file://").append(aHtmlTags).append("\" rel=\"stylesheet\">").append("\n");
                }
            }

            //循环把js、css样式写入各个HTML
            int pageSize = pages.size();
            for (int i = 0; i < pageSize; i++) {
                if (pages.get(i) != null) {
                    //获得要编辑的html文件的路径
                    String filePathName = FileUtils.ACUDATA_PATH(this) + "/page/" + pages.get(i).getPage().toLowerCase() + "/index.htm";
                    //先读取到html文件的全部内容
                    StringBuilder htmlContent = getHtmlContent(filePathName);
                    //找到内容末尾最后出现"<"的index，借此找出<\html>之前的index
                    int index = htmlContent.lastIndexOf("<");
                    htmlContent.insert(index - 1, tagSB.toString());
                    //写入编辑好的内容
                    FileOutputStream fos = new FileOutputStream(filePathName);
                    fos.write(htmlContent.toString().getBytes());
                    fos.close();
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            LogUtil.e("InitDataService", "解析出错了：" + e.getMessage());
        }
    }

    private StringBuilder getHtmlContent(String filePathName) throws IOException {
        FileInputStream fis = new FileInputStream(filePathName);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        String res = EncodingUtils.getString(buffer, "UTF-8");

        fis.close();
        return new StringBuilder(res);
    }

    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public InitDataService getService() {
            return InitDataService.this;
        }
    }

    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //获取更新信息
    private void requestUpdateInfo(final String MD5) {

        sendBroadcast("检查数据", RandomUntil.getNum(11, 100));
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("MD5", MD5);
        if (httpHelp == null)
            httpHelp = new HttpHelp(this);
        ApiParam param = httpHelp.getForApiParamSign(map);
        String url = "WxMiniApp/api/Config/vipapp_UpdateData";
        httpHelp.doAsyncPost(url, param, false, null);
        httpHelp.setHttpResponseListener(new HttpHelp.HttpResponseListener() {
            @Override
            public void httpOnFailure(HttpInfo info) {
                String result = info.getRetDetail();
                Log.e("--requestUpdateInfo-->", "异步请求失败:" + result);
                ToastUtil.showToast(activity, "网络连接失败！");
                //进入主页
                sendBroadcast("START_ACTIVITY", -1);
            }

            @Override
            public void httpOnSuccess(HttpInfo info) {
                String result = info.getRetDetail();
                LogUtil.e("--requestUpdateInfo-->", "异步请求成功:" + result);
                //解析JSON
                analysisJson(result);
            }

            //解析JSON
            private void analysisJson(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Result").equalsIgnoreCase("success")) {
                        JSONObject dataJb = new JSONObject(object.getString("Data"));
                        String md5 = dataJb.getString("MD5");
                        String url = dataJb.getString("URL");

                        //url不为空则有更新包
                        if (!TextUtils.isEmpty(url)) {
                            //清空DataDownLoad表
                            DataSupport.deleteAll(DataDownload.class);
                            //把更新数据写入数据库
                            DataDownload dataDownload = new DataDownload();
                            dataDownload.setId(1);
                            dataDownload.setMD5(md5);
                            dataDownload.setZipPath(url);
                            dataDownload.save();

                            //解压默认数据包到Acudata2临时目录
                            unZipDefaultData(FileUtils.getAcudata2Path(InitDataService.this));

//                            下载更新包到file/download目录
                            String saveDir = FileUtils.getDownloadPath(InitDataService.this) + "/";
                            downloaderUpData(httpHelp, url, saveDir);
                        } else {
                            //为空没有更新包，进入主页
                            sendBroadcast("START_ACTIVITY", -1);
                        }

                    } else {
                        //{"Result":"error","Data":null,"Error":"已是最新版"},进入主页
                        sendBroadcast("进入主页", 100);
                        sendBroadcast("START_ACTIVITY", -1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("--requestUpdateInfo-->", "解析失败:"+e.getMessage());
                    sendBroadcast("START_ACTIVITY", -1);
                }
            }
        });
    }

    //下载更新包
    private void downloaderUpData(HttpHelp httpHelp, String url, String saveDir) {
        httpHelp.downloadFile(url, saveDir, "www_base_up.zip");
        httpHelp.sethttpDownLoadFileListener(new HttpHelp.httpDownLoadFileListener() {
            @Override
            public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                LogUtil.e("InitDataService-下载更新包", "下载进度：" + percent);
                sendBroadcast("下载更新包", percent);
            }

            @Override
            public void httpOnSuccess(String filePath, HttpInfo info, String downloadStatus) {
                LogUtil.e("InitDataService-下载更新包", info.getRetDetail() + "下载状态：" + downloadStatus);
                DataDownload dataDownload = new DataDownload();
//                判断下载状态
                if (downloadStatus.equals("COMPLETED")) {
                    dataDownload.setDownloaded(1);
                    unZipUpData(info.getRetDetail());//解压更新包
                } else {
                    defaultFail();
                    ToastUtil.showToast(activity, "下载出错，请退出应用后重试");
                }
                dataDownload.updateAll();
            }

            @Override
            public void httpOnFailure(String filePath, HttpInfo info) {
                defaultFail();
            }
        });
    }

    //解压更新包
    private void unZipUpData(final String filePath) {
        ZipProgressUtil.UnZipFile(filePath, FileUtils.getAcudata2Path(InitDataService.this), new ZipProgressUtil.ZipListener() {
            @Override
            public void zipStart() {
                LogUtil.e("解压更新包", "开始");
            }

            @Override
            public void zipSuccess() {
                LogUtil.e("解压更新包", "成功");
//                把ACUDATA 目录改名为ACUDATAbak
                FileUtils.rename(FileUtils.ACUDATA_PATH(InitDataService.this), FileUtils.getACUDATAbakPath(InitDataService.this));
//                把临时目录改名为ACUDATA
                FileUtils.rename(FileUtils.getAcudata2Path(InitDataService.this), FileUtils.ACUDATA_PATH(InitDataService.this));

                FileUtils.deleteFile(filePath);//删除更新包
                FileUtils.deleteDirectory(FileUtils.getACUDATAbakPath(InitDataService.this)); //删除ACUDATAbak目录
                FileUtils.deleteDirectory(FileUtils.getDownloadPath(InitDataService.this)); //删除Download目录

                DataDownload dataDownload = new DataDownload();
                dataDownload.setUpgrade(1);
                dataDownload.updateAll();

                analysisDefaultJson();
                sendBroadcast("START_ACTIVITY", -1);
            }

            @Override
            public void zipProgress(int progress) {
                LogUtil.e("解压更新包", "进度：" + progress);
                sendBroadcast("解压更新包", progress);
            }

            @Override
            public void zipFail(Exception e) {
                LogUtil.e("解压更新包", "错误：" + e.getMessage() + ",文件路径：" + filePath);
                sendBroadcast("解压更新包错误", 0);
                defaultFail();
            }
        });
    }

    //失败后清除数据
    private void defaultFail() {
        FileUtils.deleteDirectory(FileUtils.getACUDATAbakPath(this));
        FileUtils.deleteDirectory(FileUtils.ACUDATA_PATH(this));
        FileUtils.deleteDirectory(FileUtils.getDownloadPath(this));
        DataSupport.deleteAll(DataDownload.class);
        SharedTool.putString(this, "unZipDefaultData", "0");
        sendBroadcast("START_ACTIVITY", -1);

    }

}
