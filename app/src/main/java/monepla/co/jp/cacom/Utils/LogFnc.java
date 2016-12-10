package monepla.co.jp.cacom.Utils;

import android.os.Build;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import monepla.co.jp.cacom.Model.User;


/**
 * Created by user on 2016/07/12.
 */
public class LogFnc {
    public static final String INFO = "i";
    public static final String WARNING = "w";
    public static final String DEBUG = "d";
    public static final String ERROR = "e";
    private static final String TAB = ",";


    public static void Logging(String status,String msg,StackTraceElement element) {
        String txt = TAB + status +
                TAB + element.getClassName() +
                TAB + element.getMethodName() +
                TAB + element.getLineNumber() +
                TAB + Build.DEVICE +
                TAB + Build.VERSION.SDK_INT +
                TAB + System.currentTimeMillis() +
                TAB + msg;
        switch (status) {
            case INFO:
                Log.i(element.getClassName(),txt);
                break;
            case WARNING:
                Log.w(element.getClassName(),txt);
                break;
            case DEBUG:
                Log.d(element.getClassName(),txt);
                break;
            case ERROR:
                Log.e(element.getClassName(),txt);
                break;
            default:
                break;
        }

    }
    public static void LogTraceStart(StackTraceElement element, CaComApplication application){
        if (application != null) Tracking(application.getDefaultTracker(),element,application.getLoginUser(),"start");
        Log.i(element.getClassName() ,
                        TAB + INFO +
                        TAB + element.getClassName() +
                        TAB + element.getMethodName() +
                        TAB + element.getLineNumber() +
                        TAB + Build.DEVICE +
                        TAB + Build.VERSION.SDK_INT +
                        TAB + System.currentTimeMillis() +
                        TAB + "start");
    }
    public static void LogTraceEnd(StackTraceElement element,CaComApplication application){
        if (application != null) Tracking(application.getDefaultTracker(),element,application.getLoginUser(),"end");
        Log.i(element.getClassName(),
                        TAB + INFO +
                        TAB + element.getClassName() +
                        TAB + element.getMethodName() +
                        TAB + element.getLineNumber() +
                        TAB + Build.DEVICE +
                        TAB + Build.VERSION.SDK_INT +
                        TAB + System.currentTimeMillis() +
                        TAB + "end");
    }

    public static StackTraceElement current() {
        return Thread.currentThread().getStackTrace()[3];
    }

    private static void Tracking(Tracker tracker, StackTraceElement element, User user,String tag) {
        tracker.setScreenName(element.getClassName());
        tracker.setTitle(element.getMethodName());
        if (user != null) tracker.set("user",user.objectId);
        tracker.set("content",tag);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
