package com.gbh.movil.data.net;

import android.content.Context;
import android.net.ConnectivityManager;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.domain.SessionManager;

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

/**
 * @author hecvasro
 */
@Module
public class NetModule {
  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String HEADER_USER_AGENT = "User-Agent";

  @Provides
  @Singleton
  NetworkHelper provideNetworkHelper(Context context) {
    return new ConnectivityManagerNetworkHelper((ConnectivityManager) context.getSystemService(
      Context.CONNECTIVITY_SERVICE));
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(final SessionManager sessionManager) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .connectTimeout(BuildConfig.TIME_OUT_CONNECT, TimeUnit.SECONDS)
      .readTimeout(BuildConfig.TIME_OUT_READ, TimeUnit.SECONDS)
      .writeTimeout(BuildConfig.TIME_OUT_WRITE, TimeUnit.SECONDS)
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          final Request.Builder builder = chain.request().newBuilder()
            .addHeader(HEADER_USER_AGENT, System.getProperty("http.agent"));
          if (sessionManager.isActive() && Utils.isNotNull(sessionManager.getAuthToken())) {
            builder.addHeader(HEADER_AUTHORIZATION, sessionManager.getAuthToken());
          }
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
