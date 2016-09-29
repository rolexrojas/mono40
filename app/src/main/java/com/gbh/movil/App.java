package com.gbh.movil;

import android.app.Application;
import android.support.annotation.NonNull;

import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class App extends Application {
  /**
   * TODO
   */
  private AppComponent component;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final AppComponent getComponent() {
    if (component == null) {
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
  }
}
