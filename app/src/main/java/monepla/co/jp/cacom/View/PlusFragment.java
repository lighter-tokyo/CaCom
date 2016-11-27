package monepla.co.jp.cacom.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.cacom.Common.BaseFragment;
import monepla.co.jp.cacom.Constract.CommonConst;
import monepla.co.jp.cacom.Controller.CalcView;
import monepla.co.jp.cacom.R;
import monepla.co.jp.cacom.Utils.LogFnc;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlusFragment extends BaseFragment implements CalcView.OnCalcListener ,View.OnClickListener{
    private static PlusFragment fragment = new PlusFragment ();

    @InjectView (R.id.plus_calc_view)
    CalcView calcView;
    @InjectView (R.id.plus_calc_text_input)
    AppCompatEditText appCompatEditText;
    @InjectView (R.id.plus_in_text_input)
    AppCompatEditText appCompatEditTextIn;
    private int div = R.id.plus_output;
    private View contentView;


    public PlusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlusFragment newInstance(int div,long amount,String id) {
        Bundle args = new Bundle ();
        args.putInt (ARG_PARAM1,div);
        args.putLong (ARG_PARAM2,amount);
        args.putString (ARG_PARAM3,id);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LogFnc.LogTraceStart (LogFnc.current (),application);
        TAG = getClass ().getSimpleName ();
        contentView = inflater.inflate (R.layout.fragment_plus, container, false);
        ButterKnife.inject (this,contentView);

        contentView.findViewById (R.id.plus_output).setOnClickListener (this);
        contentView.findViewById (R.id.plus_input).setOnClickListener (this);
        contentView.findViewById (R.id.plus_move).setOnClickListener (this);

        int divPrm = getArguments ().getInt (ARG_PARAM1);


        if (divPrm == CommonConst.CategoryDiv.OUTPUT.ordinal ()) {
            LogFnc.Logging (LogFnc.INFO,CommonConst.CategoryDiv.OUTPUT.name (),LogFnc.current ());
            div = CommonConst.CategoryDiv.OUTPUT.getId ();
            contentView.findViewById (div).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorAccent));
            appCompatEditText.setText (String.valueOf (getArguments ().getLong (ARG_PARAM2)));
        } else if (divPrm == CommonConst.CategoryDiv.INPUT.ordinal ()) {
            LogFnc.Logging (LogFnc.INFO,CommonConst.CategoryDiv.INPUT.name (),LogFnc.current ());
            div = CommonConst.CategoryDiv.INPUT.getId ();
            contentView.findViewById (div).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorAccent));
            appCompatEditText.setText (String.valueOf (getArguments ().getLong (ARG_PARAM2)));
        } else if (divPrm == CommonConst.CategoryDiv.CHANGE.ordinal ()) {
            LogFnc.Logging (LogFnc.INFO,CommonConst.CategoryDiv.CHANGE.name (),LogFnc.current ());
            div = CommonConst.CategoryDiv.CHANGE.getId ();
            contentView.findViewById (div).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorAccent));
            appCompatEditText.setText (String.valueOf (getArguments ().getLong (ARG_PARAM2)));
        } else {
            appCompatEditText.setText ("");
            LogFnc.Logging (LogFnc.INFO,"null",LogFnc.current ());
        }

        calcView.setTextView (appCompatEditText,appCompatEditTextIn);
        calcView.setOnCalcListener (this);
        calcView.setAppCompatButton ("次へ");
        activityListener.closeDrawer();

        LogFnc.LogTraceEnd (LogFnc.current (),application);
        return contentView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach (context);

    }

    @Override
    public void onDetach() {
        super.onDetach ();

    }


    @Override
    public void onCalc(String calcText) {

    }

    @Override
    public void onButtonClick() {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        if (!TextUtils.isEmpty (appCompatEditText.getText ().toString ())) {
            getFragmentManager ().beginTransaction ().replace (R.id.fragment_view,
                    CategoryFragment.newInstance (div,Long.parseLong (appCompatEditText.getText ().toString ()),getArguments ().getString (ARG_PARAM3)),CategoryFragment.class.getSimpleName ())
                    .addToBackStack (fragment.getClass ().getSimpleName ()).commit ();
            appCompatEditText.getEditableText ().clear ();
        } else {
            appCompatEditText.setError ("値が設定されていません");
        }
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }

    @Override
    public void onClick(View view) {
        LogFnc.LogTraceStart (LogFnc.current (),application);
        contentView.findViewById (R.id.plus_input).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorWhite));
        contentView.findViewById (R.id.plus_output).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorWhite));
        contentView.findViewById (R.id.plus_move).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorWhite));
        contentView.findViewById (view.getId ()).setBackgroundColor (ContextCompat.getColor (getContext (),R.color.colorAccent));
        div = view.getId ();
        LogFnc.LogTraceEnd (LogFnc.current (),application);
    }
}
