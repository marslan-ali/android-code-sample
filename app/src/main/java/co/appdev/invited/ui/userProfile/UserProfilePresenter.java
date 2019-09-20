package co.appdev.invited.ui.userProfile;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import javax.inject.Inject;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.data.model.User;
import co.appdev.invited.ui.base.BasePresenter;
import co.appdev.invited.util.ConstantUtils;
import co.appdev.invited.util.RxUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserProfilePresenter extends BasePresenter<UserProfileMvpView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    public UserProfilePresenter(DataManager dataManager){

        mDataManager = dataManager;
    }

    @Override
    public void attachView(UserProfileMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription !=null) mSubscription.unsubscribe();
    }

    //get User Profile
    public void getRequestForUserProfile(){
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibotsService().getUserProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserProfile>() {

                    @Override
                    public void onCompleted() {
                        Log.d("one", "in complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("two", "in error");
                        if (e instanceof HttpException) {
                            HttpException error = (HttpException)e;
                            if(error.code() == 401){
                                getMvpView().onRequestFailure();
                            }
                            Log.d("401", String.valueOf(error.code()));
                        }
                    }

                    @Override
                    public void onNext(UserProfile userProfile) {
                        if (userProfile.getUser() != null && !userProfile.getUser().equals("")) {
                            Gson gson = new Gson();
                            Object userInfoObj = gson.toJson(userProfile.getUser());

//                        Object data = userInfoObj.toString();
//                        String userfName = parseGsonData((String) data).getFirstName();

                            // USer info store in USEROBJECT in preference
                            // mDataManager.getPreferencesHelper().storeValue(ConstantUtils.USEROBJECT,gson.toJson(userInfoObj));
                            mDataManager.getPreferencesHelper().storeValue(ConstantUtils.USEROBJECT, (String) userInfoObj);
                        }
                        getMvpView().onRequestSuccess();
                    }
                });
    }

    public User parseGsonData(String data){
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {
        }.getType();
        User user = gson.fromJson(data, type);
        return user;
    }

    public String parseGsontoStringData(User user){
        Gson gson = new Gson();
        String s = gson.toJson(user);
        return s;
    }
}
