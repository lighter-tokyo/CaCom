package monepla.co.jp.cacom.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

import monepla.co.jp.cacom.Common.BaseFragment;
import monepla.co.jp.cacom.Constract.CommonConst;
import monepla.co.jp.cacom.Controller.LoginController;
import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.Model.Cash;
import monepla.co.jp.cacom.Model.Category;
import monepla.co.jp.cacom.Model.User;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CommonUtils;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * 登録フラグメント
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @see monepla.co.jp.cacom.Common.BaseFragment
 * @see android.view.View.OnClickListener
 * @see monepla.co.jp.cacom.Controller.LoginController.LoginCallback
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener ,LoginController.LoginCallback{

    /** ユーザ名 */
    private AppCompatEditText userNameEditText;
    /** パスワード */
    private AppCompatEditText passEditText;
    /** パスワード確認 */
    private AppCompatEditText passConfirmEditText;
    /** ログインコントローラー */
    private LoginController loginController;
    /** singleton */
    private static RegisterFragment fragment = new RegisterFragment();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        /** view設定 */
        userNameEditText = (AppCompatEditText)view.findViewById(R.id.register_user_name_edit_text);
        passEditText = (AppCompatEditText)view.findViewById(R.id.register_pass_edit_text);
        passConfirmEditText = (AppCompatEditText)view.findViewById(R.id.register_pass_confirm_edit_text);
        view.findViewById(R.id.register_button).setOnClickListener(this);
        /** タイトル設定 */
        activityListener.setToolbarTitle (R.string.user_register);
        activityListener.closeDrawer ();
        /** フィルターの配列をセット */
        InputFilter[] filters = new InputFilter[] {CommonUtils.inputFilter};
        userNameEditText.setFilters(filters);
        /** 登録コールバック */
        loginController = new LoginController (application);
        loginController.setLoginCallback (this,activityListener);

        LogFnc.LogTraceEnd (LogFnc.current (),application);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogFnc.LogTraceStart(LogFnc.current(),application);
        loginController = null;
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }


    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        /** 登録チェック */
        if (isLoginParamCheck()) {
            LogFnc.Logging(LogFnc.DEBUG,"登録開始",LogFnc.current());
            loginController.register (
                    passConfirmEditText.getText().toString(),userNameEditText.getText ().toString ());
        }
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    private boolean isLoginParamCheck(){
        LogFnc.LogTraceStart(LogFnc.current(),application);
        boolean isChk = true;
        if (TextUtils.isEmpty (userNameEditText.getText().toString())) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            userNameEditText.setError ("err");
            isChk = false;
        }
        if (TextUtils.isEmpty (passEditText.getText().toString())) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            passEditText.setError ("err");
            isChk = false;
        }
        if (TextUtils.isEmpty (passConfirmEditText.getText().toString())) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            passConfirmEditText.setError ("err");
            isChk = false;
        }
        if (!passConfirmEditText.getText ().toString ().equals(passEditText.getText ().toString ())) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            passConfirmEditText.setError ("err");
            isChk = false;
        }

        if (passEditText.getText ().length () < 4) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            passEditText.setError ("err");
            isChk = false;
        }

        if (passConfirmEditText.getText ().length () < 4) {
            LogFnc.Logging(LogFnc.DEBUG,"",LogFnc.current());
            passConfirmEditText.setError ("err");
            isChk = false;
        }

        LogFnc.LogTraceEnd (LogFnc.current (),application);
        return isChk;
    }

    @Override
    public void LoginDone(User loginUser) {

    }

    @Override
    public void RegisterDone(String msg) {
        LogFnc.LogTraceStart(LogFnc.current(),application);
        if (TextUtils.isEmpty (msg)) {
            NCMBQuery<NCMBObject> query = new NCMBQuery<> (Account.TABLE_NAME);
            query.whereEqualTo (Account.COL_USER_ID,"default");
            query.findInBackground (accountCallBack);
            activityListener.showProgress(R.string.user_register);
        }
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    FindCallback<NCMBObject> accountCallBack = new FindCallback<NCMBObject> () {
        @Override
        public void done(List<NCMBObject> list, NCMBException e) {
            LogFnc.LogTraceStart(LogFnc.current(),application);
            if (e == null) {
                for (NCMBObject account : list) {
                    NCMBObject newAccount = new NCMBObject(Account.TABLE_NAME);
                    newAccount.setObjectId (null);
                    newAccount.put (Account.COL_USER_ID, NCMBUser.getCurrentUser ().getObjectId ());
                    newAccount.put (Account.COL_CREATED_USER, NCMBUser.getCurrentUser ().getObjectId ());
                    newAccount.put (Account.COL_UPDATED_USER, NCMBUser.getCurrentUser ().getObjectId ());
                    newAccount.put(Account.COL_ACCOUNT_NAME,account.getString(Account.COL_ACCOUNT_NAME));
                    newAccount.put(Account.COL_ACCOUNT_DIV,account.getInt(Account.COL_ACCOUNT_DIV));
                    newAccount.put(Account.COL_DEL_FLG,account.getBoolean(Account.COL_DEL_FLG));
                    try {
                        newAccount.save ();
                    } catch (NCMBException e1) {
                        e1.printStackTrace ();
                    }
                }
                NCMBQuery<NCMBObject> query = new NCMBQuery<> (Category.TABLE_NAME);
                query.whereEqualTo (Category.COL_USER_ID,"default");
                query.findInBackground (categoryCallBack);
            }
            LogFnc.LogTraceEnd (LogFnc.current (),application);
        }
    };
    FindCallback<NCMBObject> categoryCallBack = new FindCallback<NCMBObject> () {
        @Override
        public void done(List<NCMBObject> list, NCMBException e) {
            LogFnc.LogTraceStart(LogFnc.current(),application);
            if (e == null) {
                for (NCMBObject category : list) {
                    NCMBObject newCategory = new NCMBObject(Category.TABLE_NAME);
                    newCategory.put (Category.COL_USER_ID, NCMBUser.getCurrentUser ().getObjectId ());
                    newCategory.put (Category.COL_CREATED_USER, NCMBUser.getCurrentUser ().getObjectId ());
                    newCategory.put (Category.COL_UPDATED_USER, NCMBUser.getCurrentUser ().getObjectId ());
                    newCategory.put(Category.COL_CATEGORY_NAME,category.getString(Category.COL_CATEGORY_NAME));
                    newCategory.put(Category.COL_CASH_DIV,category.getInt(Category.COL_CASH_DIV));
                    newCategory.put(Category.COL_DEL_FLG,category.getBoolean(Category.COL_DEL_FLG));

                    try {
                        newCategory.save ();
                    } catch (NCMBException e1) {
                        e1.printStackTrace ();
                    }
                }
                User user = new User();
                user.getToAppModel (User.class,NCMBUser.getCurrentUser());
                application.setLoginUser (user);
                SharedPreferences prefer = getContext ().getSharedPreferences(CommonConst.PREF_UPDATE, getContext ().MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefer.edit ();
                editor1.putString (Cash.COL_USER_ID,user.objectId);
                LogFnc.Logging (LogFnc.DEBUG,user.objectId,LogFnc.current ());
                editor1.apply ();
                activityListener.closeProgress();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                activityListener.logout();
            }
            LogFnc.LogTraceEnd (LogFnc.current (),application);
        }
    };
}
