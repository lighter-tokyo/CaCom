package monepla.co.jp.kkb.Common;

import android.widget.LinearLayout;


/**
 * Created by user on 2016/08/20.
 */
abstract public class BaseChartFragment extends BaseFragment{
    public boolean isLineCurve =  true;

    public  LinearLayout chartLayout;

    public int Range = 50;
    public int lineWidth ;
    public int barWidth;
    public float dpx;

    public int[] xData ;
    public int[] yData ;
    

    private void makeChart(){

        chartLayout.removeAllViews();
    }



}
