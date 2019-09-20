/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import co.appdev.invited.injection.component.ApplicationComponent;
import co.appdev.invited.injection.component.DaggerApplicationComponent;
import co.appdev.invited.injection.module.ApplicationModule;
import co.appdev.invited.util.ConstantUtils;
import timber.log.Timber;

import static co.appdev.invited.util.ConstantUtils.HOST_NAME;

public class InvitedApplication extends Application {

    ApplicationComponent mApplicationComponent;
    private static Context mContext;
    private String envUrl;
    private String dobAlertText;

    @Override
    public void onCreate() {

        Log.d("APP ENV",BuildConfig.APP_ENV);
        super.onCreate();
        InvitedApplication.mContext = getApplicationContext();
        FirebaseApp.initializeApp(mContext);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //    Fabric.with(this, new Crashlytics());
        }

        if (BuildConfig.APP_ENV == "DEVELOPMENT"){
            envUrl = "dev.invited.shayansolutions.com";
            ConstantUtils.HOST_NAME = envUrl;
//            Log.d("devURL", envUrl);
        } else {
            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            // Initialize a new JsonObjectRequest instance
            String url = "http://invited.shayansolutions.com/config.json";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        envUrl = response.getString("URL");
                        ConstantUtils.HOST_NAME = envUrl;
                        dobAlertText = response.getString("BirthdayAlert");
                        ConstantUtils.DOB_ALERT = dobAlertText;
//                        Log.d("proUrl", envUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                }
            });

            // Add JsonObjectRequest to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }

    public static Context getContext() {
        return InvitedApplication.mContext;
    }


    public static InvitedApplication get(Context context) {
        return (InvitedApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
