package monepla.co.jp.kkb.View;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import monepla.co.jp.kkb.Common.BaseFragment;
import monepla.co.jp.kkb.R;
import monepla.co.jp.kkb.Utils.LogFnc;

import static monepla.co.jp.kkb.Constract.CommonConst.OtherItems;

/**
 * Created by user on 2016/07/24.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,DoneCallback{
    private static SettingFragment fragment = new SettingFragment ();
    private static String paramKey = "param";
    private int settingDiv;
    private View contentView;
    @InjectView (R.id.setting_text_view_1)
    TextView textView1;
    @InjectView (R.id.setting_text_view_2)
    TextView textView2;
    @InjectView (R.id.setting_edit_text1)
    AppCompatEditText appCompatEditText1;
    @InjectView (R.id.setting_edit_text2)
    AppCompatEditText appCompatEditText2;
    @InjectView (R.id.setting_switch1)
    SwitchCompat switchCompat1;
    @InjectView (R.id.setting_button)
    AppCompatButton appCompatButton;
    public static SettingFragment newInstance(int param) {
        Bundle args = new Bundle();
        args.putInt (paramKey,param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments () != null) {
            settingDiv = getArguments ().getInt (paramKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogFnc.LogTraceStart (LogFnc.current ());
        // Inflate the layout for this fragment
        contentView = inflater.inflate (R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, contentView);
        setLayout(contentView);
        activityListener.showDrawer ();
        activityListener.setToolbarTitle (settingDiv);
        LogFnc.LogTraceEnd (LogFnc.current ());
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);

    }

    @Override
    public void onDetach() {
        super.onDetach ();
        activityListener.setToolbarTitle (R.string.other);
//        textView1.getEditableText ().clear ();
//        textView2.getEditableText ().clear ();
//        appCompatEditText1.getEditableText ().clear ();
//        appCompatEditText2.getEditableText ().clear ();
    }

    private void setLayout(View view) {
        LogFnc.LogTraceStart (LogFnc.current ());

//        if (settingDiv == OtherItems.ABOUT.getItems ()) {
//            textView1.setText ("kkbとは");
//            textView1.setVisibility (View.VISIBLE);
//        } else if (settingDiv == OtherItems.FAQ.getItems ()) {
//            view.findViewById (R.id.setting_text_input1).setVisibility (View.VISIBLE);
//            appCompatButton.setVisibility (View.VISIBLE);
//            appCompatButton.setText (R.string.send);
//            appCompatButton.setOnClickListener (this);

//        } else if (settingDiv == OtherItems.PASSWORD_CHANGE.getItems ()) {
//            view.findViewById (R.id.setting_text_input1).setVisibility (View.VISIBLE);
//            appCompatEditText1.setHint (R.string.change_pre_pass);
//            appCompatEditText1.setInputType (InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            view.findViewById (R.id.setting_text_input2).setVisibility (View.VISIBLE);
//            appCompatEditText2.setHint (R.string.change_new_pass);
//            appCompatEditText2.setInputType (InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            appCompatButton.setVisibility (View.VISIBLE);
//            appCompatButton.setText (R.string.update);
//            appCompatButton.setOnClickListener (this);
//        } else if (settingDiv == OtherItems.PASSWORD_FORGET.getItems ()) {
//            view.findViewById (R.id.setting_text_input1).setVisibility (View.VISIBLE);
//            appCompatEditText1.setHint (R.string.login_mail_hint);
//            appCompatEditText1.setInputType (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            if (!TextUtils.isEmpty (application.getLoginUser ().mailAddress)) {
//                appCompatEditText1.setText (application.getLoginUser ().mailAddress);
//            } else {
//                appCompatEditText1.setText ("aaa");
//            }
//            appCompatButton.setVisibility (View.VISIBLE);
//            appCompatButton.setText (R.string.send);
//            appCompatButton.setOnClickListener (this);
        if (settingDiv == OtherItems.PUSH.getItems ()) {
            switchCompat1.setVisibility (View.VISIBLE);
            switchCompat1.setChecked (application.getLoginUser ().push_flg);
            switchCompat1.setOnCheckedChangeListener (this);
//        } else if (settingDiv == OtherItems.TERM.getItems ()) {
//            //別画面
//        } else if (settingDiv == OtherItems.QUIT.getItems ()) {
//            view.findViewById (R.id.setting_text_input1).setVisibility (View.VISIBLE);
//            appCompatButton.setVisibility (View.VISIBLE);
//            appCompatButton.setText (R.string.other_quit);
//            appCompatButton.setOnClickListener (this);
//        } else if (settingDiv == OtherItems.MAIL_ADDRESS_CHANGE.getItems ()) {
//            view.findViewById (R.id.setting_text_input1).setVisibility (View.VISIBLE);
//            appCompatEditText1.setHint (R.string.login_mail_hint);
//            appCompatButton.setVisibility (View.VISIBLE);
//            appCompatButton.setText (R.string.update);
            appCompatButton.setOnClickListener (this);
        } else if (settingDiv == OtherItems.VERSION.getItems ()) {
            PackageManager pm = getContext ().getPackageManager();
            try{
                PackageInfo packageInfo = pm.getPackageInfo(getContext ().getPackageName(), 0);
                textView1.setText (String.valueOf (packageInfo.versionCode));
                textView1.setVisibility (View.VISIBLE);
                textView1.setGravity (Gravity.CENTER);
            }catch(PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
        }
        LogFnc.LogTraceEnd (LogFnc.current ());
    }

    @Override
    public void onClick(View view) {
//        if (settingDiv == OtherItems.FAQ.getItems ()) {
//            // Intentインスタンスを生成
//            Intent intent = new Intent();
//
//            // アクションを指定(ACTION_SENDTOではないところがミソ)
//            intent.setAction(Intent.ACTION_SENDTO);
//            // データを指定
//            intent.setData(Uri.parse("mailto:dev@mone-pla.co.jp?subject=問い合わせ&body=" + appCompatEditText1.getText ().toString ()));
//            startActivity(intent);
//        } else if (settingDiv == OtherItems.MAIL_ADDRESS_CHANGE.getItems ()) {
//            NCMBUser.getCurrentUser ().setMailAddress (appCompatEditText1.getText ().toString ());
//            NCMBUser.getCurrentUser ().saveInBackground (this);
//        } else if (settingDiv == OtherItems.PASSWORD_FORGET.getItems ()) {
//            NCMBUser.requestPasswordResetInBackground (application.getLoginUser ().mailAddress,this);
//        } else if (settingDiv == OtherItems.PASSWORD_CHANGE.getItems ()) {
//            NCMBUser.requestPasswordResetInBackground (application.getLoginUser ().mailAddress,this);
//        } else if (settingDiv == OtherItems.QUIT.getItems ()) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("title")
//                    .setMessage("message")
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            NCMBUser.getCurrentUser ().put (User.COL_DEL_FLG,true);
//                            NCMBUser.getCurrentUser ().saveInBackground (SettingFragment.this);
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Later button pressed
//                        }
//                    })
//                    .show();
//
//        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //プッシュ通知変更

    }

    @Override
    public void done(NCMBException e) {

    }
}
