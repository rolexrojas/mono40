package com.tpago.movil;

import android.app.Application;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class App extends Application {
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
