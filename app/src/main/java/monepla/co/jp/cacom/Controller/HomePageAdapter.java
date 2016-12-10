package monepla.co.jp.cacom.Controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;
import monepla.co.jp.cacom.View.HomeView;

/**
 * Created by user on 2016/08/16.
 */
public class HomePageAdapter extends PagerAdapter {
    private List<String> homeViewList = new ArrayList<> ();
    private Context mContext;
    private CaComApplication application;

    public HomePageAdapter (Context context) {
        mContext = context;
        application = (CaComApplication) context.getApplicationContext();
    }
    @Override
    public int getCount() {
        return homeViewList.size ();
    }

    public void addAll(List<String> viewList) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        homeViewList.addAll (viewList);
        for (String ym : viewList) {
            LogFnc.Logging (LogFnc.ERROR,ym,LogFnc.current ());
        }
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        String name = homeViewList.get (position);
        HomeView homeView = new HomeView (mContext);
        homeView.setYearMonth (name);
        container.addView (homeView);
        LogFnc.LogTraceEnd (LogFnc.current (),application);
        return homeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // コンテナから View を削除
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // Object 内に View が存在するか判定する
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return homeViewList.get (position);
    }
}
