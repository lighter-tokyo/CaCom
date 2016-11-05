package monepla.co.jp.kkb.Controller;

import android.view.View;
import android.widget.TextView;

import co.dift.ui.SwipeToAction;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.R;

/**
 * Created by user on 2016/07/14.
 * ホーム１行ビュー
 */
public class HomeHolder extends SwipeToAction.ViewHolder<Cash> {
    public TextView categoryText;
    public TextView detailText;
    public TextView dateText;
    public TextView amountText;
    public TextView accountText;

    public HomeHolder(View v) {
        super(v);
        categoryText = (TextView) v.findViewById(R.id.row_category_name);
        detailText = (TextView) v.findViewById(R.id.row_detail);
        dateText = (TextView) v.findViewById (R.id.row_date);
        amountText = (TextView) v.findViewById (R.id.row_amount);
        accountText = (TextView) v.findViewById (R.id.row_account_name);
    }


}
