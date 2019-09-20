package co.appdev.invited.ui.home;


import co.appdev.invited.ui.base.MvpView;

public interface HomeMvpView extends MvpView {

    void onRequestFailure();

    void onFailure();

    void pushNotiReadError();

    void pushNotiReadSuccess();

    void getNotiCountSuccess();
}
