package co.krishna.diary.activities;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import co.krishna.diary.R;
import co.krishna.diary.fragments.EnterPasswordFragment;
import co.krishna.diary.fragments.SetPasswordFragment;
import co.krishna.diary.utils.Constantz;
import co.krishna.diary.utils.PrefManager;

public class PasswordActivity extends AppCompatActivity {
    Fragment fragment;

    Boolean backButtonPressedOnce = false;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        relativeLayout = findViewById(R.id.passwordActivity);
        if (TextUtils.isEmpty(PrefManager.getPrefValue(this, Constantz.PASSWORD))) {
            fragment = new SetPasswordFragment();
        } else {
            fragment = new EnterPasswordFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ft.replace(R.id.frame, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!backButtonPressedOnce) {
            this.backButtonPressedOnce = true;
            Snackbar.make(relativeLayout, "Press again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backButtonPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }
}
