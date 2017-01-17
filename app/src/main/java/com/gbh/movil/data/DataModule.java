package com.gbh.movil.data;

import android.content.Context;

import com.gbh.movil.data.api.ApiModule;
import com.gbh.movil.data.net.NetModule;
import com.gbh.movil.data.pos.PosModule;
import com.gbh.movil.data.repo.RepoModule;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.data.res.LocalAssetProvider;
import com.gbh.movil.data.session.SessionModule;
import com.gbh.movil.domain.DeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = {
  NetModule.class,
  SessionModule.class,
  ApiModule.class,
  PosModule.class,
  RepoModule.class
})
public final class DataModule {
  @Provides
  @Singleton
  NfcHandler provideNfcHandler(Context context) {
    return new NfcHandler(context);
  }

  @Provides
  @Singleton
  DeviceManager provideDeviceManager(Context context) {
    return new AndroidDeviceManager(context);
  }

  @Provides
  @Singleton
  StringHelper provideMessageHelper(Context context) {
    return new StringHelper(context.getResources());
  }

  @Provides
  @Singleton
  SchedulerProvider provideSchedulerProvider() {
    return new SchedulerProvider();
  }

  @Provides
  @Singleton
  AssetProvider provideAssetProvider() {
    return new LocalAssetProvider();
  }
}
