package com.tpago.movil.api;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tpago.movil.app.DeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module(includes = ApiUrlFlavorModule.class)
public class ApiFlavorModule {
  @Provides
  @Singleton
  ApiBridge provideApiBridge(
    DeviceManager deviceManager,
    Gson gson,
    HttpUrl baseUrl,
    OkHttpClient httpClient) {
    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(httpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();
    return new RetrofitApiBridge(deviceManager, retrofit);
  }
}
