package monepla.co.jp.cacom.Controller;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import monepla.co.jp.cacom.Constract.CommonConst;
import monepla.co.jp.cacom.Interface.OnItemClickListener;
import monepla.co.jp.cacom.Model.Cash;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * Created by user on 2016/07/14.
 */
public class HomeController extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<Cash> items;
    private CaComApplication application;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd");
    private RecyclerView recyclerView;
    private OnItemClickListener listener;
    /** Constructor **/
    public HomeController(List<Cash> items) {
        this.items = items;
        if (this.items == null) {
            this.items = new ArrayList<> ();
        }
    }
    public void setApplication(CaComApplication application) {
        this.application = application;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Cash getItem(int position) {
        return items.get (position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relative_home_row, parent, false);
        view.setOnClickListener (this);

        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        Cash item = items.get(position);
        HomeHolder vh = (HomeHolder) holder;
        if (CommonConst.CategoryDiv.INPUT.ordinal () == item.cashDiv) {
            vh.categoryText.setText (application.getCategoryHashMap ().get (item.category_id).category_name);
            LogFnc.Logging (LogFnc.INFO,item.originAccountId,LogFnc.current ());
            vh.accountText.setText (application.getLoginUser ().getAccount (item.originAccountId).accountName);
            vh.amountText.setTextColor (Color.BLUE);
        } else if (CommonConst.CategoryDiv.OUTPUT.ordinal () == item.cashDiv){
            vh.accountText.setText (application.getCategoryHashMap ().get (item.category_id).category_name);
            LogFnc.Logging (LogFnc.INFO,item.originAccountId,LogFnc.current ());
            vh.categoryText.setText (application.getLoginUser ().getAccount (item.originAccountId).accountName);
            vh.amountText.setTextColor (Color.RED);
        } else {
            vh.accountText.setText (application.getCategoryHashMap ().get (item.category_id).category_name);
            vh.categoryText.setText (application.getLoginUser ().getAccount (item.originAccountId).accountName);
            vh.accountText.setTextColor (Color.GREEN);
        }
        vh.detailText.setText(item.detail);

        vh.dateText.setText (simpleDateFormat.format (item.cashDate));

        vh.amountText.setText (String.valueOf (item.amount));
        vh.data = item;
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        if (recyclerView == null || listener == null) {
            return;
        }
        int position = recyclerView.getChildAdapterPosition(view);
        listener.onItemClick (position,getItem (position));
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }

}
