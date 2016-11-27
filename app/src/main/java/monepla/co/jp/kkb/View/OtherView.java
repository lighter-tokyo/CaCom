package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.OtherController;
import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.KkbApplication;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/10/30.
 */

public class OtherView extends LinearLayout implements OtherController.OnOtherClickListener{
    private Context context;
    private KkbApplication application;
    private ActivityListener activityListener;
    private OtherController adapter;
    @InjectView(R.id.other_recycler)
    RecyclerView recyclerView;
    public OtherView(Context context) {
        super (context);
        // Load attributes
        final TypedArray a = getContext ().obtainStyledAttributes (
                null, R.styleable.HomeListView, 0, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_other, this);
        ButterKnife.inject(this, layout);
        application = (KkbApplication) context.getApplicationContext ();
        a.recycle ();
        /** リセントビューセット */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        List<Integer> otherList = new ArrayList<> ();
        for (CommonConst.OtherItems item : CommonConst.OtherItems.values ()) {
            otherList.add (item.getItems ());
        }
        adapter = new OtherController (this,otherList);
        activityListener = (ActivityListener)context;
    }


    public void invalidateTextPaintAndMeasurements() {
        LogFnc.LogTraceStart (LogFnc.current ());

        recyclerView.setAdapter (adapter);
        activityListener.setToolbarTitle (R.string.other);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void invalidate() {
//        recyclerView.setAdapter (adapter);
    }


    @Override
    public void onOtherClick(int item) {
        LogFnc.LogTraceStart (LogFnc.current ());
        LogFnc.Logging (LogFnc.INFO,getResources ().getString (item),LogFnc.current ());
        if (CommonConst.OtherItems.PASS_CODE.getItems () == item) {
            activityListener.addStackFragment(PassCodeFragment.newInstance (false));
            return;
        }
        if (CommonConst.OtherItems.LOGOUT.getItems () == item) {
            //ローカルファイル削除
            SharedPreferences preferences = getContext ().getSharedPreferences (CommonConst.PREF_UPDATE,Context.MODE_PRIVATE);
            //ローカルDB削除
            SharedPreferences.Editor editor = preferences.edit();
            Map<String, ?> keys = preferences.getAll();
            if (keys.size() > 0) {
                for (String key : keys.keySet()) {
                    editor.remove(key);
                }
                editor.apply ();
            }


            NCMBUser.logoutInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    new Delete().from(User.class).execute();
                    new Delete().from(Account.class).execute();
                    new Delete().from(Category.class).execute();
                    new Delete().from(Cash.class).execute();
                    activityListener.logout();
                }
            });

            return;
        }
//        if (CommonConst.OtherItems.PASSWORD_FORGET.getItems () == item && TextUtils.isEmpty (application.getLoginUser ().mailAddress)) {
//            activityListener.addStackFragment(SettingFragment.newInstance (CommonConst.OtherItems.MAIL_ADDRESS_CHANGE.getItems ()));
//            return;
//        }
//        if (CommonConst.OtherItems.PASSWORD_CHANGE.getItems () == item && TextUtils.isEmpty (application.getLoginUser ().mailAddress)) {
//            activityListener.addStackFragment(SettingFragment.newInstance (CommonConst.OtherItems.MAIL_ADDRESS_CHANGE.getItems ()));
//            return;
//        }
        activityListener.addStackFragment(SettingFragment.newInstance (item));
    }
}
