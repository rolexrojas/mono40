package com.tpago.movil.dep;

import android.app.Application;
import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.d.DepAppModule;
import com.tpago.movil.job.JobModule;
import com.tpago.movil.session.UpdateUserCarrierJob;
import com.tpago.movil.session.UpdateUserNameJob;
import com.tpago.movil.session.UpdateUserPictureJob;
import com.tpago.movil.util.ObjectHelper;

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

    private com.tpago.movil.app.AppComponent component;

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
        component = com.tpago.movil.app.DaggerAppComponent.builder()
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
