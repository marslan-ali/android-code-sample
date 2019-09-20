package co.appdev.invited.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import co.appdev.invited.data.remote.InvitedService;
import co.appdev.invited.injection.ApplicationContext;
import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    InvitedService provideRibotsService() {
        return InvitedService.Creator.newInvitedServices();
    }

}
