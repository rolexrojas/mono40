package com.tpago.movil.dep.data;

import android.content.Context;

import com.tpago.movil.dep.data.api.ApiModule;
import com.tpago.movil.dep.data.net.NetModule;
import com.tpago.movil.dep.data.pos.PosModule;
import com.tpago.movil.dep.data.repo.RepoModule;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.data.res.LocalAssetProvider;
import com.tpago.movil.dep.data.session.SessionModule;
import com.tpago.movil.dep.domain.DeviceManager;

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
@Deprecated
public final class DepDataModule {
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
