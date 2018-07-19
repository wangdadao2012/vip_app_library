package www.comradesoftware.vip.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *  自定义导航视图
 *  by WangPing
 */
public class MyNavigationView extends LinearLayout{
    private Context context;
    private OnTabClickListener mListener; //点击事件
    private List<TextView> tvTabTitles =new ArrayList<>();//tab项里的标题视图列表
    private List<TextView> tvTabIcs=new ArrayList<>();//tab项里的图标视图列表
    private List<LinearLayout> llTabContainers =new ArrayList<>();//tab项布局列表
    private List<String> tabTitleStrList = new ArrayList<>();//tab标题列表
    private List<Integer> tabIcIntList = new ArrayList<>();//tab图标resID列表

    private String mTabBgColor ="#FFFFFFFF";//tab背景默认颜色：白色
    private String mTabBgSelectColor ="#FFFFFFFF";//tab背景选中颜色：白色

    private String mTabColor ="#FF353535";//默认tab颜色：深灰色
    private String mTabSelectColor ="#e1336d";//默认tab选中颜色：玫红色

    private int lastSelectIndex =0;//记录上次选择的tab下标
    private String mTypefacePath="";
    private boolean mIsScaleAnimation =true;//是否启用点击缩放动画
    private float mScaleFromXY=0.6f;//缩放动画系数值

