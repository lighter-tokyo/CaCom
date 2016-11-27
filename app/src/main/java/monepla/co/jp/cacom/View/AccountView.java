package monepla.co.jp.cacom.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.cacom.Constract.CommonConst;
import monepla.co.jp.cacom.Controller.AccountController;
import monepla.co.jp.cacom.Interface.ActivityListener;
import monepla.co.jp.cacom.Interface.OnItemClickListener;
import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.Model.Cash;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * Created by user on 2016/09/30.
 */

public class AccountView extends LinearLayout implements View.OnClickListener,
    OnItemClickListener

    {
    private Context context;
    /** コントローラー */
    private AccountController adapter;
    private CaComApplication application;
    public ActivityListener activityListener;
    public AccountView(Context context) {
        super (context);
        init (context,null,0);
    }

    @InjectView(R.id.dw_history_list)
    RecyclerView recyclerView;

    private void init(Context context, AttributeSet attrs, int defStyle) {
        application = (CaComApplication) context.getApplicationContext ();
        LogFnc.LogTraceStart(LogFnc.current(),application);
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_home, this);
        ButterKnife.inject(this, layout);

        a.recycle ();
/** リセントビューセット */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        activityListener = (ActivityListener)context;
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }
    public void setListView() {
        /** リセントビューセット */
        LogFnc.LogTraceStart(LogFnc.current(),application);
        HashMap<String,List<Cash>> cashListMap = application.getLoginUser ().getCashListMap ();
        HashMap<Account,Long> accountLongMap = new HashMap<> ();
        for (List<Cash> cashList : cashListMap.values ()) {
            for (Cash cash : cashList) {
                if (CommonConst.CategoryDiv.INPUT.ordinal () == cash.cashDiv) {
                    if (accountLongMap.containsKey (application.getLoginUser ().getAccount (cash.originAccountId))) {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId),accountLongMap.get (application.getLoginUser ().getAccount (cash.originAccountId)) + cash.amount);
                    } else {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId), cash.amount);
                    }
                } else if (CommonConst.CategoryDiv.OUTPUT.ordinal () == cash.cashDiv) {
                    if (accountLongMap.containsKey (application.getLoginUser ().getAccount (cash.originAccountId))) {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId),accountLongMap.get (application.getLoginUser ().getAccount (cash.originAccountId)) - cash.amount);
                    } else {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId),- cash.amount);
                    }
                } else {
                    if (accountLongMap.containsKey (application.getLoginUser ().getAccount (cash.originAccountId))) {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId), accountLongMap.get (application.getLoginUser ().getAccount (cash.originAccountId)) - cash.amount);
                    } else {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.originAccountId), -cash.amount);
                    }

                    if (application.getLoginUser ().getAccount (cash.category_id) != null && accountLongMap.containsKey (application.getLoginUser ().getAccount (cash.category_id))) {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.category_id), accountLongMap.get (application.getLoginUser ().getAccount (cash.category_id)) + cash.amount);
                    } else if (application.getLoginUser ().getAccount (cash.category_id) != null) {
                        accountLongMap.put (application.getLoginUser ().getAccount (cash.category_id), cash.amount);
                    }
                }
            }


        }
        adapter = new AccountController (accountLongMap);
        recyclerView.setAdapter(adapter);
        adapter.setApplication (application);
        adapter.setOnItemClickListener (this);
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }


        @Override
        public void onClick(View view) {
            //getFragmentManager ().beginTransaction ().replace (R.id.fragment_view,PlusFragment.newInstance (-1,-1,null)).commit ();
        }

        @Override
        public void onItemClick(int position, Object object) {
            LogFnc.LogTraceStart(LogFnc.current(),application);
            try {
                Account account = (Account) object;
//                getFragmentManager ().beginTransaction ().replace (R.id.fragment_view,AccountPlusFragment.newInstance (account.objectId)).commit ();
            } catch (ClassCastException e) {
                e.printStackTrace ();
            }
            LogFnc.LogTraceEnd(LogFnc.current(),application);
        }
    }
