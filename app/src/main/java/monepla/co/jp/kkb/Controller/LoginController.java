package monepla.co.jp.kkb.Controller;

import android.text.TextUtils;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.LoginCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * ログイン共通処理
 * Created on 2016/07/12.
 * @see com.nifty.cloud.mb.core.DoneCallback
 * @see com.nifty.cloud.mb.core.LoginCallback
 */
public class LoginController implements DoneCallback,LoginCallback{

    /** コールバック */
    private LoginCallback loginCallback;
    /** Activityリスナー */
    private ActivityListener activityListener;

    /**
     * 登録処理
     * @param e エラー
     */
    @Override
    public void done(NCMBException e) {
        LogFnc.LogTraceStart(LogFnc.current());
        if (e != null) {
            LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
            loginCallback.RegisterDone (e.getMessage ());
        } else {
            loginCallback.RegisterDone (null);
        }
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * ログイン処理
     * @param ncmbUser ユーザー
     * @param e エラー
     */
    @Override
    public void done(NCMBUser ncmbUser, NCMBException e) {
        LogFnc.LogTraceStart(LogFnc.current());
        User user = new User ();
        if (e != null) {
            /** エラーの場合 */
            user.setErr (e);
            e.printStackTrace ();
            LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
            loginCallback.LoginDone (user);
        } else {
            /** 正常 */
            user = new Select ().from (User.class).where (User.COL_OBJECT_ID + " = ? ",ncmbUser.getObjectId ()).executeSingle ();
            if (user == null || TextUtils.isEmpty (user.objectId)) {
                /** DBになければ */
                user = setUser (user,ncmbUser);
            }
            loginCallback.LoginDone (user);
        }

        activityListener.closeProgress ();
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * ログインコールバック
     */
    public interface LoginCallback{
        /**
         * ログイン
         * @param loginUser ユーザー
         */
        void LoginDone(User loginUser);

        /**
         * 登録
         * @param msg メッセージ
         */
        void RegisterDone(String msg);
    }

    /**
     * ログインコールバック設定
     * @param callback ログインコールバック
     * @param activityListener Activityリスナー
     */
    public void setLoginCallback(LoginCallback callback,ActivityListener activityListener) {
        LogFnc.LogTraceStart(LogFnc.current());
        this.loginCallback = callback;
        this.activityListener = activityListener;
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * ログイン
     * @param userName ユーザ名
     * @param pass パスワード
     */
    public void login(String userName,String pass){
        LogFnc.LogTraceStart(LogFnc.current());
        try {
            activityListener.showProgress (R.string.login_button);
            NCMBUser.loginInBackground (userName,pass,this);
        } catch (NCMBException e) {
            LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
            User user = new User ();
            user.setErr (e);
            loginCallback.LoginDone (user);
        }
        LogFnc.LogTraceEnd (LogFnc.current());
    }

    /**
     * 登録処理
     * @param pass パスワード
     * @param userName ユーザ名
     */
    public void register(String pass, String userName) {
        LogFnc.LogTraceStart(LogFnc.current());
        if (loginCallback != null) {
            LogFnc.Logging(LogFnc.INFO,"登録処理開始",LogFnc.current());
            NCMBUser user = new NCMBUser ();
            user.setUserName (userName);
            user.setPassword (pass);
            activityListener.showProgress (R.string.login_button);
            user.signUpInBackground (this);
        }
        LogFnc.LogTraceStart(LogFnc.current());
    }

    /**
     * ユーザ設定
     * @param user ユーザ
     * @param ncmbUser 取得ユーザ
     * @return ユーザ
     */
    private User setUser (User user,NCMBUser ncmbUser) {
        boolean updateFlg = false;
        NCMBObject ncmbObject;
        ncmbObject = ncmbUser;
        user = new User ();
        user.objectId = ncmbUser.getObjectId ();
        user.user_name = ncmbUser.getUserName ();
        if (ncmbUser.getMailAddress () != null) {
            user.mailAddress = ncmbUser.getMailAddress ();
        }
        if (ncmbUser.getAuthData () != null) {
            user.authData = ncmbUser.getAuthData ().toString ();
        }
        try {
            user.push_flg = ncmbUser.getBoolean (User.COL_PUSH_FLG);
        } catch (Exception e1) {
            ncmbObject.put (User.COL_CREATED_USER, user.objectId);
            ncmbObject.put (User.COL_CREATED_USER, user.objectId);
            ncmbObject.put (User.COL_PUSH_FLG, true);
            ncmbObject.put (User.COL_DEL_FLG, false);
            updateFlg = true;
            e1.printStackTrace ();
            LogFnc.Logging (LogFnc.ERROR, e1.getMessage (), LogFnc.current ());
        }
        user.createdUser = ncmbUser.getObjectId ();
        user.updatedUser = ncmbUser.getObjectId ();
        user.createDate = ncmbUser.getCreateDate ();
        user.updateDate = ncmbUser.getUpdateDate ();
        user.save ();

        if (updateFlg) {
            try {
                ncmbObject.save ();
            } catch (NCMBException e1) {
                e1.printStackTrace ();
                LogFnc.Logging (LogFnc.ERROR, e1.getMessage (), LogFnc.current ());
            }
        }

        return user;
    }
}
