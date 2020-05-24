package com.mono40.movil.app.ui.activity.base;

import android.content.Context;
import android.content.res.Configuration;

import com.mono40.movil.util.ObjectHelper;

import java.util.Locale;

/**
 * @author Hector Vasquez
 */
final class ContextWrapperLocale {

  static Context wrap(Context context) {
    final Configuration configuration = ObjectHelper.checkNotNull(context, "context")
      .getResources()
      .getConfiguration();
    configuration.setLocale(new Locale("es"));

    return context.createConfigurationContext(configuration);
  }
}
