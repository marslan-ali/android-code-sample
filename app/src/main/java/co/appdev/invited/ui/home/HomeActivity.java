package co.appdev.invited.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdev.fragmentnavigation.FragNavController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.appdev.invited.InvitedApplication;
import co.appdev.invited.R;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.events.ContactListEvent;
import co.appdev.invited.events.HomeScrollEvent;
import co.appdev.invited.ui.base.BaseActivity;
import co.appdev.invited.ui.createList.AddGroupFragment;
import co.appdev.invited.ui.homeFragment.HomeFragment;
import co.appdev.invited.ui.notification.NotificationFragment;
import co.appdev.invited.ui.notification.NotificationPresenter;
import co.appdev.invited.ui.signin.SignInActivity;
import co.appdev.invited.ui.updateEvent.UpdateEventFragment;
import co.appdev.invited.ui.updateListFromContacts.UpdateGroupFragment;
import co.appdev.invited.ui.updateListSelected.UpdateSelectedGroupFragment;
import co.appdev.invited.ui.userProfile.UserProfileFragment;
import co.appdev.invited.util.ConstantUtils;
import co.appdev.invited.util.DialogFactory;
import co.appdev.invited.util.LocationUpdate;
import co.appdev.invited.util.MainBus;
import co.appdev.invited.util.ViewUtil;

import static co.appdev.invited.InvitedApplication.getContext;


public class HomeActivity extends BaseActivity implements HomeMvpView, GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener,
        UserProfileFragment.OnFragmentInteractionListener {

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.add_group)
    TextView addGroup;

    @BindView(R.id.add_group_icon)
    ImageView addGroupIcon;

    @BindView(R.id.nav_menu)
    ImageView nav_menu;

    @BindView(R.id.noti_count)
    TextView nav_noti_count;

    @Inject
    HomePresenter homePresenter;

    @Inject
    NotificationPresenter notificationPresenter;

    private static int SPLASH_TIME_OUT = 2000;

    private BaseActivity baseActivity;
    private ProgressDialog myDialog;
    public PreferencesHelper preferencesHelper;
    private GoogleApiClient mGoogleApiClient;

    private String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

//    private static final int JOBID = 110;
//    private JobScheduler myScheduler;
//    private JobInfo myjobInfo;

    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude, longitude;
    Geocoder geocoder;
    TextView notiCount;

    @Override
    public void initViews(Bundle savedInstanceState) {
        baseActivity = (BaseActivity) this;
        baseActivity.activityComponent().inject(this);
        ButterKnife.bind(this);
        homePresenter.attachView(this);
        myDialog = new ProgressDialog(this);
        preferencesHelper = InvitedApplication.get(this).getComponent().preferencesHelper();
        geocoder = new Geocoder(this, Locale.getDefault());
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        medit = mPref.edit();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        navigationView.setNavigationItemSelectedListener(this);
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragment(new HomeFragment())
                .build();

        hideAddGroupIcon();

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNavController.getCurrentFrag() instanceof UpdateSelectedGroupFragment) {
                    UpdateSelectedGroupFragment updateSelectedGroupFragment = (UpdateSelectedGroupFragment) mNavController.getCurrentFrag();
                    pushFragment(UpdateGroupFragment.newInstance(updateSelectedGroupFragment.getGid(), updateSelectedGroupFragment.getListName(), updateSelectedGroupFragment.getContacts()), false);
                } else if (!(mNavController.getCurrentFrag() instanceof AddGroupFragment)) {
                    pushFragment(AddGroupFragment.getInstance(), false);
                }


            }
        });

        addGroupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNavController.getCurrentFrag() instanceof UpdateSelectedGroupFragment) {
                    UpdateSelectedGroupFragment updateSelectedGroupFragment = (UpdateSelectedGroupFragment) mNavController.getCurrentFrag();
                    pushFragment(UpdateGroupFragment.newInstance(updateSelectedGroupFragment.getGid(), updateSelectedGroupFragment.getListName(), updateSelectedGroupFragment.getContacts()), false);
                } else if (!(mNavController.getCurrentFrag() instanceof AddGroupFragment)) {
                    pushFragment(AddGroupFragment.getInstance(), false);
                }


            }
        });

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        ViewUtil.setToken(preferencesHelper.getValue(ConstantUtils.TOKEN_TYPE) + " " + preferencesHelper.getValue(ConstantUtils.ACCESS_TOKEN));

        onNewIntent(getIntent());


        if (checkPermissions()) {
            homePresenter.startLoading();
            Intent intent = new Intent(getApplicationContext(), LocationUpdate.class);
            startService(intent);
        }

        // Update User location on login + signUp + App launch
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
//            Toast.makeText(getApplicationContext(), location+ "Sarmad", Toast.LENGTH_LONG).show();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size()>=1) {
                    String onlyCityName = addresses.get(0).getLocality();
