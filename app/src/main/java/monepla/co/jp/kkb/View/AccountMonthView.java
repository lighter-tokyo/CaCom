package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.activeandroid.query.Select;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Controller.AccountController;
import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.CommonUtils;
import monepla.co.jp.kkb.Utils.KkbApplication;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/09/30.
 */

public class AccountMonthView extends LinearLayout {
    private Context context;
    /** コントローラー */
    private AccountController adapter;
    private KkbApplication application;
    public ActivityListener activityListener;
    public AccountMonthView(Context context) {
        super (context);
        init (context,null,0);
    }

    @InjectView(R.id.bar_chart)
    BarChart barChart;

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_account_graph, this);
        ButterKnife.inject(this, layout);
        application = (KkbApplication) context.getApplicationContext ();
        a.recycle ();
/** リセントビューセット */
        activityListener = (ActivityListener)context;
    }
    public void setGraphView() {
        /** リセントビューセット */
        LogFnc.LogTraceStart (LogFnc.current ());
        List<Cash> cashList = new Select ()
                .from (Cash.class)
                .where (Cash.COL_USER_ID + "= ?" , application.getLoginUser ().objectId)
                .where (Cash.COL_CASH_DIV + "=?" , CommonConst.CategoryDiv.OUTPUT.ordinal ())
                .where (Cash.COL_DEL_FLG + "=?", false)
                .orderBy (Cash.COL_CASH_DATE +" DESC")
                .execute ();
        HashMap<String,List<Cash>> cashMonthHashMap = new HashMap<> ();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM");
        for (Cash cash : cashList) {

            if (cashMonthHashMap.containsKey (cash.originAccountId)) {
                cashMonthHashMap.get (cash.originAccountId).add (cash);
            } else {
                List<Cash> tmpList = new ArrayList<> ();
                tmpList.add (cash);
                cashMonthHashMap.put (cash.originAccountId,tmpList);
            }
        }

        int x = 0;
        //
        List<IBarDataSet> barDataSets = new ArrayList<>();

        List<String> xValues = CommonUtils.getYearMonthList (application.getLoginUser ().createDate);
        String tmp = "";
        HashMap<String,HashMap<String,Long>> accountHashMap = new HashMap<> ();

        for (Map.Entry<String,List<Cash>> entry : cashMonthHashMap.entrySet ()) {
            HashMap<String,Long> tmpMap = new HashMap<> ();
            for (Cash cash : entry.getValue ()) {
                String tmpDate = simpleDateFormat.format (cash.cashDate);
                if (tmpMap.containsKey (tmpDate)) {
                    long tmpData = tmpMap.get (tmpDate) + cash.amount;
                    tmpMap.put (tmpDate,tmpData);
                } else {
                    tmpMap.put (tmpDate,cash.amount);
                }
            }

            x = 0;
            ArrayList<BarEntry> valuesA = new ArrayList<>();
            for (String ym : xValues) {
                if (tmpMap.containsKey (ym)) {
                    valuesA.add (new BarEntry (tmpMap.get (ym), x));
                } else {
                    valuesA.add (new BarEntry (0, x));
                }
                x++;
            }

            BarDataSet valuesADataSet = new BarDataSet (valuesA, application.getLoginUser ().getAccount (entry.getKey ()).accountName);
            valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[barDataSets.size ()]);
            barDataSets.add(valuesADataSet);

        }



        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setDrawHighlightArrow(true);


        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        barChart.setData(new BarData (xValues, barDataSets));

        barChart.invalidate();
        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }
}
