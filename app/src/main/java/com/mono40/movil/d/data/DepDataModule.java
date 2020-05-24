package com.mono40.movil.d.data;

import android.content.Context;

import com.mono40.movil.d.data.api.ApiModule;
import com.mono40.movil.d.data.net.NetModule;
import com.mono40.movil.d.data.pos.PosModule;
import com.mono40.movil.d.domain.DepDeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(
  includes = {
    NetModule.class,
    ApiModule.class,
    PosModule.class
  }
)
@Deprecated
public final class DepDataModule {

  @Provides
  @Singleton
  DepDeviceManager provideDeviceManager(Context context) {
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
}
