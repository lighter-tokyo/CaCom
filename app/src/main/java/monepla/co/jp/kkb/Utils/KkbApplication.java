package monepla.co.jp.kkb.Utils;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBApplicationController;

import java.util.HashMap;
import java.util.List;

import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.R;

/**
 * アプリケーション
 * Created on 2016/07/12.
 * @see NCMBApplicationController
 */
public class KkbApplication extends NCMBApplicationController {
    /** ログインユーザー */
    private User loginUser;
    /** カテゴリーマップ */
    private HashMap<String,Category> categoryHashMap;
    /**
     * 移動時
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LogFnc.LogTraceStart (LogFnc.current ());
        /** ActiveAndroid初期化 */
        ActiveAndroid.initialize (this);
         /** Nifty初期化*/
        NCMB.initialize(this.getApplicationContext(),getString (R.string.nifty_api_key),getString (R.string.nifty_client_key));
        /** カテゴリー取得 */
        categoryHashMap = new HashMap<> ();
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * 終了時
     */
    @Override
    public void onTerminate(){
        super.onTerminate();
        /** ActiveAndroid終了 */
        ActiveAndroid.dispose ();
    }

    /**
     * Attach
     * @param base context
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * ログインユーザー取得
     * @return ログインユーザー
     */
    public User getLoginUser() {
        return loginUser;
    }

    /**
     * ログインユーザー設定
     * @param loginUser ログインユーザー
     */
    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * カテゴリーマップ取得
     * @return カテゴリーマップ
     */
    public HashMap<String, Category> getCategoryHashMap() {
        return categoryHashMap;
    }

    /**
     * カテゴリーマップ設定
     */
    public void setCategoryHashMap() {
        List<Category> categoryList = new Select ().from (Category.class).where (Category.COL_DEL_FLG +"=?",false).execute ();
        categoryHashMap.clear ();
        for (Category category : categoryList) {
            categoryHashMap.put (category.objectId,category);
        }
    }
}
