package monepla.co.jp.kkb.Utils;

import android.os.Build;
import android.util.Log;

import java.util.Date;
import java.util.regex.Pattern;


/**
 * Created by user on 2016/07/12.
 */
public class LogFnc {
    public static final String INFO = "i";
    public static final String WARNING = "w";
    public static final String DEBUG = "d";
    public static final String ERROR = "e";
    private static final String TAB = ",";
    public static int user_id;


    public static void Logging(String status,String msg,StackTraceElement element) {
        String txt = TAB + status +
                TAB + element.getClassName() +
                TAB + element.getMethodName() +
                TAB + element.getLineNumber() +
                TAB + user_id +
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
    public static void LogTraceStart(StackTraceElement element){

        Log.i(element.getClassName() ,
                        TAB + INFO +
                        TAB + element.getClassName() +
                        TAB + element.getMethodName() +
                        TAB + element.getLineNumber() +
                        TAB + user_id +
                        TAB + Build.DEVICE +
                        TAB + Build.VERSION.SDK_INT +
                        TAB + System.currentTimeMillis() +
                        TAB + "start");
    }
    public static void LogTraceEnd(StackTraceElement element){
        Log.i(element.getClassName(),
                        TAB + INFO +
                        TAB + element.getClassName() +
                        TAB + element.getMethodName() +
                        TAB + element.getLineNumber() +
                        TAB + user_id +
                        TAB + Build.DEVICE +
                        TAB + Build.VERSION.SDK_INT +
                        TAB + System.currentTimeMillis() +
                        TAB + "end");
    }

    public static StackTraceElement current() {
        return Thread.currentThread().getStackTrace()[3];
    }
}
