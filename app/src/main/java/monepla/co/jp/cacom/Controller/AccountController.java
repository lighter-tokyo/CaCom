package monepla.co.jp.cacom.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import monepla.co.jp.cacom.Interface.OnItemClickListener;
import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * Created by user on 2016/08/22.
 */
public class AccountController extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<Map.Entry<Account, Long>> items = new ArrayList<> ();
    private RecyclerView recyclerView;
    private CaComApplication application;
    private OnItemClickListener listener;

    public AccountController(HashMap<Account,Long> items) {
        for (Map.Entry<Account,Long> map : items.entrySet ()) {
            this.items.add (map);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_row, parent, false);
        application = (CaComApplication)parent.getContext().getApplicationContext();
        view.setOnClickListener (this);
        return new AccountHolder (view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        Map.Entry<Account,Long> entry = items.get (position);
        AccountHolder accountHolder = (AccountHolder) holder;
        accountHolder.account_name.setText (entry.getKey ().accountName);
        accountHolder.account_amount.setText (String.valueOf (entry.getValue ()));
        accountHolder.map = entry;
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    @Override
    public int getItemCount() {
        return items.size ();
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        if (recyclerView == null || listener == null) {
            return;
        }
        int position = recyclerView.getChildAdapterPosition(view);
        listener.onItemClick (position,getEntry (position).getKey ());
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    private Map.Entry<Account,Long> getEntry(int position) {
        return items.get (position);
    }

    public void setApplication(CaComApplication application) {
        this.application = application;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
