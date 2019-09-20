package co.appdev.invited.ui.homeFragment;

import javax.inject.Inject;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.injection.ConfigPersistent;
import co.appdev.invited.ui.base.BasePresenter;
import co.appdev.invited.ui.home.HomeMvpView;
import rx.Subscription;

@ConfigPersistent
public class HomeFragmentPresenter extends BasePresenter<HomeMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public HomeFragmentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(HomeMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

}
