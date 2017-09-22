package com.tpago.movil.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

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
public final class App extends MultiDexApplication {

  public static App get(Context context) {
    return (App) assertNotNull(context, "context == null").getApplicationContext();
  }

  private AppComponent component;

  private boolean visible = false;

  @Inject
  OkHttpClient httpClient;

  private void initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugLogTree());
    }
    if (BuildConfig.MODE_CRASH_REPORTING) {
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
  }
}
