package com.tpago.movil.data.api;

import com.google.gson.Gson;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.domain.auth.AltAuthMethodService;
import com.tpago.movil.domain.auth.AuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public class DataApiModule {

  @Provides
  @Singleton
  Api api(Gson gson, OkHttpClient okHttpClient) {
    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build();

    return retrofit.create(Api.class);
  }

  @Provides
  @Singleton
  AuthService authService(Api api) {
    return ApiAuthService.create(api);
  }

  @Provides
  @Singleton
  AltAuthMethodService altAuthMethodService(Api api) {
    throw new UnsupportedOperationException("not implemented");
  }
}
