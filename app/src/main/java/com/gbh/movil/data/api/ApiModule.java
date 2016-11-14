package com.gbh.movil.data.api;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.DecoratedApiBridge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  private static final String HEADER_AUTHORIZATION = "Authorization";

  @Provides
  @Singleton
  ApiBridge provideApiBridge() {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Bank.class, new BankJsonDeserializer())
      .create();
    final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .baseUrl(BuildConfig.API_URL);
    final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
      .connectTimeout(BuildConfig.TIME_OUT_CONNECT, TimeUnit.SECONDS)
      .readTimeout(BuildConfig.TIME_OUT_READ, TimeUnit.SECONDS)
      .writeTimeout(BuildConfig.TIME_OUT_WRITE, TimeUnit.SECONDS)
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          final Request.Builder requestBuilder = chain.request().newBuilder();
          requestBuilder.addHeader(HEADER_AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDo4MjkzNDUxMjI3OjAxMjM0NTY3ODkwMTIzNCIsImV4cCI6MTQ4MTc0MzA4N30.TNo8_Kvweck-NBiuHIdEVvwUvTodGgiyWKgGUHEwVUI");
          return chain.proceed(requestBuilder.build());
        }
      });
    if (BuildConfig.DEBUG) {
      final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      okHttpClientBuilder.addInterceptor(interceptor);
    }
    final OkHttpClient okHttpClient = okHttpClientBuilder.build();
    retrofitBuilder.client(okHttpClient);
    final Retrofit retrofit = retrofitBuilder.build();
    return new DecoratedApiBridge(new RetrofitApiBridge(retrofit.create(ApiService.class)));
  }
}
