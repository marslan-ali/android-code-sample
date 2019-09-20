package co.appdev.invited.data.local;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.appdev.invited.injection.ApplicationContext;


@Singleton
public class PreferencesHelper implements UserDataHelper {

    private Context context;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        this.context = context;
    }


    @Override
    public String getValue(String key) {
        return PreferencesDataHelper.retrieve(context, key);
    }

    @Override
    public void clearAll() {
        PreferencesDataHelper.clearPref(context);
    }

    public void storeValue(String key, String value) {
        PreferencesDataHelper.store(context, key, value);
    }

}
