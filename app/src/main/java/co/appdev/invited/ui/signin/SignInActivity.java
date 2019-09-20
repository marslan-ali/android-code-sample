/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.ui.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appdev.fragmentnavigation.FragNavController;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rilixtech.CountryCodePicker;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.appdev.invited.R;
import co.appdev.invited.ui.base.BaseActivity;
import co.appdev.invited.ui.codeVerfication.CodeVerificationActivity;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.signup.SignupActivity;
import co.appdev.invited.util.DialogFactory;
import co.appdev.invited.util.ViewUtil;


public class SignInActivity extends BaseActivity implements Validator.ValidationListener, SigninMvpView {

    @BindView(R.id.sign_in_press)
    Button signIn;

    @BindView(R.id.sign_up)
    Button signUp;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    @Order(2)
    @NotEmpty(message = "Phone is empty")
    @BindView(R.id.phone)
    EditText phone;

    @Order(1)
    @NotEmpty(message = "Password is empty")
    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.forgot)
    TextView forgot;

    @Inject
    SigninPresenter signinPresenter;
    private BaseActivity baseActivity;
    private Validator validator;
    private ProgressDialog myDialog;

    @Override
    public void initViews(Bundle savedInstanceState) {
        baseActivity = (BaseActivity) this;
        baseActivity.activityComponent().inject(this);
        ButterKnife.bind(this);
        signinPresenter.attachView(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        myDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewUtil.hideKeyboard(baseActivity);
                validator.validate();

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() >= 6) {
                    Intent intent = new Intent(getApplicationContext(), CodeVerificationActivity.class);
                    intent.putExtra("phone", countryCodePicker.getFullNumber() + "" + phone.getText().toString());
                    intent.putExtra("type", "forgot");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter phone number to recover password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void pushFragment(Fragment fragment, boolean detach) {

    }

    @Override
    public void replaceFragment(Fragment fragment) {

    }

    @Override
    public void navigationTitle(Fragment fragment) {

    }

    @Override
    public void popCurrentFragment() {

    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void onSigninSuccess() {
        myDialog.dismiss();
        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onSigninFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        myDialog.dismiss();

    }

    @Override
    public void onValidationSucceeded() {
        initSignupRequest();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ValidationError error = errors.get(errors.size() - 1);
        String message = error.getCollatedErrorMessage(this);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    // function to init signup request API call
    private void initSignupRequest() {
        if (DialogFactory.checkInternetConnection(baseActivity)) {
            myDialog = DialogFactory.showProgressDialog(this, "SignIn", "Please Wait...");
            signinPresenter.userSignin(countryCodePicker.getFullNumber() + "" + phone.getText().toString(), password.getText().toString());
        }
    }

}
