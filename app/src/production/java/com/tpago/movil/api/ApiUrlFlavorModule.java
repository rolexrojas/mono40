package com.tpago.movil.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

/**
 * @author hecvasro
 */
@Module
public final class ApiUrlFlavorModule {
  @Provides
  @Singleton
  HttpUrl provideBaseUrl() {
    return HttpUrl.parse("https://tpagonet.gcs-systems.com/api/neo/");
  }
}
