package co.appdev.invited.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.local.DatabaseRealm;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.data.remote.InvitedService;
import co.appdev.invited.injection.ApplicationContext;
import co.appdev.invited.injection.module.ApplicationModule;
import co.appdev.invited.util.RxEventBus;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    InvitedService ribotsService();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    RxEventBus eventBus();
    DatabaseRealm databaseRealm();

}
