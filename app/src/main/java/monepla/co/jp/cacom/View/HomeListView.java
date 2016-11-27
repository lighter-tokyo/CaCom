package monepla.co.jp.cacom.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import monepla.co.jp.cacom.Controller.HomePageAdapter;
import monepla.co.jp.cacom.Interface.ActivityListener;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.CommonUtils;
import monepla.co.jp.cacom.Utils.CaComApplication;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * TODO: document your custom view class.
 */
public class HomeListView extends RelativeLayout {
    private Context context;
    private CaComApplication application;
    public ActivityListener activityListener;
    public static final String TAG = "HomeListView";

    @InjectView(R.id.home_view_paper)
    ViewPager viewPaper;
    @InjectView (R.id.strip)
    PagerTabStrip pagerTabTrip;
    @InjectView (R.id.plus_button)
    FloatingActionButton actionButton;

    public HomeListView(Context context) {
        super (context);
        init (context,null, 0);
    }

    public HomeListView(Context context, AttributeSet attrs) {
        super (context, attrs);
        init (context,attrs, 0);
    }

    public HomeListView(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        init (context,attrs, defStyle);
    }

    private void init(Context context,AttributeSet attrs, int defStyle) {
        application = (CaComApplication) context.getApplicationContext ();
        LogFnc.LogTraceStart (LogFnc.current (),application);
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        activityListener = (ActivityListener)context;
        View layout = layoutInflater.inflate(R.layout.fragment_home_list, this);
        ButterKnife.inject(this, layout);
        a.recycle ();
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    public void invalidateTextPaintAndMeasurements() {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        pagerTabTrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 );
        HomePageAdapter homePageAdapter = new HomePageAdapter (context);
        homePageAdapter.addAll (CommonUtils.getYearMonthList (application.getLoginUser ().createDate));
        viewPaper.setAdapter (homePageAdapter);
        viewPaper.setCurrentItem (homePageAdapter.getCount ());
        activityListener.setToolbarTitle (R.string.deposits_withdrawals);
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw (canvas);

    }


    @OnClick(R.id.plus_button)
    public void plusView() {
        activityListener.addStackFragment (PlusFragment.newInstance (-1,-1,null));
    }
}