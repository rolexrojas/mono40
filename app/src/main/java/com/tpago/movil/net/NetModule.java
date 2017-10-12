package com.tpago.movil.net;

import android.content.Context;

import com.tpago.movil.dep.io.Files;
import com.tpago.movil.session.AccessTokenManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author hecvasro
 */
@Module
public final class NetModule {

  private static Interceptor createLoggingInterceptor() {
    return new HttpLoggingInterceptor()
      .setLevel(HttpLoggingInterceptor.Level.BODY);
  }

  private static Interceptor createUserAgentInterceptor() {
    return (chain) -> {
      final Request request = chain.request()
        .newBuilder()
        .addHeader("User-Agent", System.getProperty("http.agent"))
        .build();

      return chain.proceed(request);
    };
  }

  @Provides
  @Singleton
  Cache provideCache(Context context) {
    return new Cache(Files.createInternalCacheDirectory(context), Integer.MAX_VALUE);
  }

  @Provides
  @Singleton
  OkHttpClient okHttpClient(Cache cache, AccessTokenManager accessTokenManager) {
    return new OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor.create(accessTokenManager))
      .addInterceptor(createLoggingInterceptor())
      .addInterceptor(createUserAgentInterceptor())
      .cache(cache)
      .build();
  }
}
