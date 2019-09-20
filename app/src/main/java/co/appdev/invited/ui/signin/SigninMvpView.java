/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.ui.signin;


import co.appdev.invited.ui.base.MvpView;

public interface SigninMvpView extends MvpView {

    void onSigninSuccess();

    void onSigninFailure(String error);

 }
