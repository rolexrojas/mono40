package com.tpago.movil.dep.data.api;

import com.google.gson.Gson;
import com.tpago.movil.dep.DepQualifier;
import com.tpago.movil.dep.domain.api.ApiError;
import com.tpago.movil.dep.domain.api.DepApiBridge;

import java.lang.annotation.Annotation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
@Deprecated
public class ApiFlavorModule {
  @Provides
  @Singleton
  Retrofit provideRetrofit(
    @DepQualifier Gson gson,
    HttpUrl baseUrl,
    OkHttpClient httpClient) {
    return new Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(httpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build();
  }

  @Provides
  @Singleton
  DepApiBridge provideApiBridge(Retrofit retrofit) {
    return new RetrofitApiBridge(
      retrofit.create(ApiService.class),
      retrofit.<ApiError>responseBodyConverter(ApiError.class, new Annotation[0]));
  }
}
