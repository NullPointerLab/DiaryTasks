package co.krishna.diary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.krishna.diary.R;
import co.krishna.diary.activities.MainActivity;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;


public class EnterPasswordFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private EditText etEnterPassword;
    private Button btnProceed2;
    private TextView tvBtnNewHereSetup;
    private RelativeLayout relativeLayout;
    private TextView tvHelloThereSub;

    public EnterPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(R.layout.fragment_enter_password, container, false);
        etEnterPassword = vv.findViewById(R.id.etEnterPassword);
        btnProceed2 = vv.findViewById(R.id.btnProceed2);
        tvBtnNewHereSetup = vv.findViewById(R.id.tvBtnNewHereSetup);
        relativeLayout = vv.findViewById(R.id.enterPasswordLayout);
        tvHelloThereSub = vv.findViewById(R.id.tvHelloThereSub);

        btnProceed2.setOnClickListener(this);
        tvBtnNewHereSetup.setOnClickListener(this);

        initKeyBoardActionDone();
        return vv;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        String mPassword = PrefManager.getPrefValue(getActivity(), Constantz.PASSWORD);
        if (!Constantz.isEmpty(mPassword)) {
            int length = mPassword.length();
            tvHelloThereSub.setText("Enter your " + String.valueOf(length) + " digit password to access your Diary");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProceed2:
                String savedPassword = PrefManager.getPrefValue(mContext, Constantz.PASSWORD);
                String strPassword = etEnterPassword.getText().toString().trim();
                if (Constantz.isEmpty(strPassword)) {
                    etEnterPassword.setError("Please enter password");
                } else if (!strPassword.equals(savedPassword)) {
                    etEnterPassword.setError("Wrong Password");
                } else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.tvBtnNewHereSetup:
                startFragment();
                break;
        }
    }

    private void initKeyBoardActionDone() {
        //onKeyboard Listener for actionDone
        etEnterPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String savedPassword = PrefManager.getPrefValue(mContext, Constantz.PASSWORD);
                    String strPassword = etEnterPassword.getText().toString().trim();
                    if (Constantz.isEmpty(strPassword)) {
                        etEnterPassword.setError("Please enter password");
                    } else if (!strPassword.equals(savedPassword)) {
                        etEnterPassword.setError("Wrong Password");
                    } else {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
                return false;
            }
        });
    }


    private void startFragment() {
        Fragment ff = new SetPasswordFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ft.replace(R.id.frame, ff);
        ft.addToBackStack(null);
        ft.commit();
    }


}
