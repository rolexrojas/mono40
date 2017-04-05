package com.tpago.movil.api;

import com.google.gson.Gson;
import com.tpago.movil.app.DeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module(includes = ApiUrlFlavorModule.class)
public class ApiModule {
  @Provides
  @Singleton
  ApiService provideApiService(Retrofit retrofit) {
    return new RetrofitApiService(retrofit);
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(HttpUrl httpUrl, OkHttpClient httpClient, Gson gson) {
    return new Retrofit.Builder()
      .baseUrl(httpUrl)
      .client(httpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build();
  }

  // Deprecated provider methods.
  @Provides
  @Singleton
  DApiBridge provideApiBridge(DeviceManager deviceManager, Retrofit retrofit) {
    return new DRetrofitApiBridge(deviceManager, retrofit);
  }
}
