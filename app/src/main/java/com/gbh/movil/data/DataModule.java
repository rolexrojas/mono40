package com.gbh.movil.data;

import android.content.Context;
import android.net.ConnectivityManager;

import com.gbh.movil.data.api.ApiModule;
import com.gbh.movil.data.net.ConnectivityManagerNetworkHelper;
import com.gbh.movil.data.net.NetModule;
import com.gbh.movil.data.repo.RepoModule;
import com.gbh.movil.data.net.NetworkHelper;

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
  MessageHelper provideMessageHelper(Context context) {
    return new MessageHelper(context.getResources());
  }

  @Provides
  @Singleton
  NetworkHelper provideNetworkHelper(Context context) {
    return new ConnectivityManagerNetworkHelper((ConnectivityManager) context.getSystemService(
      Context.CONNECTIVITY_SERVICE));
  }

  @Provides
  @Singleton
  SchedulerProvider provideSchedulerProvider() {
    return new SchedulerProvider();
  }
}
