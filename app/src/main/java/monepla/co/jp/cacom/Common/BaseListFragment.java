package monepla.co.jp.cacom.Common;

import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by user on 2016/08/20.
 */
abstract public class BaseListFragment extends BaseFragment implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener{
    public ViewPager viewPager;
    public PagerSlidingTabStrip tabLayout;

    @Override
    public void onTabChanged(String s) {
        viewPager.setCurrentItem (Integer.valueOf(s));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setCurrentItem (position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
