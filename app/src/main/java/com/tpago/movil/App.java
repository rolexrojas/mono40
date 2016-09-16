package com.tpago.movil;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class App extends Application {
  private static final String LANGUAGE_ES = "es";

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
    // Sets spanish from Dominican Republic as the default locale.
    final Resources resources = getResources();
    if (resources != null) {
      final Configuration configuration = resources.getConfiguration();
      if (configuration != null) {
        final Locale locale = new Locale(LANGUAGE_ES);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
      }
    }
  }
}
