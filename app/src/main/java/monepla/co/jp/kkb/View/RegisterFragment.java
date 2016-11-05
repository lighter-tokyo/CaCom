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

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.LoginController;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * 登録フラグメント
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @see monepla.co.jp.kkb.Common.BaseFragment
 * @see android.view.View.OnClickListener
 * @see monepla.co.jp.kkb.Controller.LoginController.LoginCallback
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
        LogFnc.LogTraceStart(LogFnc.current());
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
        loginController = new LoginController ();
        loginController.setLoginCallback (this,activityListener);

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
        loginController = null;
        LogFnc.LogTraceEnd(LogFnc.current());
    }


    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart(LogFnc.current());
        /** 登録チェック */
        if (isLoginParamCheck()) {
            LogFnc.Logging(LogFnc.DEBUG,"登録開始",LogFnc.current());
            loginController.register (
                    passConfirmEditText.getText().toString(),userNameEditText.getText ().toString ());
        }
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    private boolean isLoginParamCheck(){
        LogFnc.LogTraceStart(LogFnc.current());
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

        LogFnc.LogTraceEnd(LogFnc.current());
        return isChk;
    }

    @Override
    public void LoginDone(User loginUser) {

    }

    @Override
    public void RegisterDone(String msg) {
        if (TextUtils.isEmpty (msg)) {
            NCMBQuery<NCMBObject> query = new NCMBQuery<> (Account.TABLE_NAME);
            query.whereEqualTo (Account.COL_USER_ID,"default");
            query.findInBackground (accountCallBack);

        }
    }

    FindCallback<NCMBObject> accountCallBack = new FindCallback<NCMBObject> () {
        @Override
        public void done(List<NCMBObject> list, NCMBException e) {
            if (e == null) {
                List<NCMBObject> accountList = new ArrayList<> ();
                for (NCMBObject account : list) {
                    account.setObjectId (null);
                    account.put (Account.COL_USER_ID, NCMBUser.getCurrentUser ().getObjectId ());
                    try {
                        account.save ();
                    } catch (NCMBException e1) {
                        accountList.add (account);
                        e1.printStackTrace ();
                    }
                }
                NCMBQuery<NCMBObject> query = new NCMBQuery<> (Category.TABLE_NAME);
                query.whereEqualTo (Category.COL_USER_ID,null);
                query.findInBackground (categoryCallBack);
            }
        }
    };
    FindCallback<NCMBObject> categoryCallBack = new FindCallback<NCMBObject> () {
        @Override
        public void done(List<NCMBObject> list, NCMBException e) {
            if (e == null) {
                JSONArray jsonAry = new JSONArray ();
                for (NCMBObject category : list) {
                    category.setObjectId (null);
                    category.put (Category.COL_USER_ID, NCMBUser.getCurrentUser ().getObjectId ());
                    try {
                        category.save ();
                    } catch (NCMBException e1) {
                        jsonAry.put (category.getObjectId ());
                        e1.printStackTrace ();
                    }
                }
                SharedPreferences preferences = getContext ().getSharedPreferences (CommonConst.PREF_UPDATE,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit ();
                editor.putString (Category.COL_OBJECT_ID,jsonAry.toString ());
                getFragmentManager ().beginTransaction ().replace (R.id.fragment_view,LoginFragment.newInstance ()).commit ();
            }
        }
    };
}
