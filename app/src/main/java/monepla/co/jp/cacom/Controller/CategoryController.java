package monepla.co.jp.cacom.Controller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import monepla.co.jp.cacom.Common.SpinnerCommon;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * Created by user on 2016/07/24.
 * カテゴリスピナーコントローラー
 */
public class CategoryController extends BaseAdapter implements View.OnClickListener{
    /** ドロップダウンリスト  */
    private List<SpinnerCommon> items;
    /** ビュー */
    private View selectView;
    private Context context;
    private CaComApplication application;
    /**
     * インスタンス
     * @param context コンテキスト
     * @param spinnerCommonList ドロップダウンリスト
     */
    public CategoryController(Context context,List<SpinnerCommon> spinnerCommonList) {
        super();
        this.context = context;
        application = (CaComApplication)context.getApplicationContext();
        items = spinnerCommonList;
    }

    @Override
    public int getCount() {
        return items.size ();
    }

    @Override
    public SpinnerCommon getItem(int i) {
        return items.get (i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.
                    inflate(R.layout.spinner_row, null);
        }
        TextView textView = (TextView) convertView.findViewById (R.id.spinner_text);
        textView.setText (items.get (position).name);

        LogFnc.LogTraceEnd (LogFnc.current (),application);
        return convertView;
    }

    @Override
    public View getDropDownView(int position,
                                View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.
                    inflate(R.layout.spinner_row, null);
        }
        String text = items.get(position).name;
        TextView tv = (TextView)convertView.
                findViewById(R.id.spinner_text);
        tv.setText(text);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        if (selectView != null) {
            selectView.setBackgroundColor (ContextCompat.getColor (context,R.color.colorClear));
        }
        view.setBackgroundColor (ContextCompat.getColor (context,R.color.colorAccent));
        selectView = view;
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }
}
