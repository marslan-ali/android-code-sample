package co.appdev.invited.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import co.appdev.invited.InvitedApplication;
import co.appdev.invited.R;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.signin.SignInActivity;
import co.appdev.invited.ui.signup.SignupActivity;
import co.appdev.invited.util.ConstantUtils;
import co.appdev.invited.util.RxUtil;


public class SplashActivity extends AppCompatActivity {
    // SplashActivity screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                PreferencesHelper preferencesHelper = InvitedApplication.get(getApplicationContext()).getComponent().preferencesHelper();

                if (!preferencesHelper.getValue(ConstantUtils.USERID).equals("")) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
