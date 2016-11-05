package monepla.co.jp.kkb.Controller;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/07/23.
 * 電卓view
 */
public class CalcView extends LinearLayout implements View.OnClickListener {
    /** 計算リスナー */
    private OnCalcListener onCalcListener;
    /** ビュー */
    private AppCompatEditText appCompatEditText;
    private AppCompatEditText appCompatEditTextIn;
    private AppCompatButton appCompatButton;
    /** 計算区分保持 */
    private String calcDiv = "";
    /** データ保持 */
    private long postData = 0L;

    /**
     * 計算リスナー
     *
     */
    public interface OnCalcListener {
        void onCalc(String calcText);
        void onButtonClick();
    }

    /**
     * インスタンス
     * @param context コンテキスト
     */
    public CalcView(Context context) {
        super (context);
    }

    /**
     * インスタンス
     * @param context コンテキスト
     * @param attributeSet ビュー情報
     */
    public CalcView(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    /**
     * リスナーセット
     * クリックイベントセット
     * @param listener リスナー
     */
    public void setOnCalcListener(OnCalcListener listener) {
        LogFnc.LogTraceStart (LogFnc.current ());
        onCalcListener = listener;
        findViewById (R.id.calc_0).setOnClickListener (this);
        findViewById (R.id.calc_1).setOnClickListener (this);
        findViewById (R.id.calc_2).setOnClickListener (this);
        findViewById (R.id.calc_3).setOnClickListener (this);
        findViewById (R.id.calc_4).setOnClickListener (this);
        findViewById (R.id.calc_5).setOnClickListener (this);
        findViewById (R.id.calc_6).setOnClickListener (this);
        findViewById (R.id.calc_7).setOnClickListener (this);
        findViewById (R.id.calc_8).setOnClickListener (this);
        findViewById (R.id.calc_9).setOnClickListener (this);
        findViewById (R.id.calc_plus).setOnClickListener (this);
        findViewById (R.id.calc_minus).setOnClickListener (this);
        findViewById (R.id.calc_kakeru).setOnClickListener (this);
        findViewById (R.id.calc_waru).setOnClickListener (this);
        findViewById (R.id.calc_clear).setOnClickListener (this);
        findViewById (R.id.calc_del).setOnClickListener (this);
        findViewById (R.id.calc_equal).setOnClickListener (this);
        appCompatButton = (AppCompatButton) findViewById (R.id.calc_button);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * ボタンセット
     * @param name ボタン名称
     */
    public void setAppCompatButton(String name) {
        LogFnc.LogTraceStart (LogFnc.current ());
        appCompatButton.setVisibility (VISIBLE);
        appCompatButton.setText (name);
        appCompatButton.setOnClickListener (this);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * 表示ビュー
     * @param appCompatEditText 合計値
     * @param appCompatEditTextIn 途中経過
     */
    public void setTextView(AppCompatEditText appCompatEditText,AppCompatEditText appCompatEditTextIn) {
        LogFnc.LogTraceStart (LogFnc.current ());
        this.appCompatEditText = appCompatEditText;
        this.appCompatEditTextIn = appCompatEditTextIn;
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current ());
        String text = appCompatEditText.getText ().toString ();
        long data = 0;
        if (!TextUtils.isEmpty (text)) {
            /** 計算値取得 */
            data = Long.parseLong (appCompatEditText.getText ().toString ());
        }

        switch (view.getId ()) {
            case R.id.calc_button:
                if (onCalcListener != null) {
                    /** ボタンクリック */
                    onCalcListener.onButtonClick ();
                }
                return;
            case R.id.calc_0:
                data = calc (0,data);
                break;
            case R.id.calc_1:
                data = calc (1,data);
                break;
            case R.id.calc_2:
                data = calc (2,data);
                break;
            case R.id.calc_3:
                data = calc (3,data);
                break;
            case R.id.calc_4:
                data = calc (4,data);
                break;
            case R.id.calc_5:
                data = calc (5,data);
                break;
            case R.id.calc_6:
                data = calc (6,data);
                break;
            case R.id.calc_7:
                data = calc (7,data);
                break;
            case R.id.calc_8:
                data = calc (8,data);
                break;
            case R.id.calc_9:
                data = calc (9,data);
                break;
            case R.id.calc_plus:
                data = setCalcLoad (data,"+");
                break;
            case R.id.calc_minus:
                data = setCalcLoad (data,"-");
                break;
            case R.id.calc_waru:
                data = setCalcLoad (data,"/");
                break;
            case R.id.calc_kakeru:
                data = setCalcLoad (data,"*");
                break;
            case R.id.calc_equal:
                data = calcEqual (data);
                calcDiv = "";
                break;
            case R.id.calc_clear:
                data = 0;
                appCompatEditTextIn.setText ("");
                break;
            case R.id.calc_del:
                data /= 10;
                break;
        }
        if (data > 99999999999999L) {
            /** 長さチェック */
            return;
        } else if (data < 0) {
            /** マイナス値はなし */
            data = 0;
        }
        appCompatEditText.setText (String.valueOf (data));
        if (onCalcListener != null) {
            onCalcListener.onCalc ("");
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    /**
     * 入力
     * @param num 数値
     * @param data データ
     * @return データ
     */
    private long calc(long num,long data) {
        LogFnc.LogTraceStart (LogFnc.current ());
        LogFnc.LogTraceEnd (LogFnc.current ());
        return data * 10 + num;
    }

    /**
     * 計算処理
     * @param data データ
     * @param calc 計算区分
     * @return クリア
     */
    private long setCalcLoad(long data, String calc) {
        LogFnc.LogTraceStart (LogFnc.current ());
        if (!TextUtils.isEmpty (calc)) {
            data = calcEqual (data);
        }
        calcDiv = calc;
        postData = data;
        appCompatEditTextIn.setText (String.valueOf (data) + " " + calc + "");
        LogFnc.LogTraceEnd (LogFnc.current ());
        return 0;
    }

    /**
     * イコール
     * @param data データ
     * @return 合計
     */
    private long calcEqual(long data) {
        LogFnc.LogTraceStart (LogFnc.current ());
        switch (calcDiv) {
            case "+":
                data += postData;
                break;
            case "-":
                data = postData - data;
                break;
            case "*":
                data = postData * data;
                break;
            case "/":
                data = postData / data;
                break;
        }

        appCompatEditTextIn.setText ("");
        LogFnc.LogTraceEnd (LogFnc.current ());
        return data;
    }
}
