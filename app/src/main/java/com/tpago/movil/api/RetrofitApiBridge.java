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

  private final ApiService apiService;
  private final Converter<ResponseBody, ApiErrorResponseBody> errorConverter;

  private <A, B> Function<Response<A>, HttpResult<ApiData<B>>> mapToHttpResult(
    final Function<A, B> mapperFunc) {
    return new Function<Response<A>, HttpResult<ApiData<B>>>() {
      @Override
      public HttpResult<ApiData<B>> apply(Response<A> response) throws Exception {
        final ApiData<B> data;
        if (response.isSuccessful()) {
          data = ApiData.create(mapperFunc.apply(response.body()));
        } else {
          data = ApiData.create(errorConverter.convert(response.errorBody()).getError());
        }
        return HttpResult.create(HttpCode.find(response.code()), data);
      }
    };
  }

  RetrofitApiBridge(DeviceManager deviceManager, Retrofit retrofit) {
    this.deviceManager = Preconditions.assertNotNull(deviceManager, "deviceManager == null");
    Preconditions.assertNotNull(retrofit, "retrofit == null");
    this.errorConverter = retrofit.responseBodyConverter(ApiErrorResponseBody.class, new Annotation[0]);
    this.apiService = retrofit.create(ApiService.class);
  }

  @Override
  public Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(
    PhoneNumber phoneNumber) {
    return apiService.validatePhoneNumber(phoneNumber.getValue())
      .map(mapToHttpResult(ValidatePhoneNumberResponseData.mapperFunc()));
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin) {
    return apiService.signUp(SignUpRequestBody.create(
      email.getValue(),
      deviceManager.getId(),
      phoneNumber.getValue(),
      password,
      pin.getValue()))
      .map(mapToHttpResult(AuthResponseBody.mapperFunc()));
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce) {
    final Single<Response<AuthResponseBody>> single;
    final String e = email.getValue();
    final String dId = deviceManager.getId();
    final String pn = phoneNumber.getValue();
    if (shouldForce) {
      single = apiService.associate(AssociateRequestBody.create(e, pn, dId, password));
    } else {
      single = apiService.signIn(SignInRequestBody.create(e, dId, pn, password));
    }
    return single.map(mapToHttpResult(AuthResponseBody.mapperFunc()));
  }
}
