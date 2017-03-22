package com.tpago.movil.app;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.d.DepAppModule;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class App extends Application {
  public static App get(Context context) {
    return (App) Preconditions.checkNotNull(context, "context == null")
      .getApplicationContext();
  }

  private AppComponent component;

  @Inject OkHttpClient httpClient;

  public final AppComponent getAppComponent() {
    if (Objects.isNull(component)) {
      component = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .depAppModule(new DepAppModule(this))
        .build();
    }
    return component;
  }

  @Deprecated
  public final AppComponent getComponent() {
    return getAppComponent();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    // Injects all annotated dependencies.
    getAppComponent().inject(this);
    // Initializes the picasso builder.
    final Picasso.Builder picassoBuilder = new Picasso.Builder(this)
      .downloader(new OkHttp3Downloader(httpClient));
    // Configures the app for debugging.
    if (BuildConfig.DEBUG) {
      // Plants the debugging tree.
      Timber.plant(new DebugLogTree());
    }
    // Configures the app for crash reporting.
    if (BuildConfig.CRASHLYTICS) {
      // Plants the crashlytics tree.
      Timber.plant(new CrashlyticsLogTree(this));
    }
    // Initializes Picasso.
    Picasso.setSingletonInstance(picassoBuilder.build());
  }
}
