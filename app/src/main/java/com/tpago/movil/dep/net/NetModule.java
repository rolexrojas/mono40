package com.tpago.movil.dep.net;

import android.content.Context;
import android.net.ConnectivityManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public final class NetModule {

  @Provides
  @Singleton
  ConnectivityManager provideConnectivityManager(Context context) {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  NetworkService provideNetworkService(ConnectivityManager connectivityManager) {
    return new ConnectivityManagerNetworkService(connectivityManager);
  }
}