    public MyNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        //判断调用类是否实现了本类的接口，是则赋值
        if (MyNavigationView.class.isAssignableFrom(context.getClass())) {
            this.mListener= (OnTabClickListener) context;
        }
        //初始化一些属性
        setGravity(Gravity.CENTER);
        String mBackgroundColor = "#FFFFFFFF";
        setBackgroundColor(Color.parseColor(mBackgroundColor));
        setMinimumHeight(50);
    }

    /**
     * 添加tab项
     * @param title 标题名
     * @param resIdFontIc 字体图标资源
     */
    public MyNavigationView addTab(String title,int resIdFontIc){
        tabTitleStrList.add(title);
        tabIcIntList.add(resIdFontIc);
        return this;
    }

    /**
     * 添加tab项
     * @param title 标题名
     */
    public MyNavigationView addTab(String title){
        tabTitleStrList.add(title);
        tabIcIntList.add(-1);
        return this;
    }

    /**
     * 添加tab项
     * @param resIdFontIc 字体图标资源
     */
    public MyNavigationView addTab(int resIdFontIc){
        tabTitleStrList.add("");
        tabIcIntList.add(resIdFontIc);
        return this;
    }

    //setOrientation
    public MyNavigationView orientation(int orientation){
        setOrientation(orientation);
        return this;
    }

    //setOrientation
    public void setTabSpace(int Space){
        int orientation=getOrientation();
        int size=llTabContainers.size();
        LayoutParams params= (LayoutParams) llTabContainers.get(0).getLayoutParams();
        for (int i=0;i<size-1;i++ ){
            if (orientation==LinearLayout.VERTICAL){
                params.setMargins(0,0,0,Space);
            }else {
                params.setMargins(0,0,Space,0);
            }
            llTabContainers.get(i).setLayoutParams(params);

        }
    }

    //isScaleAnimation
    public MyNavigationView isScaleAnimation(boolean isScaleAnimation){
        this.mIsScaleAnimation =isScaleAnimation;
        return this;
    }

    //缩放动画系数值,0f-1f
    public MyNavigationView setScaleFromXY(float fromXY){
        mScaleFromXY =fromXY;
        return this;
    }

    //setTypefacePath
    public MyNavigationView setTypefacePath(String path){
        mTypefacePath =path;
        return this;
    }

    //生成Tab视图
    public void create(){
        int size= tabTitleStrList.size();
        if (size>5)
            size=5;
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),mTypefacePath);
        for (int i=0;i<size;i++){
            //创建tab项View
            final LinearLayout llTabContainer =new LinearLayout(context);
            llTabContainer.setGravity(Gravity.CENTER);
            llTabContainer.setPadding(8,8,8,8);
            llTabContainer.setOrientation(LinearLayout.VERTICAL);

            int orientation=getOrientation();
            int width=0,height=0;
            if (orientation==VERTICAL){
                width=LayoutParams.WRAP_CONTENT;
            }else {
                height=LayoutParams.WRAP_CONTENT;
            }
            LayoutParams containerParams=new LayoutParams(width,height,1);
            llTabContainer.setLayoutParams(containerParams);

            if(tabIcIntList.get(i)!=-1){
                //创建tab图标View
                final TextView tvTabIc=new TextView(context);
                tvTabIc.setGravity(Gravity.CENTER);
                tvTabIc.setPadding(2,2,2,2);
                tvTabIc.setTextColor(Color.parseColor(mTabColor));
                tvTabIc.setTypeface(typeface);
                tvTabIc.setTextSize(20);
                tvTabIc.setText(tabIcIntList.get(i));
                LayoutParams tabIcParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
                tabIcParams.setMargins(0,0,0,0);
                tvTabIc.setLayoutParams(tabIcParams);
                llTabContainer.addView(tvTabIc);
                tvTabIcs.add(tvTabIc);
            }else {
                tvTabIcs.add(null);
            }

            if (!tabTitleStrList.get(i).equals("")){
                //创建tab名View
                final TextView tvTabTitle=new TextView(context);
                tvTabTitle.setGravity(Gravity.CENTER);
                tvTabTitle.setPadding(2,2,2,2);
                tvTabTitle.setTextColor(Color.parseColor(mTabColor));
                tvTabTitle.setText(tabTitleStrList.get(i));
                tvTabTitle.setTextSize(12);
                LayoutParams tabNameParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
                tvTabTitle.setLayoutParams(tabNameParams);
                llTabContainer.addView(tvTabTitle);
                tvTabTitles.add(tvTabTitle);
            }else {
                tvTabTitles.add(null);
            }
            //默认选择把第一项设为选择颜色
            if (i==0){
                if (tvTabTitles.get(0)!=null)
                    tvTabTitles.get(0).setTextColor(Color.parseColor(mTabSelectColor));
                if (tvTabIcs.get(0)!=null)
                    tvTabIcs.get(0).setTextColor(Color.parseColor(mTabSelectColor));
                llTabContainer.setBackgroundColor(Color.parseColor(mTabBgSelectColor));
            }else {
                if (tvTabTitles.get(i)!=null)
                    tvTabTitles.get(i).setTextColor(Color.parseColor(mTabColor));
                if (tvTabIcs.get(i)!=null)
                    tvTabIcs.get(i).setTextColor(Color.parseColor(mTabColor));
                llTabContainer.setBackgroundColor(Color.parseColor(mTabBgColor));
            }

            llTabContainers.add(llTabContainer);

            addView(llTabContainer);

            final int finalI = i;
            llTabContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //上次点击的变为未选择状态
                    if (tvTabTitles.get(lastSelectIndex)!=null)
                        tvTabTitles.get(lastSelectIndex).setTextColor(Color.parseColor(mTabColor));
                    if (tvTabIcs.get(lastSelectIndex)!=null)
                        tvTabIcs.get(lastSelectIndex).setTextColor(Color.parseColor(mTabColor));

                    llTabContainers.get(lastSelectIndex).setBackgroundColor(Color.parseColor(mTabBgColor));

                    //当前点击的变为选择状态
                    if (tvTabTitles.get(finalI)!=null)
                        tvTabTitles.get(finalI).setTextColor(Color.parseColor(mTabSelectColor));
                    if (tvTabIcs.get(finalI)!=null)
                        tvTabIcs.get(finalI).setTextColor(Color.parseColor(mTabSelectColor));

                    llTabContainer.setBackgroundColor(Color.parseColor(mTabBgSelectColor));
                    lastSelectIndex =finalI;

                    //View缩放动画
                    if (mIsScaleAnimation)
                        scaleAnimation(v,mScaleFromXY);
                    if (mListener!=null)
                        mListener.onTabClick(v, finalI);
                }
            });
        }

    }

    //设置tab背景色
    public void setTabBgColor(String color,String colorSelect){
        //设置默认色
        int s= llTabContainers.size();
        for (int i=0;i<s;i++) {
            if (i!=lastSelectIndex)
                llTabContainers.get(i).setBackgroundColor(Color.parseColor(color));
        }
        //设置选择色
        llTabContainers.get(lastSelectIndex).setBackgroundColor(Color.parseColor(colorSelect));

        mTabBgSelectColor =colorSelect;
        mTabBgColor =color;
    }

    //设置tab字体和图标色
    public void setTabColor(String color,String colorSelect){
        //设置默认色
        for (int i = 0; i< tvTabTitles.size(); i++){
            if (i!=lastSelectIndex){
                if ( tvTabTitles.get(i)!=null)
                    tvTabTitles.get(i).setTextColor(Color.parseColor(color));
                if (tvTabIcs.get(i)!=null)
                    tvTabIcs.get(i).setTextColor(Color.parseColor(color));
            }
        }

        //设置选择色
        if ( tvTabTitles.get(lastSelectIndex)!=null)
            tvTabTitles.get(lastSelectIndex).setTextColor(Color.parseColor(colorSelect));
        if (tvTabIcs.get(lastSelectIndex)!=null)
            tvTabIcs.get(lastSelectIndex).setTextColor(Color.parseColor(colorSelect));

        mTabSelectColor=colorSelect;
        mTabColor=color;
    }

    //设置tab里的text字体大小
    public void setTabTitleSize(float size){
        int s= tvTabTitles.size();
        for (int i=0;i<s;i++){
            if ( tvTabTitles.get(i)!=null)
                tvTabTitles.get(i).setTextSize(size);
        }
    }

    //设置tab里的字体图标大小
    public void setTabIconSize(float size){
        int s=tvTabIcs.size();
        for (int i=0;i<s;i++){
            if ( tvTabIcs.get(i)!=null)
                tvTabIcs.get(i).setTextSize(size);
        }
    }

    //setTabTitleEms
    public void setTabTitleEms(int ems){
        int s= tvTabTitles.size();
        for (int i=0;i<s;i++){
            if ( tvTabTitles.get(i)!=null)
                tvTabTitles.get(i).setEms(ems);
        }
    }

    //setTabPadding
    public void setTabPadding(int left,int top,int right,int bottom){
        int s= llTabContainers.size();
        for (int i=0;i<s;i++) {
            llTabContainers.get(i).setPadding(left, top, right, bottom);
        }
    }

    /**
     * 根据下标设置tab选择项
     * @param index tab下标
     * @param isCallBack 是否回调tab点击事件接口
     */
    public void setTabSelected(int index,boolean isCallBack){
        if (isCallBack){
            if (mListener!=null)
                mListener.onTabClick(llTabContainers.get(index),index);
        }

        //上次点击的变为默认颜色
        if ( tvTabIcs.get(lastSelectIndex)!=null)
            tvTabIcs.get(lastSelectIndex).setTextColor(Color.parseColor(mTabColor));
        if ( tvTabTitles.get(lastSelectIndex)!=null)
            tvTabTitles.get(lastSelectIndex).setTextColor(Color.parseColor(mTabColor));
        llTabContainers.get(lastSelectIndex).setBackgroundColor(Color.parseColor(mTabBgColor));

        //这次点击设为选择颜色
        if ( tvTabIcs.get(index)!=null)
            tvTabIcs.get(index).setTextColor(Color.parseColor(mTabSelectColor));
        if ( tvTabTitles.get(index)!=null)
            tvTabTitles.get(index).setTextColor(Color.parseColor(mTabSelectColor));
        llTabContainers.get(index).setBackgroundColor(Color.parseColor(mTabBgSelectColor));

        lastSelectIndex=index;
    }

    //View缩放动画
    private void scaleAnimation(View view,float fromXY){
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                fromXY                   //动画开始时，水平方向缩放系数float型值
                , 1                       //动画开始时，垂直方向缩放系数float型值
                ,fromXY                   //动画结束时，水平方向缩放系数float型值
                , 1                       //动画结束时，垂直方向缩放系数float型值
                , Animation.RELATIVE_TO_SELF   //X轴坐标的类型(不同类型，计算x轴上的偏移量的方式不同)
                , 0.5f             //表示轴点在x方向坐标值
                , Animation.RELATIVE_TO_SELF   //Y轴坐标的类型(不同类型，计算y轴上的偏移量的方式不同)
                , 0.5f);           //表示轴点在y方向坐标值
        scaleAnimation.setDuration(200);
        view.startAnimation(scaleAnimation);
    }

    //    点击事件接口
    public interface OnTabClickListener {
        void onTabClick(View v, int index);
    }

    public void setOnTabClickListener(OnTabClickListener listener){
        mListener=listener;
    }
}

