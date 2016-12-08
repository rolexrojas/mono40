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
 * TODO
 *
 * @author hecvasro
 */
@Module(includes = { NetModule.class, ApiModule.class, SessionModule.class, PosModule.class,
  RepoModule.class })
public final class DataModule {
  /**
   * TODO
   *
   * @param context
   *   TODO
   *
   * @return TODO
   */
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
