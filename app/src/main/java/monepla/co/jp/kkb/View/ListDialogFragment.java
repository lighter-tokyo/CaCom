package monepla.co.jp.kkb.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import monepla.co.jp.kkb.Common.SpinnerCommon;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.CategoryController;
import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Category;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.KkbApplication;

/**
 * Created by user on 2016/10/23.
 */

public class ListDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener{
    private KkbApplication application;
    public static final String KEY1 = "key1";
    private ListDialogItemClickListener listener;
    private CategoryController categoryController;
    private List<SpinnerCommon> spinnerCommonList = new ArrayList<> ();
    public interface ListDialogItemClickListener {
        void ItemClick(SpinnerCommon spinnerCommon);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ListView listView = new ListView (application);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /** 口座区分セット */
        String division = getArguments ().getString (KEY1);
        if (division == null) {
            return builder.create ();
        }
        if ( division.equals (getString (R.string.kind))) {
            for (CommonConst.AccountDiv accountDiv : CommonConst.AccountDiv.values ()) {
                spinnerCommonList.add (new SpinnerCommon (String.valueOf (accountDiv.getId ()), getString (accountDiv.getStr ())));
            }
        } else if (division.equals (getString (R.string.account))) {

            List<Account> accountList = application.getLoginUser ().getAccountList ();
            for (int i = 0;i < accountList.size ();i++) {
                spinnerCommonList.add (new SpinnerCommon (accountList.get (i).objectId,accountList.get (i).accountName));
            }
        } else if (division.equals (getString (R.string.input))) {
            List<Category> categoryList = CommonUtils.getCategoryList (1);
            for (int i = 0;i < categoryList.size (); i++) {
                spinnerCommonList.add (new SpinnerCommon (categoryList.get (i).objectId,categoryList.get (i).category_name));
            }
        } else {
            List<Category> categoryList = CommonUtils.getCategoryList (0);
            for (int i = 0;i < categoryList.size (); i++) {
                spinnerCommonList.add (new SpinnerCommon (categoryList.get (i).objectId,categoryList.get (i).category_name));
            }
        }

        categoryController = new CategoryController (getContext (),spinnerCommonList);

        listView.setOnItemClickListener (this);
        listView.setAdapter (categoryController);
        builder.setMessage(getArguments ().getString (KEY1))
                .setView (listView);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        application = (KkbApplication)context.getApplicationContext ();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        listener.ItemClick (spinnerCommonList.get (i));
        dismiss ();
    }

    public void setListDialogItemClickListener (ListDialogItemClickListener listener) {
        this.listener = listener;
    }
}
