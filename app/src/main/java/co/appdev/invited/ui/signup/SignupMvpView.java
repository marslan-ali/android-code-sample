/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.ui.signup;


import co.appdev.invited.ui.base.MvpView;

public interface SignupMvpView extends MvpView {

    void onSignupSuccess(String msg);

    void onSignupFailure(String error);

    void loadCodeScreen();
}
