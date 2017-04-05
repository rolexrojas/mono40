package com.tpago.movil.api;

import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;

import java.lang.annotation.Annotation;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
final class RetrofitApiService implements ApiService {
  private final Service service;
  private final Function<ResponseBody, FailureData<ApiCode>> errorMapperFunc;

  RetrofitApiService(Retrofit retrofit) {
    assertNotNull(retrofit, "retrofit == null");
    this.service = retrofit.create(Service.class);
    final Converter<ResponseBody, ApiError> apiErrorConverter = retrofit
      .responseBodyConverter(ApiError.class, new Annotation[0]);
    this.errorMapperFunc = new Function<ResponseBody, FailureData<ApiCode>>() {
      @Override
      public FailureData<ApiCode> apply(ResponseBody responseBody) throws Exception {
        final ApiError apiError = apiErrorConverter.convert(responseBody);
        return FailureData.create(apiError.embedded.code, apiError.embedded.description);
      }
    };
  }

  private <A, B> Function<Response<A>, Result<B, ApiCode>> mapperFunc(
    final Function<A, B> innerMapperFunc) {
    assertNotNull(innerMapperFunc, "innerMapperFunc == null");
    return new Function<Response<A>, Result<B, ApiCode>>() {
      @Override
      public Result<B, ApiCode> apply(Response<A> response) throws Exception {
        if (response.isSuccessful()) {
          return Result.create(innerMapperFunc.apply(response.body()));
        } else {
          return Result.create(errorMapperFunc.apply(response.errorBody()));
        }
      }
    };
  }

  @Override
  public Single<Result<Set<Bank>, ApiCode>> fetchBankSet() {
    return service.fetchBankSet()
      .map(mapperFunc(ApiBankSet.mapperFunc()));
  }

  private interface Service {
    @GET("banks") Single<Response<ApiBankSet>> fetchBankSet();
  }
}
