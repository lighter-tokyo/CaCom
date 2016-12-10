package monepla.co.jp.cacom.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.cacom.Constract.CommonConst;
import monepla.co.jp.cacom.Controller.AccountController;
import monepla.co.jp.cacom.Interface.ActivityListener;
import monepla.co.jp.cacom.Model.Account;
import monepla.co.jp.cacom.Model.Cash;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * Created by user on 2016/09/30.
 */

public class AccountItzView extends LinearLayout {
    private Context context;
    /** コントローラー */
    private AccountController adapter;
    private CaComApplication application;
    public ActivityListener activityListener;
    public AccountItzView(Context context) {
        super (context);
        init (context,null,0);
    }

    @InjectView(R.id.pie_chart)
    PieChart pieChart;

    private void init(Context context, AttributeSet attrs, int defStyle) {
        application = (CaComApplication) context.getApplicationContext ();
        LogFnc.LogTraceStart(LogFnc.current(),application);
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_account_circle, this);
        ButterKnife.inject(this, layout);

        a.recycle ();
/** リセントビューセット */
        activityListener = (ActivityListener)context;
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }
    public void setGraphView() {
        /** リセントビューセット */
        LogFnc.LogTraceStart(LogFnc.current(),application);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setRotationEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // 円グラフに表示するデータ
        List<Entry> entries = new ArrayList<> ();
        HashMap<Account,Long> categoryMap = new HashMap<> ();
        for (Map.Entry<String,List<Cash>> entry : application.getLoginUser ().getCashListMap ().entrySet ()) {
            for (Cash cash : entry.getValue ()) {
                Account account = application.getLoginUser ().getAccount (cash.originAccountId);
                if (categoryMap.containsKey (account) && account != null && cash.cashDiv == CommonConst.CategoryDiv.OUTPUT.ordinal ()) {
                    categoryMap.put (account,categoryMap.get (account) + cash.amount);
                }
                if (!categoryMap.containsKey (account) && account != null && cash.cashDiv == CommonConst.CategoryDiv.OUTPUT.ordinal ()) {
                    categoryMap.put (account,cash.amount);
                }
            }
        }

        int count = 0;
        List<String> labels = new ArrayList<> ();
        for (Map.Entry<Account,Long> entry : categoryMap.entrySet ()) {
            entries.add(new Entry(entry.getValue (), count));
            labels.add (entry.getKey ().accountName);
            count++;
        }

        PieDataSet dataSet = new PieDataSet (entries, "チャートのラベル");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);


        PieData pieData = new PieData(labels, dataSet);
        pieData.setValueFormatter(new PercentFormatter ());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }
}
