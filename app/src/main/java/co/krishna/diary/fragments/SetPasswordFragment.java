package co.krishna.diary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import java.util.Objects;

import co.krishna.diary.R;
import co.krishna.diary.activities.MainActivity;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;

public class SetPasswordFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextInputLayout tilNewPassword, tilConfirmPassword;
    private Button btnProceed;
    private TextView tvBtnAlrdyHaveCode;
    private EditText etNewPassword, etConfirmPassword;

    public SetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(R.layout.fragment_set_password, container, false);

        tilNewPassword = vv.findViewById(R.id.tilNewPassword);
        tilConfirmPassword = vv.findViewById(R.id.tilConfirmPassword);
        etNewPassword = vv.findViewById(R.id.etNewPassword);
        etConfirmPassword = vv.findViewById(R.id.etConfirmPassword);
        tvBtnAlrdyHaveCode = vv.findViewById(R.id.tvBtnAlrdyHaveCode);
        btnProceed = vv.findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);
        tvBtnAlrdyHaveCode.setOnClickListener(this);
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
        tilNewPassword.setError("New Password");
        tilConfirmPassword.setError("Confirm Password");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProceed:
                validateInputs();
                break;
            case R.id.tvBtnAlrdyHaveCode:
                startFragment();
                break;
        }
    }

    private void initKeyBoardActionDone() {
        //onKeyboard Listener for actionDone
        etConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validateInputs();
                }
                return false;
            }
        });
    }

    private void validateInputs() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (Constantz.isEmpty(newPassword) && Constantz.isEmpty(confirmPassword)) {
            tilNewPassword.setErrorTextAppearance(R.style.errorRed);
            tilConfirmPassword.setErrorTextAppearance(R.style.errorRed);
            tilNewPassword.setError("Enter New Password");
            tilConfirmPassword.setError("Enter Confirm Password");
        } else if (Constantz.isEmpty(newPassword)) {
            tilNewPassword.setErrorTextAppearance(R.style.errorRed);
            tilNewPassword.setError("Enter New Password");
        } else if (Constantz.isEmpty(confirmPassword)) {
            tilConfirmPassword.setErrorTextAppearance(R.style.errorRed);
            tilConfirmPassword.setError("Enter Confirm Password");
        } else if (!Objects.equals(newPassword, confirmPassword)) {
            tilConfirmPassword.setErrorTextAppearance(R.style.errorRed);
            tilNewPassword.setErrorTextAppearance(R.style.errorRed);
            Toast.makeText(mContext, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            proceedToActivity();
        }
    }

    private void proceedToActivity() {
        PrefManager.setPrefValue(mContext, Constantz.PASSWORD, etNewPassword.getText().toString().trim());
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    private void startFragment() {
        Fragment ff = new EnterPasswordFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ft.replace(R.id.frame, ff);
        ft.addToBackStack(null);
        ft.commit();
    }
}
