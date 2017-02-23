package com.tpago.movil.app;

import android.app.Application;
import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class App extends Application {
  private AppComponent component;

  public static App get(Context context) {
    return (App) Preconditions.checkNotNull(context, "context == null")
      .getApplicationContext();
  }

  public final AppComponent getAppComponent() {
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
