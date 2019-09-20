/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       shayansolutions@gmail.com
 */
package co.appdev.invited.ui.signin;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.model.DefaultResponse;
import co.appdev.invited.data.model.LoginResponse;
import co.appdev.invited.injection.ConfigPersistent;
import co.appdev.invited.ui.base.BasePresenter;
import co.appdev.invited.util.ConstantUtils;
import co.appdev.invited.util.RxUtil;
import co.appdev.invited.util.ViewUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class SigninPresenter extends BasePresenter<SigninMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public SigninPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SigninMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void userSignin(String phone, String password) {
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibotsService().userLogin("password", "1", "mA696UDP5ibROH9aeqAOSJGGsZIiVHR0KqXYZRzh", phone, password, "*", "user")

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {

                        }
                        HttpException error = (HttpException)e;
                        try {
                            JSONObject  errorBody = new JSONObject( error.response().errorBody().string());
                            String message = errorBody.getString("message");
                            getMvpView().onSigninFailure(message);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {

                        if(loginResponse.getUserId()!=null && !loginResponse.getUserId().equals("")) {
                            mDataManager.getPreferencesHelper().storeValue(ConstantUtils.USERID,loginResponse.getUserId());
                            mDataManager.getPreferencesHelper().storeValue(ConstantUtils.TOKEN_TYPE,loginResponse.getTokenType());
                            mDataManager.getPreferencesHelper().storeValue(ConstantUtils.ACCESS_TOKEN,loginResponse.getAccessToken());
                            ViewUtil.setToken(mDataManager.getPreferencesHelper().getValue(ConstantUtils.TOKEN_TYPE) + " " + mDataManager.getPreferencesHelper().getValue(ConstantUtils.ACCESS_TOKEN));
                            updateToken (loginResponse.getUserId());
                            getMvpView().onSigninSuccess();


                        }
                    }
                });

    }

    public void updateToken(String userId) {

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null){
            token = "";
        }

        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibotsService().updateToken(userId, token, "android")

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {

                        }

                     }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {

                        if(defaultResponse!=null) {

                        }
                    }
                });

    }
}