//                Toast.makeText(getApplicationContext(), onlyCityName, Toast.LENGTH_LONG).show();
                    if ((preferencesHelper.getValue(ConstantUtils.USERID)) != "") {
                        Integer userID = Integer.valueOf(preferencesHelper.getValue(ConstantUtils.USERID));
                        if (DialogFactory.checkInternetConnection(baseActivity)) {
                            homePresenter.updateLocation(userID, onlyCityName);
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //get user notifications
        if ((preferencesHelper.getValue(ConstantUtils.USERID)) != "") {
            Integer userID = Integer.valueOf(preferencesHelper.getValue(ConstantUtils.USERID));
            homePresenter.getNotifications(String.valueOf(userID));
        }
        //set noti count on nav menu
        if(!preferencesHelper.getValue(ConstantUtils.NOTI_COUNT).equals("")) {
            nav_noti_count.setVisibility(View.VISIBLE);
            if (preferencesHelper.getValue(ConstantUtils.NOTI_COUNT).equals(0)){
                nav_noti_count.setText("");
            } else {
                nav_noti_count.setText(preferencesHelper.getValue(ConstantUtils.NOTI_COUNT));
            }
        }

        //set inner noti count
        notiCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notification));

        initializeCountDrawer();
    }

    private void initializeCountDrawer() {
        if(!preferencesHelper.getValue(ConstantUtils.NOTI_COUNT).equals("")) {
            notiCount.setGravity(Gravity.CENTER_VERTICAL);
            notiCount.setTextColor(Color.WHITE);
            if (preferencesHelper.getValue(ConstantUtils.NOTI_COUNT).equals(0)){
                notiCount.setText("");
            } else {
                notiCount.setText(preferencesHelper.getValue(ConstantUtils.NOTI_COUNT));
            }
        }
    }
    //------------------------------------Location Update Scheduler-------------------------------//

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            latitude = Double.valueOf(intent.getStringExtra("latutide"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size()>=1) {
                    String onlyCityName = addresses.get(0).getLocality();
//                Toast.makeText(getApplicationContext(), onlyCityName, Toast.LENGTH_LONG).show();
                    Integer userID = Integer.valueOf(preferencesHelper.getValue(ConstantUtils.USERID));
                    if (DialogFactory.checkInternetConnection(baseActivity)) {
                        homePresenter.updateLocation(userID, onlyCityName);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationUpdate.str_receiver));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    //------------------------------------Location Update Scheduler ends-------------------------------//

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void pushFragment(Fragment fragment, boolean detach) {

        /**
         * Check for one time insertion
         */
        if (fragment instanceof UpdateGroupFragment) {
            if (!(mNavController.getCurrentFrag() instanceof UpdateGroupFragment)) {
                mNavController.pushFragment(fragment, detach);
            }
        } else if (fragment instanceof UpdateEventFragment) {
            if (!(mNavController.getCurrentFrag() instanceof UpdateEventFragment)) {
                mNavController.pushFragment(fragment, detach);
            }
        } else if (fragment instanceof UpdateSelectedGroupFragment) {
            if (!(mNavController.getCurrentFrag() instanceof UpdateSelectedGroupFragment)) {
                mNavController.pushFragment(fragment, detach);
            }
        } else if (fragment instanceof AddGroupFragment) {
            if (!(mNavController.getCurrentFrag() instanceof AddGroupFragment)) {
                mNavController.pushFragment(fragment, detach);
            }
        } else {
            mNavController.pushFragment(fragment, detach);
        }

    }

    @Override
    public void replaceFragment(Fragment fragment) {

    }

    @Override
    public void navigationTitle(Fragment fragment) {

    }

    @Override
    public void popCurrentFragment() {
        mNavController.popFragment();

        if (mNavController.getCurrentFrag() instanceof HomeFragment) {
            MainBus.getInstance().post(new ContactListEvent(ConstantUtils.LOAD));
        }

    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grants) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grants);
                }
            }
        }

        if (grants.length > 0
                && grants[0] == PackageManager.PERMISSION_GRANTED) {
            homePresenter.startLoading();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage() + "", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (intent != null) {
                final String code = intent.getStringExtra("code");
                final String notification_id = intent.getStringExtra("notification_id");
                //read push notification on click
//                notificationPresenter.readNotificationStatus(notification_id);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (code != null) {
                            if (code.equals("1") || code.equals("2") || code.equals("5") || code.equals("6") || code.equals("7")) {
                                // CREATED OR UPDATED
                                homePresenter.readPushNotification(notification_id);
                                MainBus.getInstance().post(new HomeScrollEvent("0"));
                            } else if (code.equals("4")) {
                                homePresenter.readPushNotification(notification_id);
                                MainBus.getInstance().post(new HomeScrollEvent("1"));
                            } else if (code.equals("3")) {
                                homePresenter.readPushNotification(notification_id);
                                MainBus.getInstance().post(new HomeScrollEvent("2"));
                            }
                        }
                    }
                }, 1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }Log.d("five", "out");
    }


    // function to logout from application
    private void logout() {
        Integer userID = Integer.valueOf(preferencesHelper.getValue(ConstantUtils.USERID));
        homePresenter.updateLogoutAtUser(userID);
        InvitedApplication.get(baseActivity).getComponent().preferencesHelper().clearAll();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }


    public void ShowAddGroup() {
        addGroup.setVisibility(View.VISIBLE);
    }

    public void HideAddGroup() {
        addGroup.setVisibility(View.GONE);
    }

    public void showAddGroupIcon() {
        addGroupIcon.setVisibility(View.VISIBLE);
    }

    public void hideAddGroupIcon() {
        addGroupIcon.setVisibility(View.GONE);
    }

    public void hideNavMenu() {
        nav_menu.setVisibility(View.GONE);
    }
    public void showNavMenu() {
        nav_menu.setVisibility(View.VISIBLE);
    }

    public void popTwoFragments() {
        popCurrentFragment();
        popCurrentFragment();
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            for(int i=0; i<getSupportFragmentManager().getBackStackEntryCount(); i++){
                getSupportFragmentManager().popBackStack();
            }
            Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_profile) {
            //getSupportActionBar().setTitle("My Profile");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new UserProfileFragment(),null).addToBackStack(null).commit();
        } else if (id == R.id.nav_notification) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new NotificationFragment(),null).addToBackStack(null).commit();
        } else if (id == R.id.nav_logout) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            logout();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void clearBackStackInclusive(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void hashKeyPrint() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature:packageInfo.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KEYSHA", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
                }else{
                    Log.d("SPLASH CHECK","MAIN"+preferencesHelper.getValue(ConstantUtils.USERID));
                    startActivity(new Intent(getContext().getApplicationContext(), HomeActivity.class).putExtra("status","splash")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onFailure() {
        myDialog.dismiss();
        Toast.makeText(getApplicationContext(), "The request timed out" + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void pushNotiReadError() {

    }

    @Override
    public void pushNotiReadSuccess() {

    }

    @Override
    public void getNotiCountSuccess() {
        nav_noti_count.setText(preferencesHelper.getValue(ConstantUtils.NOTI_COUNT));
    }
}
