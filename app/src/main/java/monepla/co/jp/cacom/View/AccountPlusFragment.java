package monepla.co.jp.cacom.View;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import monepla.co.jp.cacom.Common.BaseFragment;
import monepla.co.jp.cacom.Common.SpinnerCommon;
import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.LogFnc;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static monepla.co.jp.cacom.Utils.CommonUtils.getQuery;

/**
 * Created by user on 2016/08/07.
 *  口座登録
 */
public class AccountPlusFragment extends BaseFragment
        implements View.OnClickListener,DoneCallback,ListDialogFragment.ListDialogItemClickListener,FindCallback<NCMBObject> {
    /***/
    private static AccountPlusFragment accountPlusFragment;

    @InjectView (R.id.plus_text_input)
    AppCompatEditText appCompatEditText;
    @InjectView (R.id.plus_account_div)
    AppCompatEditText appCompatTextView;

    private SpinnerCommon spinnerCommon;
    public static AccountPlusFragment newInstance(String id) {
        if (accountPlusFragment == null) {
            accountPlusFragment = new AccountPlusFragment ();
        }
        Bundle args = new Bundle ();
        args.putString (ARG_PARAM1,id);
        accountPlusFragment.setArguments (args);
        return accountPlusFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        /** ビューセット */
        View view = inflater.inflate(R.layout.fragment_account_plus, container, false);
        ButterKnife.inject(this, view);

        /** 口座区分セット */
        if (getArguments ().getString (ARG_PARAM1) != null) {
            Account account = new Select ().from (Account.class).where (Account.COL_OBJECT_ID + " =?",getArguments ().getString (ARG_PARAM1)).executeSingle ();
            appCompatEditText.setText (account.accountName);
        }

        /** リスナーセット */
        view.findViewById (R.id.register_button).setOnClickListener (this);
        /** ドロワー表示 */
        activityListener.showDrawer ();
        /** タイトルセット */
        activityListener.setToolbarTitle (R.string.account_register);


        LogFnc.LogTraceEnd(LogFnc.current(),application);
        return view;
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        NCMBObject ncmbObject = new NCMBObject (Account.TABLE_NAME);
        if (getArguments ().getString (ARG_PARAM1) != null) {
            ncmbObject.setObjectId (getArguments ().getString (ARG_PARAM1));
        }
        ncmbObject.put (Account.COL_USER_ID,application.getLoginUser ().objectId);
        ncmbObject.put (Account.COL_ACCOUNT_NAME,appCompatEditText.getText ().toString ());
        ncmbObject.put (Account.COL_ACCOUNT_DIV,spinnerCommon.id);
        ncmbObject.put (Account.COL_CREATED_USER,application.getLoginUser ().objectId);
        ncmbObject.put (Account.COL_UPDATED_USER,application.getLoginUser ().objectId);
        ncmbObject.put (Account.COL_DEL_FLG,false);
        ncmbObject.saveInBackground (this);
        activityListener.showProgress (R.string.user_register);
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }

    @Override
    public void done(NCMBException e) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        if (e == null) {
            appCompatEditText.getEditableText ().clear ();
            NCMBQuery<NCMBObject> query = getQuery (application.getApplicationContext(), Account.TABLE_NAME);
            query.whereEqualTo (Account.COL_USER_ID,application.getLoginUser ().objectId);
            query.findInBackground(this);
            LogFnc.LogTraceEnd(LogFnc.current(),application);
            return;
        }
        activityListener.closeProgress();
        LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
        e.printStackTrace ();
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }

    @OnClick(R.id.plus_account_div)
    public void setAccountName() {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        ListDialogFragment listDialogFragment = new ListDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (ListDialogFragment.KEY1,getString (R.string.kind));
        listDialogFragment.setListDialogItemClickListener (this);
        listDialogFragment.setArguments (args);
        listDialogFragment.setTargetFragment(this, 100);
        listDialogFragment.show (getFragmentManager (),getString (R.string.kind));
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }


    @Override
    public void ItemClick(SpinnerCommon spinnerCommon) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        this.spinnerCommon = spinnerCommon;
        appCompatTextView.setText (spinnerCommon.name);
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }

    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        for (NCMBObject object : list) {
            Account account = new Select().from(Account.class).where(Account.COL_OBJECT_ID + " = ? ", object.getObjectId()).executeSingle();
            if (account == null) {
                /** インサート */
                account = new Account();
            }
            account.getToAppModel(Account.class, object);
        }
        /** ホームへ遷移 */
        FragmentManager fm = getFragmentManager ();
        FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt (0);
        fm.popBackStack (entry.getId (), POP_BACK_STACK_INCLUSIVE);
        activityListener.closeProgress();
        LogFnc.LogTraceEnd(LogFnc.current(),application);
    }
}
