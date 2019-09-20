/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.ui.signup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appdev.fragmentnavigation.FragNavController;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rilixtech.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.appdev.invited.R;
import co.appdev.invited.ui.base.BaseActivity;
import co.appdev.invited.ui.codeVerfication.CodeVerificationActivity;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.signin.SignInActivity;
import co.appdev.invited.util.DialogFactory;
import co.appdev.invited.util.ViewUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class SignupActivity extends BaseActivity implements Validator.ValidationListener, SignupMvpView {

    @Order(7)
    //@NotEmpty(message = "Gender is required")
    @BindView(R.id.genderSpinner)
    Spinner genderSpinner;

    @Order(6)
    @NotEmpty(message = "First Name is required")
    @BindView(R.id.firstName)
    TextView firstName;

    @Order(5)
    @NotEmpty(message = "Last Name is required")
    @BindView(R.id.lastName)
    TextView lastName;

    @Order(4)
    @NotEmpty(message = "Phone is required")
    @BindView(R.id.phone)
    TextView phone;

    //    @Order(3)
//    @Email(message = "Valid email is required")
    @BindView(R.id.email)
    TextView email;

    @Order(2)
    @NotEmpty(message = "Password is required")
    @BindView(R.id.password)
    TextView password;

    @Order(1)
    @NotEmpty(message = "Confirm password is required")
    @BindView(R.id.c_password)
    TextView confirmPassword;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;


    @BindView(R.id.sign_up_press)
    Button signUp;
    @BindView(R.id.sign_in)
    Button signIn;

    @Order(3)
//    @NotEmpty(message = "Date of Birth is required")
    @BindView(R.id.dateOfBirth)
    EditText et_dob;

    @BindView(R.id.anniversary)
    EditText et_anniversary;

    private Validator validator;


    private ProgressDialog myDialog;

    @Inject
    SignupPresenter signupPresenter;

    private BaseActivity baseActivity;

    private Calendar myCalendar = Calendar.getInstance();

    private String gender;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    public void initViews(Bundle savedInstanceState) {
        baseActivity = (BaseActivity) this;
        baseActivity.activityComponent().inject(this);
        ButterKnife.bind(this);
        signupPresenter.attachView(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        myDialog = new ProgressDialog(this);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (phone.getText().toString().matches(ConstantUtils.PHONE_RE) || phone.getText().toString().matches(ConstantUtils.INTER_PHONE_RE)) {
                ViewUtil.hideKeyboard(baseActivity);
                validator.validate();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Valid phone is requried", Toast.LENGTH_LONG).show();
//                }

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderSpinner.getSelectedItem().toString();
                if (gender.equals("Male")){
                    gender = "1";
                }else if(gender.equals("Female")) {
                    gender = "2";
                }else {
                    gender = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Gender is requried", Toast.LENGTH_LONG).show();
            }
        });

        et_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N){
                        new DatePickerDialog(SignupActivity.this,birthday,
                                myCalendar.get(Calendar.YEAR) - 29, myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        new DatePickerDialog(SignupActivity.this, AlertDialog.THEME_HOLO_LIGHT, birthday,
                                myCalendar.get(Calendar.YEAR) - 29, myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
                return false;
            }
        });

        et_anniversary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ViewUtil.hideKeyboard(baseActivity);
                    if(android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N){
                        new DatePickerDialog(SignupActivity.this,birthday,
                                myCalendar.get(Calendar.YEAR) - 29, myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        new DatePickerDialog(SignupActivity.this, AlertDialog.THEME_HOLO_LIGHT, anniversary, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
                return false;
            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }


    @Override
    public void onValidationSucceeded() {
        if (password.getText().toString().length() >= 6) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
//                if(gender.equals("0")){
//                    Toast.makeText(this, "Gender is required", Toast.LENGTH_LONG).show();
//                } else {
                if ( email.getText().toString().length() > 0) {
                    if (email.getText().toString().matches(emailPattern)) {
                        initSignupRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    initSignupRequest();
                }
//                }
            } else {
                Toast.makeText(this, "Password should be same", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Password length is small", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ValidationError error = errors.get(errors.size() - 1);
        String message = error.getCollatedErrorMessage(this);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSignupSuccess(String msg) {
        myDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();


    }

    @Override
    public void onSignupFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        myDialog.dismiss();

    }

    @Override
    public void loadCodeScreen() {
        String dobSqlFormate = ViewUtil.onlyFormatDateSql(String.valueOf(et_dob.getText()));
        myDialog.cancel();
        Intent intent = new Intent(getApplicationContext(), CodeVerificationActivity.class);
        intent.putExtra("firstName", firstName.getText().toString());
        intent.putExtra("lastName", lastName.getText().toString());
        intent.putExtra("phone", countryCodePicker.getFullNumber()+""+phone.getText().toString());
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("password", password.getText().toString());
        intent.putExtra("gender", password.getText().toString());
        intent.putExtra("dob",dobSqlFormate);
        intent.putExtra("dateofrelation", et_anniversary.getText().toString());
        intent.putExtra("gender", gender);
        intent.putExtra("type", "signup");
        startActivity(intent);
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

    // function to init signup request API call
    private void initSignupRequest() {
        if (DialogFactory.checkInternetConnection(baseActivity)) {
            myDialog = DialogFactory.showProgressDialog(this, "Signup", "Please Wait...");
            signupPresenter.userSignupValidation("fndemo", "lndemo", countryCodePicker.getFullNumber()+""+phone.getText().toString(), email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
        }
    }

    DatePickerDialog.OnDateSetListener birthday = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            ViewUtil.hideKeyboard(baseActivity);
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            et_dob.setText(sdf.format(myCalendar.getTime()));
        }

    };

    DatePickerDialog.OnDateSetListener anniversary = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            ViewUtil.hideKeyboard(baseActivity);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            et_anniversary.setText(sdf.format(myCalendar.getTime()));
        }

    };

}
