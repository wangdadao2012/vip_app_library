package www.comradesoftware.vip.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wendu.dsbridge.DWebView;
import www.comradesoftware.vip.activities.MainActivity;
import www.comradesoftware.vip.db.Preload;
import www.comradesoftware.vip.utils.LogUtil;

public class PreloadWbViewPage extends NoScrollViewPager {
    private List<Preload> mPreloads = LitePal.findAll(Preload.class);
    private Context context;
    private List<String> pageZ=new ArrayList<>();

    public PreloadWbViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public PreloadWbViewPage(Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init(){
        //禁止滑动
        setNoScroll(true);
        setOffscreenPageLimit(mPreloads.size());
        //设置适配器
        setAdapter(adapter);
    }

    /**
     * 根据page获取下标
     * @param page 后台返回的page
     * @return 存在返回下标，不存在返回-1
     */
    private int getIndexOfPage(String page){
        int size=mPreloads.size();
        for (int i=0;i<size;i++){
            if (mPreloads.get(i).getValue().equals(page)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 移除页面
     * @param index 页面下标
     */
    private void removeItem(int index){
        if (mPreloads.size()>index){
            mPreloads.remove(index);
            adapter.notifyDataSetChanged();
        }
    }

    private void removeItemOfPageZ(){
        for (int i=0;i<pageZ.size();i++){
            String currentPage=mPreloads.get(getCurrentItem()).getValue();
            if (pageZ.get(i).equals(currentPage)){
                //如果队列里存在当前页,就把它移出队列
                pageZ.remove(currentPage);
            }
        }
    }

    /**
     * 在0下标插入页面
     * @param page 后台传来的页面标志
     */
    private void addItemInFirst(String page){
        Preload preload=new Preload();
        preload.setValue(page);
        mPreloads.add(0,preload);
        adapter.notifyDataSetChanged();
    }

    /**
     * JS的navigateTo接口需求：
     * 只能跳到内部页，一个内部页面打开对应一个新的WebView，已存在的就显示，不存在就创建，加入队列
     * 参数 { Page:"Main",Close:true }
     * Close跳转并关闭当前WebView(如果可以关闭的话)
     */
    public void setPreloadViewPageTo(String page,boolean close){
        hidePreload(false);
        //如果后台要求close当前页再跳转，就remove当前item
        if (close){
            //1.先从队列移除当前页
            removeItemOfPageZ();
            //2.再从page移除当前页
            removeItem(getCurrentItem());
        }

        //把跳转页添加到队列,如果队列有它了，就把它末尾
        addPageZ(page);
        setCurrentItemOfPage(page);
        LogUtil.e("pageZ",pageZ.toString());
        LogUtil.e("page",mPreloads.toString());
    }

    //把跳转页添加到队列,如果队列有它了，就把它移到末尾，因为JS的navigateBack接口调用的话，返回的页面是读取队列从末至首
    private void addPageZ(String page){
        int size =pageZ.size();
        boolean isHave=false;
        for (int i=0;i<size;i++){
            if (pageZ.get(i).equals(page)){
//                移到后面
                Collections.swap(pageZ,i,size-1);
                isHave=true;
            }
        }
        if (!isHave)
            pageZ.add(page);
    }

    private void setCurrentItemOfPage(String page){
        //遍历preloads看有没有page，下标>=0说明存在page页面
        int index=getIndexOfPage(page);
//        下标>=0说明存在page页面
        if (index>=0){
            //存在page，跳转
            setCurrentItem(index);
        }else{
            //不存在page，在0下标处创建对应page页面，再跳转
            addItemInFirst(page);
            setCurrentItem(0);
        }
    }

    /**
     * JS的navigateBack接口需求:
     * 如果页面是tab列表和preload列表的页面，则保持webview的生命，如果是preload页面既隐藏，返回显示上一个页面，如果是tab页面就不用做动作，因为已经是顶层
     */
    public void setPreloadViewPageBack() {
//        如果preload页面是显示状态，说明当前是preload页面
        if (getVisibility() == VISIBLE) {
            //计算队列的大小
            int size = pageZ.size();
            if (size == 1) {
                //如果队列里只有一个，把它移出队列，然后隐藏PreloadViewPage
                pageZ.remove(0);
                hidePreload(true);
            } else {
                //如果队列里只有2个或以上，把最后移出队列，倒数第二显示
                int index = size - 2;
                String page = pageZ.get(index);
                setCurrentItem(getIndexOfPage(page));
                pageZ.remove(size - 1);
                hidePreload(false);
            }
            LogUtil.e("pageZ", pageZ.toString());
        }
    }

    private void hidePreload(boolean hide){
        if (hide){
            setVisibility(GONE);
            ((MainActivity)context).setMyWebViewsVisibility(VISIBLE);
        }else {
            setVisibility(VISIBLE);
            ((MainActivity)context).setMyWebViewsVisibility(GONE);
        }
    }

    //适配器
    private PagerAdapter adapter =new PagerAdapter() {
        @Override
        public int getCount() {
            return mPreloads.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }
        //创建视图  根据指定位置将视图添加到容器当中
        //uper.instantiateItem(container, position);  必须删点  通过看源码 里面抛出的是异常

        @SuppressLint("SetJavaScriptEnabled")
        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            DWebView webView= new DWebView(context);
//            webView.setWebViewClient(new WebViewClient());
//            webView.setWebChromeClient(new WebChromeClient());
//            WebSettings settings = webView.getSettings();
//            settings.setJavaScriptEnabled(true);//设置js可用(必要)
//            settings.setAllowFileAccess(true);// 设置允许访问文件数据(必要)
//            settings.setSupportZoom(true);//支持放大网页功能
//            settings.setBuiltInZoomControls(true);//支持缩小网页
//            webView.addJavascriptObject(new JsApi(context), null);
//            DWebView.setWebContentsDebuggingEnabled(true);
            String data = "内部页面:"+ mPreloads.get(position).getValue();
            webView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");

            container.addView(webView);
            return webView;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
        //销毁视图  根据指定位置删除ViewGroup视图
        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            // TODO Auto-generated method stub
            container.removeView((View)object);
        }
    };
}
