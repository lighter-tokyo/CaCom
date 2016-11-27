package monepla.co.jp.kkb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.Model.User;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.KkbApplication;
import monepla.co.jp.kkb.Utils.LogFnc;
import monepla.co.jp.kkb.Utils.Queue;
import monepla.co.jp.kkb.View.AccountListView;
import monepla.co.jp.kkb.View.HomeListView;
import monepla.co.jp.kkb.View.LoginFragment;
import monepla.co.jp.kkb.View.OtherView;
import monepla.co.jp.kkb.View.PassCodeFragment;

import static monepla.co.jp.kkb.Utils.CommonUtils.getQuery;
import static monepla.co.jp.kkb.Utils.CommonUtils.updateInstallation;

/**
 * メインActivity
 * @see AppCompatActivity
 * @inheritDoc
 * @see NavigationView.OnNavigationItemSelectedListener ドロワー選択リスナー
 * @see BaseFragment.OnFragmentInteractionListener Fragment変更イベント
 * @see ActivityListener Activity通知リスナー
 * @see com.nifty.cloud.mb.core.FindCallback
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseFragment.OnFragmentInteractionListener,
        ActivityListener,
        View.OnClickListener,
        Queue.onObserver {

    /** アプリケーション */
    private KkbApplication application;
    /** プログレス */
    private ProgressDialog progressDialog;
    /** ツールバー */
    private Toolbar toolbar;
    /** ナビゲーションドロワー */
    private NavigationView navigationView;
    /** ドロワー */
    private DrawerLayout drawer;
    /** */
    private ActionBarDrawerToggle actionBarDrawerToggle;
    /** ユーザ名 */
    private TextView textView;
    private HomeListView homeListView;
    private AccountListView accountListView;
    private OtherView otherView;
    private FrameLayout frameLayout;
    private Queue queue;
    /**
     * create
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogFnc.LogTraceStart(LogFnc.current());
        setContentView(R.layout.activity_main);
        /** toolbar設定 */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        pushSetting();
        /** アプリケーション取得 */
        application = (KkbApplication)getApplicationContext ();

        /** ドロワー設定 */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById (R.id.main_view);

        if (queue == null) {
            queue = Queue.getInstance ();
        }
        queue.setOnObserver (this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(actionBarDrawerToggle);
            drawer.closeDrawer (Gravity.LEFT);
        }
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setToolbarNavigationClickListener (this);
        /** ナビゲーションドロワー設定 */
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener (this);
            // header view inflate
            View drawerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
            textView = (TextView) drawerLayout.findViewById (R.id.nav_header_user_name);
        }

        /** プログレス設定 */
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        /** カテゴリー取得 */

        /** 自動ログイン */
        login();

        getSupportFragmentManager().addOnBackStackChangedListener (new FragmentManager.OnBackStackChangedListener () {
            @Override
            public void onBackStackChanged() {
                shouldDisplayHomeUp();
            }
        });
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * 戻るボタン押下時
     */
    @Override
    public void onBackPressed() {
        LogFnc.LogTraceStart(LogFnc.current());

        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            /** ドロワーが開いていたら閉じる */
            LogFnc.Logging(LogFnc.DEBUG,"ドロワー閉じる",LogFnc.current());
            drawer.closeDrawer(GravityCompat.START);
        } else {
            LogFnc.Logging(LogFnc.DEBUG,"aaa",LogFnc.current());
            frameLayout.setVisibility (View.VISIBLE);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            homeListView.postInvalidate();
            super.onBackPressed();
        }
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * fragmentアタッチ時
     * @param fragment フラグメント
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        LogFnc.LogTraceStart(LogFnc.current());
        super.onAttachFragment (fragment);


        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * アプリ閉じる
     */
    @Override
    public void onDestroy() {
        LogFnc.LogTraceStart(LogFnc.current());
        super.onDestroy ();
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * メニュー作成
     * @param menu メニュー
     * @return override可否
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * メニュー選択時
     * @param item メニュー
     * @return スーパークラスメソッド
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogFnc.LogTraceStart(LogFnc.current());
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager ().popBackStack ();
                return true;
        }
        LogFnc.LogTraceEnd(LogFnc.current());
        return super.onOptionsItemSelected(item);
    }

    /**
     * ナビゲーションドロワー選択時
     * @param item メニュー
     * @return スーパークラス有効
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        LogFnc.LogTraceStart(LogFnc.current());
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_deposits_withdrawals) {
            /** 入出金履歴 */
            LogFnc.Logging(LogFnc.DEBUG,"入出金履歴",LogFnc.current());
            frameLayout.removeAllViews ();
            if (homeListView == null) {
                homeListView = new HomeListView (this);
            }
            frameLayout.addView (homeListView);
            homeListView.invalidateTextPaintAndMeasurements ();
        } else if (id == R.id.nav_account_history) {
            /** 口座履歴 */
            LogFnc.Logging(LogFnc.DEBUG,"口座履歴",LogFnc.current());
            frameLayout.removeAllViews ();
            if (accountListView == null) {
                accountListView = new AccountListView (this);
            }
            frameLayout.addView (accountListView);
            accountListView.invalidateTextPaintAndMeasurements ();
        } else if (id == R.id.nav_other) {
            /** その他 */
            LogFnc.Logging(LogFnc.DEBUG,"設定",LogFnc.current());
            frameLayout.removeAllViews ();
            if (otherView == null) {
                otherView = new OtherView (this);
            }
            frameLayout.addView (otherView);
            otherView.invalidateTextPaintAndMeasurements ();
        }


        if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        LogFnc.LogTraceEnd(LogFnc.current());
        return true;
    }

    /**
     * fragmentから戻るボタン押下時
     * @param param 文字列
     */
    @Override
    public void onFragmentInteraction(BaseFragment param) {
        frameLayout.setVisibility (View.VISIBLE);
        if (param == null || param.getTag () == null) return;
        switch (param.getTag ()) {
            case HomeListView.TAG:
                setToolbarTitle (R.string.deposits_withdrawals);
                break;
            case AccountListView.TAG:
                setToolbarTitle (R.string.account);
                break;
        }
        try {
            getSupportFragmentManager ().beginTransaction ().remove (param).commit ();
        } catch (IllegalStateException e) {
            e.printStackTrace ();
        }

    }

    /**
     * 自動ログイン
     */
    public void login() {
        LogFnc.LogTraceStart(LogFnc.current());
        /** ユーザリストを取得 */
        String user_id = CommonUtils.getUpdateShare (this,Cash.COL_USER_ID);
        if (!TextUtils.isEmpty (user_id)) {
            User userList = new Select ().from (User.class).where (User.COL_OBJECT_ID + " = ?", user_id).executeSingle ();
            if (!TextUtils.isEmpty (user_id) && userList != null && userList.objectId.equals (user_id)) {
                application.setLoginUser (userList);
                homeListView = new HomeListView (this);
                homeListView.invalidateTextPaintAndMeasurements ();
                frameLayout.addView (homeListView);
                if (textView != null) textView.setText (userList.user_name);
                getCategory ();
                getSupportActionBar ().setHomeButtonEnabled (false);
                getSupportActionBar ().setDisplayHomeAsUpEnabled (false);
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                return;
            }
        }
        frameLayout.setVisibility (View.INVISIBLE);
        getSupportFragmentManager ().beginTransaction ().add (R.id.fragment_no_toolbar, LoginFragment.newInstance (),HomeListView.TAG).commit ();
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * タイトル設定
     * @param title タイトル
     */
    @Override
    public void setToolbarTitle(int title) {
        LogFnc.LogTraceStart(LogFnc.current());
        toolbar.setTitle (title);
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * プログレス表示
     * @param title タイトル
     */
    @Override
    public void showProgress(int title) {
        LogFnc.LogTraceStart(LogFnc.current());
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        LogFnc.LogTraceEnd(LogFnc.current());
    }

    /**
     * プログレス非表示
     */
    @Override
    public void closeProgress() {
        LogFnc.LogTraceStart(LogFnc.current());
        progressDialog.dismiss ();
        LogFnc.LogTraceEnd(LogFnc.current());
    }

    @Override
    public void showDrawer() {
        drawer.setDrawerLockMode (DrawerLayout.LOCK_MODE_UNDEFINED);
    }

    @Override
    public void closeDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode (DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        frameLayout.setVisibility (View.GONE);

    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        if (canBack && getSupportActionBar () != null) {
            getSupportActionBar ().setHomeButtonEnabled (true);
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
            closeDrawer ();
        } else if (getSupportActionBar () != null){
            getSupportActionBar ().setHomeButtonEnabled (false);
            getSupportActionBar ().setDisplayHomeAsUpEnabled (false);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        }
    }

    @Override
    public void addStackFragment(BaseFragment fragment) {
        getSupportFragmentManager ().beginTransaction ().add (R.id.fragment_view,fragment,fragment.getClass ().getSimpleName ())
                .addToBackStack (fragment.getClass ().getSimpleName ()).commit ();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawer.getDrawerLockMode (Gravity.LEFT);
        frameLayout.setVisibility (View.GONE);
    }

    private void getCategory() {
        done (Category.TABLE_NAME);
        done (Account.TABLE_NAME);
        done (Cash.TABLE_NAME);
        /** カテゴリ取得 */
        NCMBQuery<NCMBObject> query = getQuery(this,Category.TABLE_NAME);
        query.whereEqualTo (Category.COL_DEL_FLG,false);
        query.whereEqualTo(Category.COL_USER_ID,application.getLoginUser().objectId);
        queue.put (query);
        /** 口座取得 */
        query = getQuery (this,Account.TABLE_NAME);
        query.whereEqualTo (Account.COL_USER_ID,application.getLoginUser ().objectId);
        queue.put (query);
        /** 入出金取得 */
        query = getQuery (this,Cash.TABLE_NAME);
        query.whereEqualTo (Cash.COL_USER_ID,application.getLoginUser ().objectId);
        queue.put (query);
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current ());
        onBackPressed ();
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void onStart() {
        super.onStart ();
        LogFnc.LogTraceStart (LogFnc.current ());
        String passCode = CommonUtils.getUpdateShare (this,CommonConst.PREF_PASS_CODE);
        if (passCode != null) {
            getSupportFragmentManager ().beginTransaction ().add (R.id.fragment_no_toolbar, PassCodeFragment.newInstance (true),HomeListView.TAG).commit ();
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void done(String tableName) {
        LogFnc.LogTraceStart (LogFnc.current ());
        if (tableName.equals (Category.TABLE_NAME)) {
            application.setCategoryHashMap ();
            CommonUtils.setUpdateShare (this,Account.TABLE_NAME);
        }
        if (tableName.equals (Account.TABLE_NAME)) {
            List<Account> accountList = new Select ().from (Account.class).where (Account.COL_DEL_FLG +"=?",false).execute ();
            application.getLoginUser ().getAccountList ().clear ();
            application.getLoginUser ().getAccountList ().addAll (accountList);
        }
        if (tableName.equals (Cash.TABLE_NAME)) {
            List<Cash> cashList = new Select ().from (Cash.class).where (Cash.COL_DEL_FLG +"=?",false).execute ();
            application.getLoginUser ().getCashListMap ().clear ();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM");
            for (Cash cash : cashList) {
                String cashDate = simpleDateFormat.format (cash.cashDate);
                if (!application.getLoginUser ().getCashListMap ().containsKey (cashDate)) {
                    ArrayList<Cash> cashList2 = new ArrayList<> ();
                    cashList2.add (cash);
                    application.getLoginUser ().getCashListMap ().put (cashDate,cashList2);
                } else {
                    application.getLoginUser ().getCashListMap ().get (cashDate).add (cash);
                }
            }
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void logout() {
        frameLayout.removeAllViews ();
        frameLayout.setVisibility(View.INVISIBLE);
        login();
    }
}
