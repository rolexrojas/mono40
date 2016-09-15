package com.tpago.movil.ui;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * TODO
 *
 * @author hecvasro
 */
public class App extends Application {
  private static final String LANGUAGE_ES_DO = "es_DO";

  @Override
  public void onCreate() {
    super.onCreate();
    // Sets spanish from Dominican Republic as the default locale.
    final Resources resources = getResources();
    if (resources != null) {
      final Configuration configuration = resources.getConfiguration();
      if (configuration != null) {
        final Locale locale = new Locale(LANGUAGE_ES_DO);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
      }
    }
  }
}
