package com.tpago.movil.dep.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

/**
 * @author hecvasro
 */
@Module
public class ApiUrlFlavorModule {
  @Provides
  @Singleton
  HttpUrl provideBaseUrl() {
    return HttpUrl.parse("http://172.19.1.103:8081/api/neo/");
  }
}
