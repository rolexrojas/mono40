package com.gbh.movil.data.api;


import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiError;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.misc.Mapper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Api {
  static final String URL = BuildConfig.API_URL;

  static final class Header {
    static final String AUTHORIZATION = "Authorization";
    static final String USER_AGENT = "User-Agent";
  }

  /**
   * TODO
   */
  public static final class Property {
    public static final String DEVICE_ID = "imei";
    public static final String EMAIL = "email";
    public static final String NEW_DEVICE_ID = "newImei";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "msisdn";
    public static final String PIN = "pin";
    public static final String USERNAME = "username";
  }

  public static <A, B> Observable.Transformer<Response<A>, ApiResult<B>> transformToApiResponse(
    final Mapper<A, B> dataMapper,
    final Converter<ResponseBody, ApiError> errorMapper) {
    return new Observable.Transformer<Response<A>, ApiResult<B>>() {
      @Override
      public Observable<ApiResult<B>> call(Observable<Response<A>> observable) {
        return observable
          .flatMap(new Func1<Response<A>, Observable<ApiResult<B>>>() {
            @Override
            public Observable<ApiResult<B>> call(Response<A> response) {
              try {
                B data = null;
                ApiError error = null;
                if (response.isSuccessful()) {
                  data = dataMapper.map(response.body());
                } else {
                  error = errorMapper.convert(response.errorBody());
                }
                return Observable
                  .just(new ApiResult<>(ApiCode.fromValue(response.code()), data, error));
              } catch (IOException exception) {
                return Observable.error(exception);
              }
            }
          });
      }
    };
  }
}
