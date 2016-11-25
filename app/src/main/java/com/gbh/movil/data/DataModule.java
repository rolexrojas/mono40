package com.gbh.movil.data;

import android.content.Context;

import com.gbh.movil.data.api.ApiModule;
import com.gbh.movil.data.net.NetModule;
import com.gbh.movil.data.repo.RepoModule;
import com.gbh.movil.data.res.LocalResourceProvider;
import com.gbh.movil.data.res.ResourceProvider;

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
  ResourceProvider provideResourceProvider() {
    return new LocalResourceProvider();
  }
}
