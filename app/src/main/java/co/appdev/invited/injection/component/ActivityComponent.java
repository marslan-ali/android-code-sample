package co.appdev.invited.injection.component;

import co.appdev.invited.injection.PerActivity;
import co.appdev.invited.injection.module.ActivityModule;
import co.appdev.invited.ui.changePassword.ChangePasswordActivity;
import co.appdev.invited.ui.codeVerfication.CodeVerificationActivity;
import co.appdev.invited.ui.createList.AddGroupFragment;
import co.appdev.invited.ui.base.BaseFragment;
import co.appdev.invited.ui.contactList.ContactListFragment;
import co.appdev.invited.ui.createEvent.CreateEventFragment;
import co.appdev.invited.ui.invitesSent.InvitesSentFragment;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.homeFragment.HomeFragment;
import co.appdev.invited.ui.myEvents.MyEventsFragment;
import co.appdev.invited.ui.invitesRecieved.InvitesRecievedFragment;
import co.appdev.invited.ui.myEventsDetail.MyEventsDetailFragment;
import co.appdev.invited.ui.notification.NotificationFragment;
import co.appdev.invited.ui.sendReport.SendReportFragment;
import co.appdev.invited.ui.signin.SignInActivity;
import co.appdev.invited.ui.signup.SignupActivity;
import co.appdev.invited.ui.updateEvent.UpdateEventFragment;
import co.appdev.invited.ui.updateListFromContacts.UpdateGroupFragment;
import co.appdev.invited.ui.updateListSelected.UpdateSelectedGroupFragment;
import co.appdev.invited.ui.updateUserProfile.UpdateUserProfileFragment;
import co.appdev.invited.ui.userProfile.UserProfileFragment;
import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SignupActivity signupActivity);
    void inject(SignInActivity signInActivity);
    void inject(ChangePasswordActivity changePasswordActivity);
    void inject(HomeActivity homeActivity);
    void inject(BaseFragment baseFragment);
    void inject(HomeFragment homeFragment);
    void inject(AddGroupFragment addGroupFragment);
    void inject(ContactListFragment createListFragment);
    void inject(UpdateGroupFragment updateGroupFragment);
    void inject(CreateEventFragment createEventFragment);
    void inject(InvitesSentFragment yourEventsFragment);
    void inject(UpdateEventFragment updateEventFragment);
    void inject(InvitesRecievedFragment requestsFragment);
    void inject(MyEventsFragment receivedFragment);
    void inject(UpdateSelectedGroupFragment updateSelectedGroupFragment);
    void inject(MyEventsDetailFragment myEventsDetailFragment);
    void inject(CodeVerificationActivity codeVerificationActivity);
    void inject(SendReportFragment sendReportFragment);
    void inject(UserProfileFragment userProfileFragment);
    void inject(UpdateUserProfileFragment updateUserProfileFragment);
    void inject(NotificationFragment notificationFragment);


}
