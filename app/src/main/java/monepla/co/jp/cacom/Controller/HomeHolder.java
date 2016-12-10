package monepla.co.jp.cacom.Controller;

import android.view.View;
import android.widget.TextView;

import co.dift.ui.SwipeToAction;
import monepla.co.jp.cacom.Model.Cash;
import monepla.co.jp.cacom.R;

/**
 * Created by user on 2016/07/14.
 * ホーム１行ビュー
 */
class HomeHolder extends SwipeToAction.ViewHolder<Cash> {
    TextView categoryText;
    TextView detailText;
    TextView dateText;
    TextView amountText;
    TextView accountText;

    HomeHolder(View v) {
        super(v);
        categoryText = (TextView) v.findViewById(R.id.row_category_name);
        detailText = (TextView) v.findViewById(R.id.row_detail);
        dateText = (TextView) v.findViewById (R.id.row_date);
        amountText = (TextView) v.findViewById (R.id.row_amount);
        accountText = (TextView) v.findViewById (R.id.row_account_name);
    }


}
