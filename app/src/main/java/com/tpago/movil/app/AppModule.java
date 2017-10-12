package com.tpago.movil.app;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.tpago.movil.DisplayDensity;
import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

  @Provides
  @Singleton
  ComponentBuilderSupplier componentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }

  @Provides
  @Singleton
  DisplayDensity provideDisplayDensity(Context context) {
    return DisplayDensity.get(context);
  }

  @Provides
  @Singleton
  SharedPreferences sharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides
  @Singleton
  KeyguardManager keyguardManager(Context context) {
    return (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
  }

  @Provides
  @Singleton
  FingerprintManagerCompat fingerprintManager(Context context) {
    return FingerprintManagerCompat.from(context);
  }
}
