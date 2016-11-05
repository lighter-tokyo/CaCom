package monepla.co.jp.kkb.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.R;

/**
 * Created by user on 2016/08/22.
 */
public class AccountHolder extends RecyclerView.ViewHolder {
    public TextView account_name;
    public TextView account_amount;
    public Map.Entry<Account,Long> map;
    public AccountHolder(View itemView) {
        super (itemView);
        account_name = (TextView) itemView.findViewById (R.id.row_account_name);
        account_amount = (TextView) itemView.findViewById (R.id.row_account_amount);
    }
}
