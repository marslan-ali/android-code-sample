package co.appdev.invited.ui.userProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.appdev.invited.InvitedApplication;
import co.appdev.invited.R;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.data.model.User;
import co.appdev.invited.ui.base.BaseFragment;
import co.appdev.invited.ui.createList.AddGroupFragment;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.signin.SignInActivity;
import co.appdev.invited.ui.updateUserProfile.UpdateUserProfileFragment;
import co.appdev.invited.util.ConstantUtils;
import co.appdev.invited.util.DialogFactory;
import co.appdev.invited.util.ViewUtil;

import static co.appdev.invited.util.ConstantUtils.HOST_NAME;

public class UserProfileFragment extends BaseFragment implements UserProfileMvpView {

    public UserProfileFragment(){

    }
    @BindView(R.id.profile_image)
    ImageView im_PI;

    @BindView(R.id.go_profile_update)
    TextView im_PU;

    @BindView(R.id.txFirstName)
    TextView tx_FN;

    @BindView(R.id.txLastName)
    TextView tx_LN;

    @BindView(R.id.txEmail)
    TextView tx_Email;

    @BindView(R.id.txGender)
    TextView tx_Gender;

    @BindView(R.id.txDateOfBirth)
    TextView tx_DOB;

    @BindView(R.id.txDateofRelation)
    TextView tx_DOR;

    @BindView(R.id.hintDateOfBirth)
    TextView tx_hintDateOfBirth;

    @Inject
    UserProfilePresenter userProfilePresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private static int SPLASH_TIME_OUT = 2000;
    private UserProfileFragment.OnFragmentInteractionListener mListener;
    private ProgressDialog myDialog;

    public static UserProfileFragment getInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void initViews(View parentView) {
        baseActivity.activityComponent().inject(this);
        userProfilePresenter.attachView(this);
        ButterKnife.bind(getActivity());

        // Hide add group text
        ((HomeActivity) getActivity()).HideAddGroup();

        //get-user
        if (DialogFactory.checkInternetConnection(baseActivity)) {
            myDialog = DialogFactory.showProgressDialog(getActivity(), "", "Please Wait...");
            myDialog.setCancelable(false);
            userProfilePresenter.getRequestForUserProfile();
        }

        //Go to update user screen
        im_PU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,new UpdateUserProfileFragment(),null).addToBackStack(null).commit();
            }
        });
        //Full Image
        im_PI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesHelper preferencesHelper = InvitedApplication.get(getContext().getApplicationContext()).getComponent().preferencesHelper();
                String prefUserObject = preferencesHelper.getValue(ConstantUtils.USEROBJECT);
                User user = userProfilePresenter.parseGsonData(prefUserObject);
                if(!user.getProfileImage().equals("")) {
                    ((HomeActivity) getContext()).pushFragment(ProfileFullImageFragment.newInstance(user), false);
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public void onRequestSuccess() {

        PreferencesHelper preferencesHelper = InvitedApplication.get(getContext().getApplicationContext()).getComponent().preferencesHelper();
        String prefUserObject = preferencesHelper.getValue(ConstantUtils.USEROBJECT);

        User user = userProfilePresenter.parseGsonData(prefUserObject);

        String defaultURL = "http://"+HOST_NAME+"/images/";
        if(!user.getProfileImage().equals("")) {
            Uri profilePhotoUri = Uri.parse(user.getProfileImage());
            Glide.with(getContext()).load(profilePhotoUri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(im_PI);
        }else{
            im_PI.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_red_person));
        }
        tx_FN.setText(user.getFirstName());
        tx_LN.setText(user.getLastName());
        if(!(user.getGenderId() == null) && !user.getGenderId().equals("") && !user.getGenderId().equals(0)) {
            if (user.getGenderId().equals(1)) {
                tx_Gender.setText("Male");
            } else {
                tx_Gender.setText("Female");
            }
        } else {
            tx_Gender.setText("");
        }
        tx_Email.setText(user.getEmail());

        if (!(user.getUpdatedAt() == null) || !user.getUpdatedAt().equals("")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Timestamp updatedAt = Timestamp.valueOf(user.getUpdatedAt());

            long diff = timestamp.getTime() / (24 * 60 * 60 * 1000) - updatedAt.getTime() / (24 * 60 * 60 * 1000);
            long leftDays = TimeUnit.DAYS.convert(diff, TimeUnit.DAYS);
            if (user.getDob() != null && !user.getDob().equals("")) {
                tx_DOB.setText(ViewUtil.onlyFormatDate(user.getDob()));
            }
        } else {
            tx_DOB.setText(ViewUtil.onlyFormatDate(user.getDob()));
        }
        if(user.getDob() != null && !user.getDob().equals("")) {
            tx_DOR.setText(ViewUtil.onlyFormatDate(user.getDor()));
        }

        myDialog.dismiss();

    }

    @Override
    public void onRequestFailure() {
        myDialog.dismiss();
        preferencesHelper.clearAll();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                PreferencesHelper preferencesHelper = InvitedApplication.get(getContext().getApplicationContext()).getComponent().preferencesHelper();
                if (preferencesHelper.getValue(ConstantUtils.USERID).equals("")
                        || preferencesHelper.getValue("access_token").equals("")) {
                    Log.d("SPLASH CHECK","HOME");
                    startActivity(new Intent(getContext().getApplicationContext(), SignInActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        }, SPLASH_TIME_OUT);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        myDialog.dismiss();
    }
}
