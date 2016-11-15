package com.gbh.movil.data.api;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.DecoratedApiBridge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import ch.halarious.core.HalDeserializer;
import ch.halarious.core.HalResource;
import ch.halarious.core.HalSerializer;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  @Provides
  @Singleton
  ApiBridge provideApiBridge(OkHttpClient okHttpClient) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Bank.class, new BankJsonDeserializer())
      .registerTypeAdapter(HalResource.class, new HalSerializer())
      .registerTypeAdapter(HalResource.class, new HalDeserializer(AccountHalResource.class))
      .registerTypeAdapter(HalResource.class, new HalDeserializer(QueryHalResource.class))
      .registerTypeAdapter(HalResource.class, new HalDeserializer(InitialDataHalResource.class))
      .create();
    final Retrofit retrofit = new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .build();
    return new DecoratedApiBridge(new RetrofitApiBridge(retrofit.create(ApiService.class)));
  }
}
