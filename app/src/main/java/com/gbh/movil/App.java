package com.gbh.movil;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Locale;

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
    // Sets spanish as the default locale.
    final Resources resources = getResources();
    if (resources != null) {
      final Configuration configuration = resources.getConfiguration();
      if (configuration != null) {
        final Locale locale = new Locale("es");
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
      }
    }
  }
}
