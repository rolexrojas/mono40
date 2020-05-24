package com.mono40.movil.d.data.api;

import com.google.gson.Gson;
import com.mono40.movil.BuildConfig;
import com.mono40.movil.d.domain.api.ApiError;
import com.mono40.movil.d.domain.api.DepApiBridge;

import java.lang.annotation.Annotation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public class ApiFlavorModule {

  @Provides
  @Singleton
  DepApiBridge provideApiBridge(Gson gson, OkHttpClient httpClient) {
    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .client(httpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build();
    return new RetrofitApiBridge(
      retrofit.create(ApiService.class),
      retrofit.responseBodyConverter(ApiError.class, new Annotation[0])
    );
  }
}
