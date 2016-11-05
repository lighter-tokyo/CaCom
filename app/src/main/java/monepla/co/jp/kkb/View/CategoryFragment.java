package monepla.co.jp.kkb.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CategoryFragment extends BaseFragment
        implements View.OnClickListener,DoneCallback,DatePickerDialog.OnDateSetListener,ListDialogFragment.ListDialogItemClickListener {
    /** 区分 */
    private int categoryDiv;
    /** 金額 */
    private long amount;
    /** シングルトーン */
    private static CategoryFragment fragment = new CategoryFragment ();
    /** ビュー */
    private TextView typeTextView;
    private AppCompatEditText appCompatEditText;
    private AppCompatSpinner appCompatSpinnerOrigin;
    private AppCompatSpinner appCompatSpinner;
    private TextView dateTextView;
    /** カレンダー */
    private Calendar calendar;
    /** コントローラー */
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
     * @param amount Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(int categoryDiv,long amount,String id) {
        Bundle args = new Bundle ();
        args.putInt (ARG_PARAM1, categoryDiv);
        args.putLong (ARG_PARAM2, amount);
        args.putString (ARG_PARAM3,id);
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
        TextView amountTextView = (TextView) contentView.findViewById (R.id.category_amount);
        amountTextView.setText (String.valueOf (amount));
        typeTextView = (TextView) contentView.findViewById (R.id.category_type);
        appCompatSpinnerOrigin = (AppCompatSpinner) contentView.findViewById (R.id.category_origin);
        appCompatSpinner = (AppCompatSpinner) contentView.findViewById (R.id.category_category);
        dateTextView = (TextView) contentView.findViewById (R.id.date_picker_text);
        appCompatEditText = (AppCompatEditText) contentView.findViewById (R.id.category_detail_edit_text);

        /** カレンダー初期値セット */
        calendar = Calendar.getInstance ();
        if (getArguments ().getString (ARG_PARAM3) != null) {
            cash = new Select ().from (Cash.class).where (Cash.COL_OBJECT_ID + "=?",getArguments ().getString (ARG_PARAM3)).executeSingle ();
            calendar.setTime (cash.cashDate);
            AppCompatButton appCompatButton = (AppCompatButton) contentView.findViewById (R.id.category_button);
            appCompatButton.setText (R.string.update);
            appCompatEditText.setText (cash.detail);
        }
        dateTextView.setText (String.valueOf (calendar.get(Calendar.YEAR)) + "/" + String.valueOf (calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf (calendar.get(Calendar.DAY_OF_MONTH)));


        /** スピナー情報セット */
        setSpinner ();

        /** リスナーセット */
        dateTextView.setOnClickListener (this);
        contentView.findViewById (R.id.category_button).setOnClickListener (this);

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


    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current ());
        switch (view.getId ()) {
            case R.id.date_picker_text:
                /** カレンダーダイアログ */
                DatePickerDialog dpd = DatePickerDialog.newInstance (
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get (Calendar.MONTH),
                        calendar.get (Calendar.DAY_OF_MONTH));
                Activity appCompatActivity = (Activity) getContext ();
                dpd.show (appCompatActivity.getFragmentManager (),"Date");
                break;
            case R.id.category_button:
                /** 登録ボタン処理 */
                registerCash ();
                break;
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void done(NCMBException e) {
        LogFnc.LogTraceStart (LogFnc.current ());
        /** プログレス削除 */
        activityListener.closeProgress ();
        if (e != null) {
            e.printStackTrace ();
            LogFnc.Logging (LogFnc.ERROR, e.getMessage (), LogFnc.current ());
        } else {
            /** ホームへ遷移 */
        FragmentManager fm = getFragmentManager ();
            FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(0);
            fm.popBackStack(entry.getId(), POP_BACK_STACK_INCLUSIVE);
//            getFragmentManager ().beginTransaction ().replace (R.id.fragment_view, HomeListFragment.newInstance ()).commit ();
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * データ登録処理
     */
    private void registerCash() {
        LogFnc.LogTraceStart (LogFnc.current ());

        /** サーバーデータ登録 */
        NCMBObject ncmbObject = new NCMBObject (Cash.TABLE_NAME);
        Cash cash = new Cash ();
        /** 更新の場合検索 */
        if (getArguments ().getString (ARG_PARAM3,null) != null) {
            ncmbObject.setObjectId (getArguments ().getString (ARG_PARAM3,null));
            cash = new Select ().from (Cash.class).where (Cash.COL_OBJECT_ID + "=?",getArguments ().getString (ARG_PARAM3,null)).executeSingle ();
        }
        ncmbObject.put (Cash.COL_USER_ID,application.getLoginUser ().objectId);
        ncmbObject.put (Cash.COL_AMOUNT,amount);
        CommonConst.CategoryDiv getCategoryDiv = CommonUtils.getCategoryDiv (categoryDiv);

        if (getCategoryDiv != null) {
            ncmbObject.put (Cash.COL_CASH_DIV, getCategoryDiv.ordinal ());
        }
        ncmbObject.put (Cash.COL_CASH_DATE,new Date (dateTextView.getText ().toString ()));
        ncmbObject.put (Cash.COL_CATEGORY_ID,categoryController.getItem (appCompatSpinner.getSelectedItemPosition ()).id);
        ncmbObject.put (Cash.COL_ORIGIN_ACCOUNT,categoryControllerOrigin.getItem (appCompatSpinnerOrigin.getSelectedItemPosition ()).id);
        ncmbObject.put (Cash.COL_DETAIL,appCompatEditText.getText ().toString ());
        ncmbObject.put (Cash.COL_DEL_FLG,false);
        ncmbObject.put (Cash.COL_CREATED_USER,application.getLoginUser ().objectId);
        ncmbObject.put (Cash.COL_UPDATED_USER,application.getLoginUser ().objectId);
        cash.getToAppModel (Cash.class,ncmbObject);

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

        List<SpinnerCommon> originList = new ArrayList<> ();
        List<SpinnerCommon> spinnerCommonList = new ArrayList<> ();
        int index1 = 0,index2 = 0;

        if (categoryDiv == CommonConst.CategoryDiv.OUTPUT.getId ()) {/** 出金 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.INPUT.getName ());
            /** 移動先 */
            List<Category> categoryList = CommonUtils.getCategoryList (0);
            for (int i = 0;i < categoryList.size (); i++) {
                spinnerCommonList.add (new SpinnerCommon (categoryList.get (i).objectId,categoryList.get (i).category_name));
                if (cash != null && cash.category_id.equals (categoryList.get (i).objectId)) {
                    index2 = i;
                }
            }
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0;i < accountList.size ();i++) {
                originList.add (new SpinnerCommon (accountList.get (i).objectId,accountList.get (i).accountName));
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    index1 = i;
                }
            }
        } else if (categoryDiv == CommonConst.CategoryDiv.INPUT.getId ()) {/** 入金 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.INPUT.getName ());
            /** 移動先 */
            List<Category> categoryList = CommonUtils.getCategoryList (1);
            for (int i = 0;i < categoryList.size (); i++) {
                spinnerCommonList.add (new SpinnerCommon (categoryList.get (i).objectId,categoryList.get (i).category_name));
                if (cash != null && cash.category_id.equals (categoryList.get (i).objectId)) {
                    index2 = i;
                }
            }
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0;i < accountList.size ();i++) {
                originList.add (new SpinnerCommon (accountList.get (i).objectId,accountList.get (i).accountName));
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    index1 = i;
                }
            }
        } else {/** 振替 */
            /** 名称セット */
            typeTextView.setText (CommonConst.CategoryDiv.CHANGE.getName ());
            /** 移動元 */
            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0;i < accountList.size ();i++) {
                spinnerCommonList.add (new SpinnerCommon (accountList.get (i).objectId,accountList.get (i).accountName));
                if (cash != null && cash.category_id.equals (accountList.get (i).objectId)) {
                    index2 = i;
                }
            }
            /** 移動先 */
            for (int i = 0;i < accountList.size ();i++) {
                originList.add (new SpinnerCommon (accountList.get (i).objectId,accountList.get (i).accountName));
                if (cash != null && cash.originAccountId.equals (accountList.get (i).objectId)) {
                    index1 = i;
                }
            }
        }
        categoryControllerOrigin = new CategoryController (getContext (),originList);
        categoryController = new CategoryController (getContext (),spinnerCommonList);
        appCompatSpinnerOrigin.setAdapter (categoryControllerOrigin);
        appCompatSpinner.setAdapter (categoryController);
        if (cash != null) {
            appCompatSpinnerOrigin.setSelection (index1);
            appCompatSpinner.setSelection (index2);
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void ItemClick(SpinnerCommon spinnerCommon) {

    }
}
