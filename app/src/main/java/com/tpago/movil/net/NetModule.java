package com.tpago.movil.net;

import android.content.Context;

import com.tpago.movil.io.FileHelper;
import com.tpago.movil.session.AccessTokenInterceptor;

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
    return new Cache(FileHelper.createIntCacheDir(context), Integer.MAX_VALUE);
  }

  @Provides
  @Singleton
  OkHttpClient okHttpClient(Cache cache, AccessTokenInterceptor accessTokenInterceptor) {
    return new OkHttpClient.Builder()
      .addInterceptor(accessTokenInterceptor)
      .addInterceptor(createLoggingInterceptor())
      .addInterceptor(createUserAgentInterceptor())
      .cache(cache)
      .build();
  }
}
