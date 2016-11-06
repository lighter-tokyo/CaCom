package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;

import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.LoginController;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * ログインフラグメント
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @see monepla.co.jp.kkb.Common.BaseFragment
 * @see android.view.View.OnClickListener
 * @see LoginController
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener,LoginController.LoginCallback,FindCallback<NCMBObject> {

    /** メールアドレス */
    private AppCompatEditText mailEditText;
    /** パスワード */
    private AppCompatEditText passEditText;
    /** ログインコントローラー */
    private LoginController loginController;
    /** singleton */
    private static LoginFragment fragment = new LoginFragment();

    /**
     * インスタンス
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
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
        LogFnc.LogTraceStart(LogFnc.current());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        /** viewセット */
        mailEditText = (AppCompatEditText)view.findViewById(R.id.login_mail_edit_text);
        passEditText = (AppCompatEditText)view.findViewById(R.id.login_pass_edit_text);
        view.findViewById(R.id.login_button).setOnClickListener(this);
        view.findViewById(R.id.login_register_button).setOnClickListener(this);
        /** フィルターの配列を作成 */
        InputFilter[] filters = new InputFilter[] {CommonUtils.inputFilter};
        /** フィルターの配列をセット */
        mailEditText.setFilters(filters);
        /** ログインコントローラー */
        loginController = new LoginController ();
        loginController.setLoginCallback (this,activityListener);
        activityListener.closeDrawer ();
        LogFnc.LogTraceEnd(LogFnc.current());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogFnc.LogTraceStart(LogFnc.current());
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogFnc.LogTraceStart(LogFnc.current());
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart(LogFnc.current());
        switch(view.getId()) {
            case R.id.login_register_button:
                /** 新規登録画面 */
                LogFnc.Logging (LogFnc.INFO,"新規登録画面",LogFnc.current ());
                getFragmentManager().beginTransaction().addToBackStack(getClass().getSimpleName()).replace(R.id.fragment_no_toolbar,RegisterFragment.newInstance()).commit();
                break;
            case R.id.login_button:
                /** ログインチェック */
                if(isLoginParamCheck()) {
                    LogFnc.Logging(LogFnc.DEBUG,"ログイン開始",LogFnc.current());
                    loginController.login(mailEditText.getText().toString(),passEditText.getText().toString());
                } else {
                    LogFnc.Logging(LogFnc.DEBUG,"ログインエラー",LogFnc.current());
                }
                break;

        }
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * ログインチェック
     * @return 可否
     */
    private boolean isLoginParamCheck(){
        LogFnc.LogTraceStart(LogFnc.current());
        boolean isChk = true;
        if (TextUtils.isEmpty (mailEditText.getText().toString())) {
            mailEditText.setError ("エラー");
            LogFnc.Logging (LogFnc.WARNING,"メールエラー",LogFnc.current ());
            isChk = false;
        }
        if (TextUtils.isEmpty (passEditText.getText().toString())) {
            passEditText.setError ("エラー");
            LogFnc.Logging (LogFnc.WARNING,"パスワード空白",LogFnc.current ());
            isChk = false;
        }
        if (passEditText.getText ().toString ().length () < 4 ) {
            passEditText.setError ("エラー");
            LogFnc.Logging (LogFnc.WARNING,"パスワード４桁未満",LogFnc.current ());
            isChk = false;
        }

        LogFnc.LogTraceEnd(LogFnc.current());
        return isChk;
    }

    @Override
    public void LoginDone(User loginUser) {
        if (loginUser.getErr () == null) {
            application.setLoginUser (loginUser);
            SharedPreferences prefer = getContext ().getSharedPreferences(CommonConst.PREF_UPDATE, getContext ().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit ();
            editor.putString (Cash.COL_USER_ID,loginUser.objectId);
            LogFnc.Logging (LogFnc.DEBUG,loginUser.objectId,LogFnc.current ());
            editor.apply ();
            NCMBQuery<NCMBObject> query = new NCMBQuery<> (Account.TABLE_NAME);
            query.whereEqualTo (Account.COL_USER_ID,loginUser.objectId);
            query.findInBackground(this);

        } else {
            mailEditText.setError (loginUser.getErr ().getCode ());
            passEditText.setError (loginUser.getErr ().getMessage ());
        }
    }

    @Override
    public void RegisterDone(String msg) {

    }


    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        for (NCMBObject object : list) {
            Account account = new Account ();
            account.getToAppModel (Account.class,object);
        }
        getFragmentManager().beginTransaction().remove(this).commit();
        onButtonPressed(this);
        activityListener.logout();
        onDetach();
    }
}
