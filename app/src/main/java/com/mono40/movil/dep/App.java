package com.mono40.movil.dep;

import android.app.Application;
import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.mono40.movil.BuildConfig;
import com.mono40.movil.app.AppComponent;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.di.ComponentBuilderSupplierContainer;
import com.mono40.movil.d.DepAppModule;
import com.mono40.movil.job.JobModule;
import com.mono40.movil.session.UpdateUserCarrierJob;
import com.mono40.movil.session.UpdateUserNameJob;
import com.mono40.movil.session.UpdateUserPictureJob;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class App extends Application implements ComponentBuilderSupplierContainer,
        DependencyInjector {

    public static App get(Context context) {
        final Context appContext = ObjectHelper.checkNotNull(context, "context")
                .getApplicationContext();
        if (!(appContext instanceof App)) {
            throw new ClassCastException("!(appContext instanceof App)");
        }
        return (App) appContext;
    }

    private com.mono40.movil.app.AppComponent component;

    private boolean visible = false;

    @Inject
    ComponentBuilderSupplier componentBuilderSupplier;
    @Inject
    OkHttpClient httpClient;

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        if (BuildConfig.MODE_CRASH_REPORTING) {
            Timber.plant(new CrashlyticsLogTree(this));
        }
    }

    private void initDagger() {
        component = com.mono40.movil.app.DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .depAppModule(new DepAppModule(this))
                .jobModule(JobModule.create(this))
                .build();
        component.inject(this);
    }

    private void initializePicasso() {
    }

    @Deprecated
    public final AppComponent component() {
        return component;
    }

    public final boolean isVisible() {
        return visible;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        initDagger();
        initializePicasso();
//    FirebaseApp.initializeApp(this);
    }

    @Override
    public ComponentBuilderSupplier componentBuilderSupplier() {
        return this.componentBuilderSupplier;
    }

    @Override
    public void inject(Job job) {
        if (job instanceof UpdateUserNameJob) {
            this.component.inject((UpdateUserNameJob) job);
        } else if (job instanceof UpdateUserPictureJob) {
            this.component.inject((UpdateUserPictureJob) job);
        } else if (job instanceof UpdateUserCarrierJob) {
            this.component.inject((UpdateUserCarrierJob) job);
        }
    }
}
