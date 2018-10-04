package com.tpago.movil.app.ui.activity.base;

import android.content.Context;
import android.content.res.Configuration;

import com.tpago.movil.util.ObjectHelper;

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
