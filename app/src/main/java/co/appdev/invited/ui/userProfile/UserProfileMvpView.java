package co.appdev.invited.ui.userProfile;

import co.appdev.invited.ui.base.MvpView;

public interface UserProfileMvpView extends MvpView {
        public void onRequestSuccess();

    void onRequestFailure();
}
