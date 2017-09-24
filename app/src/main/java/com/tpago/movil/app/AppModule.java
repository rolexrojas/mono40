package com.tpago.movil.app;

import android.content.Context;

import com.google.gson.Gson;
import com.tpago.movil.Avatar;
import com.tpago.movil.ConfigManager;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.UserStore;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.data.DeviceIdSupplier;
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
  DeviceManager provideDeviceManager(DeviceIdSupplier deviceIdSupplier) {
    return DeviceManager.create(deviceIdSupplier);
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
  UserStore provideUserStore(
    Gson gson,
    SharedPreferencesCreator sharedPreferencesCreator,
    Avatar avatar
  ) {
    return new UserStore(gson, sharedPreferencesCreator, avatar);
  }

  @Provides
  @Singleton
  StringResolver provideStringResolver(Context context) {
    return new StringResolver(context);
  }
}
