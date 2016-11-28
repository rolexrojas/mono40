package com.gbh.movil.data;

import android.content.Context;

import com.gbh.movil.data.api.ApiModule;
import com.gbh.movil.data.net.NetModule;
import com.gbh.movil.data.repo.RepoModule;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.data.res.LocalAssetProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = { NetModule.class, ApiModule.class, RepoModule.class })
public final class DataModule {
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
