package com.tpago.movil.dep;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.d.DepAppModule;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class App extends Application implements ComponentBuilderSupplierContainer {

  public static App get(Context context) {
    return (App) ObjectHelper.checkNotNull(context, "context")
      .getApplicationContext();
  }

  private com.tpago.movil.app.AppComponent component;

  private boolean visible = false;

  @Inject ComponentBuilderSupplier componentBuilderSupplier;
  @Inject OkHttpClient httpClient;

  private void initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugLogTree());
    }
    if (BuildConfig.MODE_CRASH_REPORTING) {
      Timber.plant(new CrashlyticsLogTree(this));
    }
  }

  private void initDagger() {
    component = com.tpago.movil.app.DaggerAppComponent.builder()
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
  }

  @Override
  public ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }
}
