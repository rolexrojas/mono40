package com.tpago.movil.app;

import android.content.SharedPreferences;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;

/**
 * @author hecvasro
 */
public final class App extends com.tpago.movil.dep.App
  implements ComponentBuilderSupplierContainer {

  @Override
  public void onCreate() {
    super.onCreate();
    final SharedPreferences sharedPreferences = this
      .getSharedPreferences(BuildConfig.APPLICATION_ID + ".UserStore", MODE_PRIVATE);
    sharedPreferences.edit()
      .putString("phoneNumber", "8098829887")
      .putString("email", "hecvasro@gmail.com")
      .putString("firstName", "Hector")
      .putString("lastName", "Vasquez")
      .apply();
  }
}
