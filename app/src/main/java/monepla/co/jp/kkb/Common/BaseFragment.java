package monepla.co.jp.kkb.Common;

import android.content.Context;
import android.support.v4.app.Fragment;

import monepla.co.jp.kkb.Interface.ActivityListener;
import monepla.co.jp.kkb.Utils.KkbApplication;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
abstract public class BaseFragment extends Fragment {


    public OnFragmentInteractionListener mListener;
    public KkbApplication application;
    public ActivityListener activityListener;
    public String TAG;
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM3 = "param3";
    public static final String ARG_PARAM4 = "param4";


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(BaseFragment param) {
        int backStackCnt = getFragmentManager ().getBackStackEntryCount();
        if (backStackCnt != 0) {
            try {
                getFragmentManager().popBackStack();
            } catch (RuntimeException e) {
                e.printStackTrace ();
            }

        }
        if (mListener != null) {
            mListener.onFragmentInteraction(param);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogFnc.LogTraceStart (LogFnc.current ());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof ActivityListener) {
            activityListener = (ActivityListener) context;
        }
        application = (KkbApplication)context.getApplicationContext();
        setHasOptionsMenu (false);

        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onButtonPressed (this);
        mListener = null;
        activityListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(BaseFragment param);
    }
}
