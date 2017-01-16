package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.misc.Mapper;
import com.gbh.movil.misc.Utils;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Api {
  public static final String URL = BuildConfig.API_URL;

  /**
   * TODO
   */
  public static final class Header {
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_AGENT = "User-Agent";
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

  /**
   * TODO
   *
   * @param mapper
   *   TODO
   * @param <A>
   *   TODO
   * @param <B>
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static <A, B> Func1<Response<A>, ApiResult<B>> mapToApiResponse(
    @NonNull final Mapper<A, B> mapper) {
    return new Func1<Response<A>, ApiResult<B>>() {
      @Override
      public ApiResult<B> call(Response<A> response) {
        final ApiCode code = ApiCode.fromValue(response.code());
        final B data = Utils.isNotNull(response.body()) ? mapper.map(response.body()) : null;
        return new ApiResult<>(code, data);
      }
    };
  }
}
