package com.mono40.movil.d.data.api;


import com.mono40.movil.d.domain.api.ApiCode;
import com.mono40.movil.d.domain.api.ApiError;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.misc.Mapper;

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
@Deprecated
public final class Api {
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
