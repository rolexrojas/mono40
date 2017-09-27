package com.tpago.movil.app.ui;

import android.content.Context;
import android.content.res.Configuration;

import com.tpago.movil.util.ObjectHelper;

import java.util.Locale;

/**
 * @author Hector Vasquez
 */
final class LocaleContextWrapper {

  static Context wrap(Context context) {
    final Configuration configuration = ObjectHelper.checkNotNull(context, "context")
      .getResources()
      .getConfiguration();
    configuration.setLocale(new Locale("es"));

    return context.createConfigurationContext(configuration);
  }
}
