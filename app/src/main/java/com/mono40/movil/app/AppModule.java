package com.mono40.movil.app;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.mono40.movil.DisplayDensity;
import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.upgrade.AppUpgradeModule;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppUpgradeModule.class)
public final class AppModule {

  @Provides
  @Singleton
  DisplayDensity provideDisplayDensity(Context context) {
    return DisplayDensity.get(context);
  }

  @Provides
  @Singleton
  DrawableMapper drawableMapper(Context context) {
    return (drawableId) -> ContextCompat.getDrawable(context, drawableId);
  }

  @Provides
  @Singleton
  StringMapper stringMapper(Context context) {
    return context::getString;
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

  @Provides
  @Singleton
  ComponentBuilderSupplier componentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }
}
