package com.gbh.movil.data.session;

import android.support.annotation.NonNull;

import com.gbh.movil.data.api.Api;
import com.gbh.movil.data.api.ApiRequestBody;
import com.gbh.movil.domain.api.ApiError;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.session.SessionService;
import com.gbh.movil.misc.Mapper;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author hecvasro
 */
@Singleton
class RetrofitSessionService implements SessionService {
  private final Service service;
  private final Converter<ResponseBody, ApiError> errorConverter;
  private final Mapper<Token, String> mapper;

  @Inject
  RetrofitSessionService(@NonNull Retrofit retrofit) {
    service = retrofit.create(Service.class);
    errorConverter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
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
  public Observable<ApiResult<String>> signIn(
    @NonNull String phoneNumber,
    @NonNull String email,
    @NonNull String password,
    @NonNull String deviceId,
    boolean force) {
    final Observable<Response<Token>> observable;
    final ApiRequestBody.Builder builder = new ApiRequestBody.Builder()
      .putProperty(Api.Property.PHONE_NUMBER, phoneNumber)
      .putProperty(Api.Property.EMAIL, email)
      .putProperty(Api.Property.USERNAME, email)
      .putProperty(Api.Property.PASSWORD, password);
    if (force) {
      builder.putProperty(Api.Property.NEW_DEVICE_ID, deviceId);
      observable = service.associate(builder.build());
    } else {
      builder.putProperty(Api.Property.DEVICE_ID, deviceId);
      observable = service.signIn(builder.build());
    }
    return observable
      .compose(Api.transformToApiResponse(mapper, errorConverter));
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
    return service.signUp(body)
      .compose(Api.transformToApiResponse(mapper, errorConverter));
  }

  private class Token {
    String token;
  }

  private interface Service {
    @POST("signin")
    Observable<Response<Token>> signIn(@Body ApiRequestBody body);

    @POST("signup")
    Observable<Response<Token>> signUp(@Body ApiRequestBody body);

    @POST("associate")
    Observable<Response<Token>> associate(@Body ApiRequestBody body);
  }
}
