package monepla.co.jp.kkb.Controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.View.AccountItzView;
import monepla.co.jp.kkb.View.AccountMonthView;
import monepla.co.jp.kkb.View.AccountView;

/**
 * Created by user on 2016/08/20.
 */
public class AccountPageAdapter extends PagerAdapter {
    private String[] accountViewList = new String[3];
    private Context mContext;

    public AccountPageAdapter(Context context) {
        mContext = context;
        accountViewList[0] = context.getString (R.string.account_history);
        accountViewList[1] = context.getString (R.string.account_break_down);
        accountViewList[2] = context.getString (R.string.account_monthly);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return accountViewList.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                //残高
                AccountView accountView = new AccountView (mContext);
                container.addView (accountView);
                accountView.setListView ();
                return accountView;
            case 1:
                //内訳
                AccountItzView accountItzView = new AccountItzView (mContext);
                container.addView (accountItzView);
                accountItzView.setGraphView ();
                return accountItzView;
            case 2:
                AccountMonthView accountMonthView = new AccountMonthView (mContext);
                container.addView (accountMonthView);
                accountMonthView.setGraphView ();
                return accountMonthView;
        }


        return null;
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
        return accountViewList[position];
    }
}
