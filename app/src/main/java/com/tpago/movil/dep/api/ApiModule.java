package com.tpago.movil.dep.api;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.dep.DeviceManager;
import com.tpago.movil.util.StringHelper;

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
@Deprecated
@Module(includes = ApiUrlFlavorModule.class)
public class ApiModule {

  @Provides
  @Singleton
  ApiService provideApiService(Retrofit retrofit) {
    if (StringHelper.isNullOrEmpty(BuildConfig.API_URL)) {
      return MockApiService.create();
    } else {
      return new RetrofitApiService(retrofit);
    }
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
    if (StringHelper.isNullOrEmpty(BuildConfig.API_URL)) {
      return MockDApiBridge.create();
    } else {
      return new DRetrofitApiBridge(deviceManager, retrofit);
    }
  }
}
