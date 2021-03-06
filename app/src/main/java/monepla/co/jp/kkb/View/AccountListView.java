package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.res.TypedArray;
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
import monepla.co.jp.kkb.Controller.AccountPageAdapter;
import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.KkbApplication;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/09/30.
 */

public class AccountListView extends RelativeLayout {
    private Context context;
    private KkbApplication application;
    public ActivityListener activityListener;
    public static final String TAG = "AccountListView";

    @InjectView(R.id.home_view_paper)
    ViewPager viewPaper;
    @InjectView (R.id.strip)
    PagerTabStrip pagerTabTrip;
    @InjectView (R.id.plus_button)
    FloatingActionButton actionButton;

    public AccountListView (Context context) {
        super(context);
        init (context,null,0);
    }

    public AccountListView (Context context, AttributeSet attrs) {
        super (context,attrs);
        init (context,attrs,0);
    }

    public AccountListView(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        init (context,attrs, defStyle);
    }

    private void init(Context context,AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext ().obtainStyledAttributes (
                attrs, R.styleable.HomeListView, defStyle, 0);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        try {
            application = (KkbApplication) context.getApplicationContext ();
        } catch (ClassCastException e) {
            e.printStackTrace ();
        }
        activityListener = (ActivityListener)context;
        View layout = layoutInflater.inflate(R.layout.fragment_home_list, this);
        ButterKnife.inject(this, layout);
        a.recycle ();
    }

    public void invalidateTextPaintAndMeasurements() {
        LogFnc.LogTraceStart (LogFnc.current ());
        pagerTabTrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 );
        AccountPageAdapter accountPageAdapter = new AccountPageAdapter (context);
        viewPaper.setAdapter (accountPageAdapter);
        viewPaper.setCurrentItem (0);
        activityListener.setToolbarTitle (R.string.account);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @OnClick(R.id.plus_button)
    public void plusView() {
        activityListener.addStackFragment (AccountPlusFragment.newInstance (null));
    }
}
