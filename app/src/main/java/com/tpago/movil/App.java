package com.tpago.movil;

import android.app.Application;

import com.tpago.movil.util.Objects;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class App extends Application {
  private AppComponent component;

  public final AppComponent getComponent() {
    if (Objects.isNull(component)) {
      component = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
    }
    return component;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugLogTree());
    }
    if (BuildConfig.CRASHLYTICS) {
      Timber.plant(new CrashlyticsLogTree(this));
    }
  }
}
