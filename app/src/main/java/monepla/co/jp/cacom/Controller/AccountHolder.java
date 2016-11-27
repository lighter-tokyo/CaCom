package monepla.co.jp.cacom.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.R;

/**
 * Created by user on 2016/08/22.
 */
class AccountHolder extends RecyclerView.ViewHolder {
    TextView account_name;
    TextView account_amount;
    Map.Entry<Account,Long> map;
    AccountHolder(View itemView) {
        super (itemView);
        account_name = (TextView) itemView.findViewById (R.id.row_account_name);
        account_amount = (TextView) itemView.findViewById (R.id.row_account_amount);
    }
}
