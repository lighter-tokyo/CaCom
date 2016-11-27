package monepla.co.jp.kkb.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Common.SpinnerCommon;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.CategoryController;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.LogFnc;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static monepla.co.jp.kkb.Utils.CommonUtils.getQuery;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CategoryFragment extends BaseFragment
        implements DoneCallback,DatePickerDialog.OnDateSetListener,FindCallback<NCMBObject> {
    /**
     * 区分
     */
    private int categoryDiv;
    /**
     * 金額
     */
    private long amount;
    /**
     * シングルトーン
     */
    private static CategoryFragment fragment = new CategoryFragment ();
    /**
     * ビュー
     */
    @InjectView(R.id.category_type)
    TextView typeTextView;
    @InjectView(R.id.category_amount)
    TextView amountTextView;
    @InjectView(R.id.category_detail_edit_text)
    AppCompatEditText appCompatEditText;
    @InjectView(R.id.category_origin)
    TextView appCompatSpinnerOrigin;
    @InjectView(R.id.category_category)
    TextView appCompatSpinner;
    @InjectView(R.id.date_picker_text)
    TextView dateTextView;
    @InjectView(R.id.category_button)
    AppCompatButton appCompatButton;
    private String accountId;
    private String categoryId;
    /**
     * カレンダー
     */
    private Calendar calendar;
    /**
     * コントローラー
     */
    private CategoryController categoryControllerOrigin;
    private CategoryController categoryController;
    private Cash cash = null;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryDiv Parameter 1.
     * @param amount      Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(int categoryDiv, long amount, String id) {
        Bundle args = new Bundle ();
        args.putInt (ARG_PARAM1, categoryDiv);
        args.putLong (ARG_PARAM2, amount);
        args.putString (ARG_PARAM3, id);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments () != null) {
            categoryDiv = getArguments ().getInt (ARG_PARAM1);
            amount = getArguments ().getLong (ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogFnc.LogTraceStart (LogFnc.current ());
        /** ビューセット  */
        View contentView = inflater.inflate (R.layout.fragment_category, container, false);
        ButterKnife.inject (this, contentView);
        amountTextView.setText (String.valueOf (amount));

        /** カレンダー初期値セット */
        calendar = Calendar.getInstance ();
        if (getArguments ().getString (ARG_PARAM3) != null) {
            cash = new Select ().from (Cash.class).where (Cash.COL_OBJECT_ID + "=?", getArguments ().getString (ARG_PARAM3)).executeSingle ();
            calendar.setTime (cash.cashDate);
            appCompatButton.setText (R.string.update);
            appCompatEditText.setText (cash.detail);
        }
        dateTextView.setText (String.valueOf (calendar.get (Calendar.YEAR)) + "/" + String.valueOf (calendar.get (Calendar.MONTH) + 1) + "/" + String.valueOf (calendar.get (Calendar.DAY_OF_MONTH)));


        /** スピナー情報セット */
        setSpinner ();

        /** ドロワー */
        activityListener.closeDrawer ();
        /** タイトル */
        activityListener.setToolbarTitle (R.string.category);
        LogFnc.LogTraceEnd (LogFnc.current ());
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);

    }

    @Override
    public void onDetach() {
        super.onDetach ();
    }

    @Override
    public void onResume() {
        super.onResume ();

    }

    @OnClick(R.id.date_picker_text)
    public void dateClick() {
        /** カレンダーダイアログ */
        DatePickerDialog dpd = DatePickerDialog.newInstance (
                this,
                calendar.get (Calendar.YEAR),
                calendar.get (Calendar.MONTH),
                calendar.get (Calendar.DAY_OF_MONTH));
        Activity appCompatActivity = (Activity) getContext ();
        dpd.show (appCompatActivity.getFragmentManager (), "Date");
    }

    @OnClick(R.id.category_category)
    public void categoryClick() {
        ListDialogFragment listDialogFragment = new ListDialogFragment ();
        Bundle args = new Bundle ();
        if (categoryDiv == CommonConst.CategoryDiv.OUTPUT.getId ()) {/** 出金 */
            args.putString (ListDialogFragment.KEY1, getString (R.string.output));
        } else if (categoryDiv == CommonConst.CategoryDiv.INPUT.getId ()) {/** 入金 */
            args.putString (ListDialogFragment.KEY1, getString (R.string.input));
        } else {
            args.putString (ListDialogFragment.KEY1, getString (R.string.account));
        }
        listDialogFragment.setListDialogItemClickListener (onCategoryClick);
        listDialogFragment.setArguments (args);
        listDialogFragment.setTargetFragment (this, 100);
        listDialogFragment.show (getFragmentManager (), getString (R.string.kind));
    }

    @OnClick(R.id.category_origin)
    public void accountClick() {
        ListDialogFragment listDialogFragment = new ListDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (ListDialogFragment.KEY1, getString (R.string.account));
        listDialogFragment.setListDialogItemClickListener (onAccountClick);
        listDialogFragment.setArguments (args);
        listDialogFragment.setTargetFragment (this, 100);
        listDialogFragment.show (getFragmentManager (), getString (R.string.kind));
    }

    @Override
    public void done(NCMBException e) {
        LogFnc.LogTraceStart (LogFnc.current ());
        if (e != null) {
            e.printStackTrace ();
            LogFnc.Logging (LogFnc.ERROR, e.getMessage (), LogFnc.current ());
        } else {
            NCMBQuery<NCMBObject> query = getQuery (application.getApplicationContext(),Cash.TABLE_NAME);
            query.whereEqualTo (Cash.COL_USER_ID,application.getLoginUser ().objectId);
            query.findInBackground(this);

//            getFragmentManager ().beginTransaction ().replace (R.id.fragment_view, HomeListFragment.newInstance ()).commit ();
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * データ登録処理
     */
    @OnClick(R.id.category_button)
    public void registerCash() {
        LogFnc.LogTraceStart (LogFnc.current ());
        if (TextUtils.isEmpty(appCompatSpinnerOrigin.getText().toString())) {
            appCompatSpinnerOrigin.setError("エラー");
            return;
        }
        if (TextUtils.isEmpty(appCompatSpinner.getText().toString())) {
            appCompatSpinner.setError("エラー");
            return;
        }

        /** サーバーデータ登録 */
        NCMBObject ncmbObject = new NCMBObject (Cash.TABLE_NAME);
        /** 更新の場合検索 */
        if (getArguments ().getString (ARG_PARAM3, null) != null) {
            ncmbObject.setObjectId (getArguments ().getString (ARG_PARAM3, null));
        }
        ncmbObject.put (Cash.COL_USER_ID, application.getLoginUser ().objectId);
        ncmbObject.put (Cash.COL_AMOUNT, amount);
        CommonConst.CategoryDiv getCategoryDiv = CommonUtils.getCategoryDiv (categoryDiv);

        if (getCategoryDiv != null) {
            ncmbObject.put (Cash.COL_CASH_DIV, getCategoryDiv.ordinal ());
        }
        ncmbObject.put (Cash.COL_CASH_DATE, new Date (dateTextView.getText ().toString ()));
        ncmbObject.put (Cash.COL_CATEGORY_ID, categoryId);
        ncmbObject.put (Cash.COL_ORIGIN_ACCOUNT, accountId);
        ncmbObject.put (Cash.COL_DETAIL, appCompatEditText.getText ().toString ());
        ncmbObject.put (Cash.COL_DEL_FLG, false);
        ncmbObject.put (Cash.COL_CREATED_USER, application.getLoginUser ().objectId);
        ncmbObject.put (Cash.COL_UPDATED_USER, application.getLoginUser ().objectId);

        activityListener.showProgress (R.string.category);
        ncmbObject.saveInBackground (this);

        LogFnc.LogTraceEnd (LogFnc.current ());
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        LogFnc.LogTraceStart (LogFnc.current ());
        dateTextView.setText (String.valueOf (year) + "/" + String.valueOf (monthOfYear + 1) + "/" + String.valueOf (dayOfMonth));
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * スピナーセット
     */
    protected void setSpinner() {
        LogFnc.LogTraceStart (LogFnc.current ());

        if (categoryDiv == CommonConst.CategoryDiv.OUTPUT.getId ()) {/** 出金 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.OUTPUT.getName ());
            /** 移動先 */
            List<Category> categoryList = CommonUtils.getCategoryList (0);
            for (int i = 0; i < categoryList.size (); i++) {
                if (cash != null && cash.category_id.equals (categoryList.get (i).objectId)) {
                    categoryId = categoryList.get (i).objectId;
                }
            }
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0; i < accountList.size (); i++) {
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    accountId = accountList.get (i).objectId;
                }
            }
            if (TextUtils.isEmpty (categoryId) && TextUtils.isEmpty (accountId)) {
                appCompatSpinner.setHint (R.string.category);
                appCompatSpinnerOrigin.setHint (R.string.account);
            }
        } else if (categoryDiv == CommonConst.CategoryDiv.INPUT.getId ()) {/** 入金 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.INPUT.getName ());
            /** 移動先 */
            List<Category> categoryList = CommonUtils.getCategoryList (1);
            for (int i = 0; i < categoryList.size (); i++) {
                if (cash != null && cash.category_id.equals (categoryList.get (i).objectId)) {
                    categoryId = categoryList.get (i).objectId;
                }
            }
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0; i < accountList.size (); i++) {
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    accountId = accountList.get (i).objectId;
                }
            }
            if (TextUtils.isEmpty (categoryId) && TextUtils.isEmpty (accountId)) {
                appCompatSpinner.setHint (R.string.category);
                appCompatSpinnerOrigin.setHint (R.string.account);
            }
        } else {/** 振替 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.CHANGE.getName ());
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0; i < accountList.size (); i++) {
                if (cash != null && cash.category_id.equals (accountList.get (i).objectId)) {
                    categoryId = accountList.get (i).objectId;
                }
            }
            /** 移動先 */
            for (int i = 0; i < accountList.size (); i++) {
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    accountId = accountList.get (i).objectId;
                }
            }
            if (TextUtils.isEmpty (categoryId) && TextUtils.isEmpty (accountId)) {
                appCompatSpinner.setHint (R.string.transfer_source);
                appCompatSpinnerOrigin.setHint (R.string.transfer_destination);
            }
        }

        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    ListDialogFragment.ListDialogItemClickListener onAccountClick = new ListDialogFragment.ListDialogItemClickListener () {
        @Override
        public void ItemClick(SpinnerCommon spinnerCommon) {
            accountId = spinnerCommon.id;
            appCompatSpinnerOrigin.setText (spinnerCommon.name);
        }
    };

    ListDialogFragment.ListDialogItemClickListener onCategoryClick = new ListDialogFragment.ListDialogItemClickListener () {

        @Override
        public void ItemClick(SpinnerCommon spinnerCommon) {
            categoryId = spinnerCommon.id;
            appCompatSpinner.setText (spinnerCommon.name);
        }
    };

    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        for (NCMBObject object : list) {
            Cash cash = new Select().from(Cash.class).where(Cash.COL_OBJECT_ID + " = ? ", object.getObjectId()).executeSingle();
            if (cash == null) {
                /** インサート */
                cash = new Cash();
            }
            cash.getToAppModel(Cash.class, object);
        }
        /** ホームへ遷移 */
        activityListener.closeProgress();
        FragmentManager fm = getFragmentManager ();
        FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt (0);
        fm.popBackStack (entry.getId (), POP_BACK_STACK_INCLUSIVE);

    }
}