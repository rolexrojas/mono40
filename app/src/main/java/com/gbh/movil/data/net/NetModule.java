package com.gbh.movil.data.net;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class NetModule {
  @Provides
  @Singleton
  NetworkHelper provideNetworkHelper(Context context) {
    return new ConnectivityManagerNetworkHelper(context);
  }
}
