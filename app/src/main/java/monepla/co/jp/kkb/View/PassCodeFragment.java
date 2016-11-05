package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.Constract.CommonConst;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/08/24.
 */
public class PassCodeFragment extends BaseFragment implements View.OnClickListener{

    private String pass_code = "";
    private View view;
    public static PassCodeFragment newInstance(boolean isSet) {
        PassCodeFragment fragment = new PassCodeFragment ();
        Bundle arg = new Bundle ();
        arg.putBoolean (ARG_PARAM1,isSet);
        fragment.setArguments (arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogFnc.LogTraceStart (LogFnc.current ());
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_pass_code, container, false);
        if (!getArguments ().getBoolean (ARG_PARAM1)) {
            activityListener.setToolbarTitle (R.string.other_pass_code);
        }
        setOnClickListener (view);
        this.view = view;
        pass_code = "";
        LogFnc.LogTraceEnd (LogFnc.current ());
        return view;
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current ());
        View passView;
        switch (view.getId ()) {
            case R.id.calc_0:
                pass_code += "0";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_1:
                pass_code += "1";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_2:
                pass_code += "2";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_3:
                pass_code += "3";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_4:
                pass_code += "4";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_5:
                pass_code += "5";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_6:
                pass_code += "6";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_7:
                pass_code += "7";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_8:
                pass_code += "8";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_9:
                pass_code += "9";
                passView = getPassTextView (pass_code.length ());
                if (passView != null) passView.setBackgroundColor (Color.WHITE);
            break;
            case R.id.calc_del:
                if (pass_code.length () == 0) break;
                pass_code = pass_code.substring (0,pass_code.length () - 1);
                passView = getPassTextView (pass_code.length () + 1);
                if (passView != null) passView.setBackgroundColor (getResources ().getColor (R.color.colorGray));
            break;
        }
        if (pass_code.length () == 4 && !getArguments ().getBoolean (ARG_PARAM1)) {
            SharedPreferences preferences = getContext ().getSharedPreferences (CommonConst.PREF_UPDATE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putString (CommonConst.PREF_PASS_CODE,pass_code);
            editor.apply ();
            onButtonPressed (this);
            return;
        } else if (pass_code.length () == 4) {
            SharedPreferences preferences = getContext ().getSharedPreferences (CommonConst.PREF_UPDATE, Context.MODE_PRIVATE);
            String code = preferences.getString (CommonConst.PREF_PASS_CODE,null);
            if (code != null && code.equals (pass_code)) {
                getFragmentManager ().beginTransaction ().remove (this).commit ();
                return;
            } else {
                pass_code = "";
                this.view.findViewById (R.id.pass1).setBackgroundColor (getResources ().getColor (R.color.colorGray));
                this.view.findViewById (R.id.pass2).setBackgroundColor (getResources ().getColor (R.color.colorGray));
                this.view.findViewById (R.id.pass3).setBackgroundColor (getResources ().getColor (R.color.colorGray));
                this.view.findViewById (R.id.pass4).setBackgroundColor (getResources ().getColor (R.color.colorGray));
            }
        }



        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    private void setOnClickListener (View view) {
        LogFnc.LogTraceStart (LogFnc.current ());
        view.findViewById (R.id.calc_0).setOnClickListener (this);
        view.findViewById (R.id.calc_1).setOnClickListener (this);
        view.findViewById (R.id.calc_2).setOnClickListener (this);
        view.findViewById (R.id.calc_3).setOnClickListener (this);
        view.findViewById (R.id.calc_4).setOnClickListener (this);
        view.findViewById (R.id.calc_5).setOnClickListener (this);
        view.findViewById (R.id.calc_6).setOnClickListener (this);
        view.findViewById (R.id.calc_7).setOnClickListener (this);
        view.findViewById (R.id.calc_8).setOnClickListener (this);
        view.findViewById (R.id.calc_9).setOnClickListener (this);
        view.findViewById (R.id.calc_del).setOnClickListener (this);
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    private View getPassTextView(int l) {
        switch (l - 1) {
            case 0:
                return view.findViewById (R.id.pass1);
            case 1:
                return view.findViewById (R.id.pass2);
            case 2:
                return view.findViewById (R.id.pass3);
            case 3:
                return view.findViewById (R.id.pass4);
        }
        return null;
    }
}
