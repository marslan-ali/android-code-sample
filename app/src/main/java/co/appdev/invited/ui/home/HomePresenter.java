package co.appdev.invited.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.model.DefaultResponse;
import co.appdev.invited.injection.ApplicationContext;
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

import static co.appdev.invited.InvitedApplication.getContext;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeMvpView> {

    private final DataManager mDataManager;
    private final Context mContext;
    private Subscription mSubscription;
    private List<Contact> contactsList = new ArrayList();

    @Inject
    public HomePresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        mContext = context;
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

    public void startLoading() {
        new LoadContactsJob().execute();
    }


    public class LoadContactsJob extends AsyncTask<Object, Object, Object> {


        @Override
        protected String doInBackground(Object... params) {

            String permission = Manifest.permission.READ_CONTACTS;
            int res = getContext().checkCallingOrSelfPermission(permission);

            if (res == PackageManager.PERMISSION_GRANTED) {
                getAllContacts();
                return "";
            }
            return permission;

        }

        @Override
        protected void onPostExecute(Object result) {

        }
    }

    private void getAllContacts() {

        contactsList.clear();

        Contact contact;

        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contact = new Contact();
                    contact.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor != null) {

                        if (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                            contact.setContactNumber(phoneNumber);
                        }
                    }

                    phoneCursor.close();

                    contact.setIconDp(ViewUtil.getRandomIcon());
                    contact.setStatus(false);

                    contactsList.add(contact);
                }
            }
        }

        if (cursor != null) {

            cursor.close();

        }

        if (contactsList.size() > 0) {

            mDataManager.getmDatabaseRealm().setContacts(contactsList);
            mDataManager.getmDatabaseRealm().getContacts();

        }
    }

    public void updateLocation(Integer userId, String address){
        RxUtil.unsubscribe(mSubscription);

        mSubscription = mDataManager.getRibotsService().updateLocationOnceInADay(userId,address)

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
                        if(defaultResponse.getStatus().equals("success")){
                        }
                    }
                });
    }

    public void updateLogoutAtUser(Integer userId){
        RxUtil.unsubscribe(mSubscription);

        mSubscription = mDataManager.getRibotsService().updateLogoutAt(userId)

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
                        if(defaultResponse.getStatus().equals("success")){
                        }
                    }
                });
    }

    public void getNotifications(String userId) {

        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibotsService().getNotificationsList(userId)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Notification>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException error = (HttpException)e;
                            if(error.code() == 401){
                                getMvpView().onRequestFailure();
                            }
                        }
                        if(e instanceof SocketTimeoutException){
                            getMvpView().onFailure();
                        }
                    }

                    @Override
                    public void onNext(Notification notification) {
                        if(notification.getUnReadNotificationCount()!=null && !notification.getUnReadNotificationCount().equals("")) {
                            mDataManager.getPreferencesHelper().storeValue(ConstantUtils.NOTI_COUNT, notification.getUnReadNotificationCount());
                            getMvpView().getNotiCountSuccess();
                        }
                    }
                });
    }

    public void readPushNotification(String notification_id) {

        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibotsService().readNotification(notification_id)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException error = (HttpException)e;
                            if(error.code() == 401){
                                getMvpView().onRequestFailure();
                            }
                        }
                        if(e instanceof SocketTimeoutException){
                            getMvpView().onFailure();
                        }
                    }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {
                        if (defaultResponse.getStatus().equals("success")) {
                            getMvpView().pushNotiReadSuccess();
                        } else {
                            getMvpView().pushNotiReadError();
                        }
                    }
                });
    }

}
