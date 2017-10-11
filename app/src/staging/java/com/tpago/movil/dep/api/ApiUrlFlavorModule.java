package com.tpago.movil.dep.api;

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
    return HttpUrl.parse("https://tpagonet-dev.gcs-systems.com/api/neo/");
  }
}
