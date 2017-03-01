package com.tpago.movil.api;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.app.DeviceManager;
import com.tpago.movil.net.HttpCode;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.util.Preconditions;

import java.lang.annotation.Annotation;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author hecvasro
 */
final class RetrofitApiBridge implements ApiBridge {
  private final DeviceManager deviceManager;

  private final ApiService service;
  private final Converter<ResponseBody, ApiErrorResponseData> converter;

  private <A, B> Function<Response<A>, HttpResult<ApiData<B>>> mapToHttpResult(
    final Function<A, B> mapperFunc) {
    return new Function<Response<A>, HttpResult<ApiData<B>>>() {
      @Override
      public HttpResult<ApiData<B>> apply(Response<A> response) throws Exception {
        final ApiData<B> data;
        if (response.isSuccessful()) {
          data = ApiData.create(mapperFunc.apply(response.body()));
        } else {
          data = ApiData.create(converter.convert(response.errorBody()).getError());
        }
        return HttpResult.create(HttpCode.find(response.code()), data);
      }
    };
  }

  RetrofitApiBridge(DeviceManager deviceManager, Retrofit retrofit) {
    this.deviceManager = Preconditions.checkNotNull(deviceManager, "deviceManager == null");
    Preconditions.checkNotNull(retrofit, "retrofit == null");
    this.service = retrofit.create(ApiService.class);
    this.converter = retrofit.responseBodyConverter(ApiErrorResponseData.class, new Annotation[0]);
  }

  @Override
  public Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(
    PhoneNumber phoneNumber) {
    return service.validatePhoneNumber(phoneNumber.getValue())
      .map(mapToHttpResult(ValidatePhoneNumberResponseData.mapperFunc()));
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin) {
    return service.signUp(SignUpBody.create(
      deviceManager.getId(),
      phoneNumber.getValue(),
      email.getValue(),
      password,
      pin.getValue()))
      .map(mapToHttpResult(AuthResponseData.mapperFunc()));
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce) {
    return Single.error(new UnsupportedOperationException("Not implemented"));
  }
}
