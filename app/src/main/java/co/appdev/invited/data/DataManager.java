package co.appdev.invited.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.appdev.invited.data.local.DatabaseRealm;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.data.remote.InvitedService;
import rx.Observable;

@Singleton
public class DataManager {

    private final InvitedService mRibotsService;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseRealm mDatabaseRealm;

    @Inject
    public DataManager(InvitedService ribotsService, PreferencesHelper preferencesHelper, DatabaseRealm databaseRealm) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        this.mDatabaseRealm=databaseRealm;

    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public InvitedService getRibotsService(){return  mRibotsService;}

    public DatabaseRealm getmDatabaseRealm(){
        return mDatabaseRealm;
    }

    public Observable<List<Users>> getUsers() {
        return mDatabaseRealm.getUsers();
    }

}
