package com.gbh.movil;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.gbh.movil.misc.Utils;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class App extends Application {
  private AppComponent component;

  @NonNull
  public final AppComponent getComponent() {
    if (Utils.isNull(component)) {
      component = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
    }
    return component;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    // Initializes Timber.
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    // Initializes Fabric.
    Fabric.with(this, new Crashlytics());
  }
}
