package monepla.co.jp.kkb.Common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by user on 2016/08/20.
 */
abstract public class BasePageAdapter extends FragmentPagerAdapter {
    public ArrayList<String> yearMonth;
    public static String KEY_INDEX = "INDEX";
    public BasePageAdapter(FragmentManager fm) {
        super (fm);
        yearMonth = new ArrayList<> ();
    }

    @Override
    public int getCount() {
        return yearMonth.size ();
    }

    public void add(String ym) {
        yearMonth.add (ym);
    }

    public void destroyAllItem(ViewPager pager) {
        for (int i = 0; i < getCount() - 1; i++) {
            try {
                Object obj = this.instantiateItem(pager, i);
                if (obj != null)
                    destroyItem(pager, i, obj);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    public ArrayList<String> getAll() {
        return yearMonth;
    }

    public void addAll(ArrayList<String> list) {
        yearMonth = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return yearMonth.get (position);
    }
}
