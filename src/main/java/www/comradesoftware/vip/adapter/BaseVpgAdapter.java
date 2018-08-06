package www.comradesoftware.vip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ViewPager适配器
 * Created by Paul on 2018/1/10.
 */

public class BaseVpgAdapter extends FragmentPagerAdapter {
    private List<Fragment> fmList;
    private FragmentManager fm;

    public BaseVpgAdapter(FragmentManager fm, List<Fragment> fmList) {
        super(fm);
        this.fmList = fmList;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position % fmList.size());
    }



    @Override
    public int getCount() {
        return fmList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        //没有找到child  要求重新加载
        return POSITION_NONE;
    }
}
