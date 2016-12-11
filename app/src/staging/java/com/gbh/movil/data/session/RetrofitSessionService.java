package com.gbh.movil.data.session;

import android.support.annotation.NonNull;

import com.gbh.movil.data.api.Api;
import com.gbh.movil.data.api.ApiRequestBody;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.session.SessionService;
import com.gbh.movil.misc.Mapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Singleton
class RetrofitSessionService implements SessionService {
  private final Service service;
  private final Mapper<Token, String> mapper;

  /**
   * TODO
   *
   * @param retrofit
   *   TODO
   */
  @Inject
  RetrofitSessionService(@NonNull Retrofit retrofit) {
    service = retrofit.create(Service.class);
    mapper = new Mapper<Token, String>() {
      @NonNull
      @Override
      public String map(@NonNull Token token) {
        return token.token;
      }
    };
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> signIn(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId) {
    final ApiRequestBody body = new ApiRequestBody.Builder()
      .putProperty(Api.Property.PHONE_NUMBER, phoneNumber)
      .putProperty(Api.Property.EMAIL, email)
      .putProperty(Api.Property.USERNAME, email)
      .putProperty(Api.Property.PASSWORD, password)
      .putProperty(Api.Property.DEVICE_ID, deviceId)
      .build();
    return service.signIn(body).map(Api.mapToApiResponse(mapper));
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> signUp(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId, @NonNull String pin) {
    final ApiRequestBody body = new ApiRequestBody.Builder()
      .putProperty(Api.Property.PHONE_NUMBER, phoneNumber)
      .putProperty(Api.Property.EMAIL, email)
      .putProperty(Api.Property.USERNAME, email)
      .putProperty(Api.Property.PASSWORD, password)
      .putProperty(Api.Property.DEVICE_ID, deviceId)
      .putProperty(Api.Property.PIN, pin)
      .build();
    return service.signUp(body).map(Api.mapToApiResponse(mapper));
  }

  /**
   * TODO
   */
  private class Token {
    String token;
  }

  /**
   * TODO
   */
  private interface Service {
    /**
     * TODO
     *
     * @param body
     *   TODO
     *
     * @return TODO
     */
    @POST("signin")
    Observable<Response<Token>> signIn(@Body ApiRequestBody body);

    /**
     * TODO
     *
     * @param body
     *   TODO
     *
     * @return TODO
     */
    @POST("signup")
    Observable<Response<Token>> signUp(@Body ApiRequestBody body);
  }
}
