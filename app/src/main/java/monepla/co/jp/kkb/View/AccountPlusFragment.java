package monepla.co.jp.kkb.View;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Common.SpinnerCommon;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/08/07.
 *  口座登録
 */
public class AccountPlusFragment extends BaseFragment
        implements View.OnClickListener,DoneCallback,ListDialogFragment.ListDialogItemClickListener {
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
            LogFnc.LogTraceStart (LogFnc.current ());
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
        LogFnc.LogTraceStart(LogFnc.current());
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


        LogFnc.LogTraceEnd(LogFnc.current());
        return view;
    }

    @Override
    public void onClick(View view) {
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
    }

    @Override
    public void done(NCMBException e) {
        if (e == null) {
            appCompatEditText.getEditableText ().clear ();
            return;
        }
        LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
        e.printStackTrace ();
    }

    @OnClick(R.id.plus_account_div)
    public void setAccountName() {
        ListDialogFragment listDialogFragment = new ListDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (listDialogFragment.KEY1,getString (R.string.kind));
        listDialogFragment.setListDialogItemClickListener (this);
        listDialogFragment.setArguments (args);
        listDialogFragment.setTargetFragment(this, 100);
        listDialogFragment.show (getFragmentManager (),getString (R.string.kind));
    }


    @Override
    public void ItemClick(SpinnerCommon spinnerCommon) {
        this.spinnerCommon = spinnerCommon;
        appCompatTextView.setText (spinnerCommon.name);
    }
}
