package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.kkb.Controller.HomeController;
import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Interface.OnItemClickListener;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.KkbApplication;

/**
 * Created by user on 2016/09/25.
 */

public class HomeView extends LinearLayout  implements
        OnItemClickListener {
    private Context context;
    /** コントローラー */
    private HomeController adapter;
    private KkbApplication application;
    public ActivityListener activityListener;
    @InjectView (R.id.dw_history_list)
    RecyclerView recyclerView;
    public HomeView(Context context) {
        super (context);
        init (context,null,0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_home, this);
        ButterKnife.inject(this, layout);
        application = (KkbApplication) context.getApplicationContext ();
        a.recycle ();
/** リセントビューセット */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        activityListener = (ActivityListener)context;

    }

    public void setYearMonth(String ym) {
        List<Cash> cashList = application.getLoginUser ().getCashListMap ().get (ym);
        adapter = new HomeController (cashList);
        adapter.setApplication (application);
        adapter.setOnItemClickListener (this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, Object object) {
        Cash cash = (Cash) object;
        activityListener.addStackFragment (PlusFragment.newInstance (cash.cashDiv,cash.amount,cash.objectId));
    }

}
