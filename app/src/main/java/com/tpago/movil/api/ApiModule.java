package com.tpago.movil.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.app.DeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  @Provides
  @Singleton
  ApiBridge provideApiBridge(DeviceManager deviceManager, OkHttpClient httpClient) {
//    final Gson gson = new GsonBuilder()
//      .registerTypeAdapterFactory(ApiTypeAdapterFactory.create())
//      .create();
//    final Retrofit retrofit = new Retrofit.Builder()
//      .client(httpClient)
//      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//      .addConverterFactory(GsonConverterFactory.create(gson))
//      .baseUrl(BuildConfig.API_URL)
//      .build();
//    return new RetrofitApiBridge(deviceManager, retrofit);
    return new FakeApiBridge();
  }
}
