package com.tpago.movil.data.api;

import com.google.gson.Gson;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.api.Api;
import com.tpago.movil.util.FailureData;

import java.lang.annotation.Annotation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Module
public final class FlavorDataApiModule {

  @Provides
  @Singleton
  Api api(Gson gson, OkHttpClient okHttpClient) {
    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build();

    final RetrofitApi api = retrofit.create(RetrofitApi.class);
    final Converter<ResponseBody, FailureData> apiFailureDataConverter = retrofit
      .responseBodyConverter(FailureData.class, new Annotation[0]);
    final RetrofitApiFailureDataMapper retrofitApiFailureDataMapper
      = RetrofitApiFailureDataMapper.create(apiFailureDataConverter);
    final RetrofitApiResultMapper.Builder retrofitApiResultMapperBUilder = RetrofitApiResultMapper
      .builder(retrofitApiFailureDataMapper);

    return RetrofitApiImpl.create(api, retrofitApiResultMapperBUilder);
  }
}
