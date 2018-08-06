package www.comradesoftware.vip.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Paul on 2017/11/8.
 */

public class NoScrollViewPager extends ViewPager {

    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        //为了解决使用photoView时双指对角缩小图片时崩溃问题
        try {
            return !noScroll && super.onTouchEvent(arg0);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        //为了解决使用photoView时双指对角缩小图片时崩溃问题
        try {
            return !noScroll && super.onInterceptTouchEvent(arg0);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //加false参数 去除滑动效果
        super.setCurrentItem(item,false);
    }
}
