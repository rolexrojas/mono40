package com.gbh.movil.data;

import android.content.Context;
import android.net.ConnectivityManager;

import com.gbh.movil.data.api.ApiModule;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.data.repo.RepoModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module(includes = { ApiModule.class, RepoModule.class })
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
  MessageHelper provideMessageHelper(Context context) {
    return new MessageHelper(context.getResources());
  }

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
  NetworkHelper provideNetworkHelper(Context context) {
    return new NetworkHelper((ConnectivityManager) context.getSystemService(
      Context.CONNECTIVITY_SERVICE));
  }
}
