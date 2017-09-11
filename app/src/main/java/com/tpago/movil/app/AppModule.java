package com.tpago.movil.app;

import android.content.Context;

import com.tpago.movil.Avatar;
import com.tpago.movil.ConfigManager;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.UserStore;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.io.Files;
import com.tpago.movil.util.Preconditions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
final class AppModule {
  private final App app;

  AppModule(App app) {
    this.app = Preconditions.assertNotNull(app, "app == null");
  }

  @Provides
  @Singleton
  Context provideContext() {
    return app;
  }

  @Provides
  @Singleton
  DisplayDensity provideDisplayDensity(Context context) {
    return DisplayDensity.get(context);
  }

  // Deprecated providers.
  @Provides
  @Singleton
  DeviceManager provideDeviceManager(Context context) {
    return new DeviceManager(context);
  }

  @Provides
  @Singleton
  Avatar provideAvatar(Context context) {
    return Avatar.create(Files.createInternalPictureFile(context, Avatar.class.getSimpleName()));
  }

  @Provides
  @Singleton
  ConfigManager provideConfigManager(SharedPreferencesCreator sharedPreferencesCreator) {
    return new ConfigManager(sharedPreferencesCreator);
  }

  @Provides
  @Singleton
  UserStore provideUserStore(SharedPreferencesCreator sharedPreferencesCreator, Avatar avatar) {
    return new UserStore(sharedPreferencesCreator, avatar);
  }

  @Provides
  @Singleton
  StringResolver provideStringResolver(Context context) {
    return new StringResolver(context);
  }
}
