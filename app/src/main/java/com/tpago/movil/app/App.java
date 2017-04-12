package com.tpago.movil.app;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.d.DepAppModule;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import timber.log.Timber;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class App extends Application {
  public static App get(Context context) {
    return (App) assertNotNull(context, "context == null").getApplicationContext();
  }

  private AppComponent component;

  @Inject OkHttpClient httpClient;

  private void initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugLogTree());
    }
    if (BuildConfig.CRASHLYTICS) {
      Timber.plant(new CrashlyticsLogTree(this));
    }
  }

  private void initDagger() {
    component = DaggerAppComponent.builder()
      .appModule(new AppModule(this))
      .depAppModule(new DepAppModule(this))
      .build();
    component.inject(this);
  }

  private void initializePicasso() {
    final Picasso picasso = new Picasso.Builder(this)
      .downloader(new OkHttp3Downloader(httpClient))
      .build();
    Picasso.setSingletonInstance(picasso);
  }

  public final AppComponent getComponent() {
    return component;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    initTimber();
    initDagger();
    initializePicasso();
  }
}
