package monepla.co.jp.kkb.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import monepla.co.jp.kkb.Common.AppModel;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.Model.Category;

/**
 * 共通メソッドクラス
 * Created on 2016/07/14.
 */
public class CommonUtils {
    /**
     * フィルターを作成
     * 半角のみ
     */
    public static InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            if (source.toString().matches("^[0-9a-zA-Z@¥.¥_¥¥-]+$")) {
                return source;
            } else {
                return "";
            }
        }
    };

    /**
     * カテゴリー取得
     * @param type 区分
     * @return カテゴリーリスト
     */
    public static List<Category> getCategoryList(int type) {
        return new Select ().from (Category.class).where (Category.COL_CASH_DIV + "= ?",type).execute ();
    }

    public static CommonConst.CategoryDiv getCategoryDiv(int id) {
        if (id == CommonConst.CategoryDiv.OUTPUT.getId ()) {
            return CommonConst.CategoryDiv.OUTPUT;
        }
        if (id == CommonConst.CategoryDiv.INPUT.getId ()) {
            return CommonConst.CategoryDiv.INPUT;
        }
        if (id == CommonConst.CategoryDiv.CHANGE.getId ()) {
            return CommonConst.CategoryDiv.CHANGE;
        }
        return null;
    }

    public static ArrayList<String> getYearMonthList(Date createDate) {
        LogFnc.LogTraceStart (LogFnc.current ());
        ArrayList<String> list = new ArrayList<> ();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMM");
        String created = simpleDateFormat.format (createDate);
        String now = simpleDateFormat.format (new Date ());

        for (int yy = Integer.parseInt (created.substring (0,4)); yy <= Integer.parseInt (now.substring (0,4));yy++) {
            int start;
            int end;
            if (yy == Integer.parseInt (created.substring (0,4))) {
                start = Integer.parseInt (created.substring (5));
            } else {
                start = 1;
            }
            if (yy == Integer.parseInt (now.substring (0,4))) {
                end = Integer.parseInt (now.substring (4));
            } else {
                end = 12;
            }
            LogFnc.Logging (LogFnc.ERROR,String.valueOf (start) ,LogFnc.current ());
            LogFnc.Logging (LogFnc.ERROR,String.valueOf (end) ,LogFnc.current ());
            for (int mm = start; mm <= end; mm++) {
                list.add(String.valueOf (yy) + "/" + String.format ("%02d",mm));

            }

        }
        LogFnc.LogTraceEnd (LogFnc.current ());
        return list;
    }

    public static String getUpdateShare (Context context,String name) {
        SharedPreferences preferences = context.getSharedPreferences (CommonConst.PREF_UPDATE,Context.MODE_PRIVATE);
        return preferences.getString (name,null);
    }

    public static void setUpdateShare (Context context,String name) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences (CommonConst.PREF_UPDATE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putString (name,new Date ().toString ());
        editor.apply ();
    }

    public static NCMBQuery<NCMBObject> getQuery (Context context,String tableName) {
        String updated = CommonUtils.getUpdateShare (context,tableName);
        NCMBQuery<NCMBObject> query = new NCMBQuery<> (tableName);
        if (!TextUtils.isEmpty (updated)) {
            query.whereGreaterThan (AppModel.COL_UPDATED_DATE,updated);
        }
        return query;
    }
}
