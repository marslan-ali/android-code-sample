package co.appdev.invited.data.local;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.appdev.invited.injection.ApplicationContext;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class DatabaseRealm {

    private Context mContext;

    RealmConfiguration realmConfiguration;

    @Inject
    public DatabaseRealm(@ApplicationContext Context context) {
        this.mContext = context;
        setup();
    }

    private void setup() {
        if (realmConfiguration == null) {
            Realm.init(mContext);
            // create your Realm configuration
            realmConfiguration = new RealmConfiguration.
                    Builder().
                    deleteRealmIfMigrationNeeded().
                    build();
            Realm.setDefaultConfiguration(realmConfiguration);

        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public void close() {
        getRealmInstance().close();
    }

    public Observable<Users> setUsers(final List<Users> users) {
        return Observable.create(new Observable.OnSubscribe<Users>() {
            @Override
            public void call(Subscriber<? super Users> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Realm realm = getRealmInstance();
                realm.beginTransaction();
                for (Users user : users) {
                    realm.copyToRealm(user);
                }
                realm.commitTransaction();
                subscriber.onCompleted();

            }
        });
    }

    public Observable<List<Users>> getUsers() {
        return Observable.just(getRealmInstance().copyFromRealm(getRealmInstance().where(Users.class).findAll()));

    }

    public void setContacts(final List<Contact> contacts) {

        Realm realm = getRealmInstance();

        realm.beginTransaction();

        realm.delete(Contact.class);
        for (Contact contact : contacts) {
            realm.copyToRealm(contact);
        }

        realm.commitTransaction();

    }

    public List<Contact> getContacts() {
        return getRealmInstance().copyFromRealm(getRealmInstance().where(Contact.class).findAll());
    }

    public Contact getContact(String phone) {

        return getRealmInstance().where(Contact.class).equalTo("ContactNumber", phone).findFirst();
    }

}
