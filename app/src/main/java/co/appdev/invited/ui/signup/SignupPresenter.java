/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.ui.signup;

import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.model.DefaultResponse;
import co.appdev.invited.injection.ConfigPersistent;
import co.appdev.invited.ui.base.BasePresenter;
import co.appdev.invited.util.NetworkUtil;
import co.appdev.invited.util.RxUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class SignupPresenter extends BasePresenter<SignupMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public SignupPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SignupMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }



    public void userSignupValidation(String fName, String lName, final String phone, String email, final String password, String cpassword) {
        RxUtil.unsubscribe(mSubscription);

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null) {
            token = "";
        }

        mSubscription = mDataManager.getRibotsService().userSignupValidation(fName, lName, phone, email, password, cpassword, token)

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

                        getMvpView().onSignupFailure(e.getMessage());

                    }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {

                        if (defaultResponse.getErrorEmail() != null) {
                            getMvpView().onSignupFailure(defaultResponse.getErrorEmail().get(0));
                        } else if (defaultResponse.getErrorphone() != null) {
                            getMvpView().onSignupFailure(defaultResponse.getErrorphone().get(0));
                        } else if (defaultResponse.getStatus().equals(NetworkUtil.SUCCESS)) {
                              getMvpView().loadCodeScreen();
                         }
                    }
                });

    }

}
