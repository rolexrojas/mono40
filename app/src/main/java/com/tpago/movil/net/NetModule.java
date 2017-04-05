package com.tpago.movil.net;

import android.content.Context;
import android.net.ConnectivityManager;

import com.tpago.movil.BuildConfig;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author hecvasro
 */
@Module
public final class NetModule {
  @Provides
  @Singleton
  ConnectivityManager provideConnectivityManager(Context context) {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  NetworkService provideNetworkService(ConnectivityManager connectivityManager) {
    return new ConnectivityManagerNetworkService(connectivityManager);
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          final Request.Builder builder = chain.request()
            .newBuilder()
            .addHeader("User-Agent", System.getProperty("http.agent"));
          return chain.proceed(builder.build());
        }
      });
    if (BuildConfig.DEBUG) {
      final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(interceptor);
    }
    return builder.build();
  }
}
