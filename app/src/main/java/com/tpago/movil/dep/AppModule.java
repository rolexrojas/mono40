package com.tpago.movil.dep;

import android.content.Context;

import com.tpago.movil.DisplayDensity;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.dep.data.DataModule;
import com.tpago.movil.dep.io.Files;
import com.tpago.movil.dep.content.ContentModule;
import com.tpago.movil.d.DepAppModule;
import com.tpago.movil.dep.api.ApiModule;
import com.tpago.movil.d.data.DepDataModule;
import com.tpago.movil.d.domain.DomainModule;
import com.tpago.movil.dep.net.NetModule;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module(
  includes = {
    ApiModule.class,
    DataModule.class,
    ContentModule.class,
    DepAppModule.class,
    DepDataModule.class,
    DomainModule.class,
    NetModule.class
  }
)
public final class AppModule {

  private final App app;

  AppModule(App app) {
    this.app = ObjectHelper.checkNotNull(app, "app");
  }

  @Provides
  @Singleton
  Context provideContext() {
    return app;
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
  StringResolver provideStringResolver(Context context) {
    return new StringResolver(context);
  }
}
