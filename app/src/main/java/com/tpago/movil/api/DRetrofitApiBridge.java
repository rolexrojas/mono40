package com.tpago.movil.api;

import static com.tpago.movil.util.Preconditions.assertNotNull;

import android.support.v4.util.Pair;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.app.DeviceManager;
import com.tpago.movil.net.HttpCode;
import com.tpago.movil.net.HttpResult;

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
final class DRetrofitApiBridge implements DApiBridge {

  private final DeviceManager deviceManager;

  private final DApiService apiService;
  private final Converter<ResponseBody, ApiErrorResponseBody> apiErrorConverter;

  private <A, B> Function<Response<A>, HttpResult<DApiData<B>>> mapToHttpResult(final Function<A, B> mapperFunc) {
    return new Function<Response<A>, HttpResult<DApiData<B>>>() {
      @Override
      public HttpResult<DApiData<B>> apply(Response<A> response) throws Exception {
        final DApiData<B> data;
        if (response.isSuccessful()) {
          data = DApiData.create(mapperFunc.apply(response.body()));
        } else {
          data = DApiData.create(apiErrorConverter.convert(response.errorBody()).getError());
        }
        return HttpResult.create(HttpCode.find(response.code()), data);
      }
    };
  }

  private Function<Response<UserData>, HttpResult<DApiData<Pair<UserData, String>>>> mapToHttpResult() {
    return new Function<Response<UserData>, HttpResult<DApiData<Pair<UserData, String>>>>() {
      @Override
      public HttpResult<DApiData<Pair<UserData, String>>> apply(Response<UserData> response) throws Exception {
        final DApiData<Pair<UserData, String>> data;
        if (response.isSuccessful()) {
          data = DApiData.create(
            Pair.create(
              response.body(),
              response.headers()
                .get("token")
            )
          );
        } else {
          data = DApiData.create(apiErrorConverter.convert(response.errorBody()).getError());
        }

        return HttpResult.create(HttpCode.find(response.code()), data);
      }
    };
  }

  DRetrofitApiBridge(DeviceManager deviceManager, Retrofit retrofit) {
    this.deviceManager = assertNotNull(deviceManager, "deviceManager == null");

    assertNotNull(retrofit, "retrofit == null");
    this.apiService = retrofit.create(DApiService.class);
    this.apiErrorConverter = retrofit.responseBodyConverter(ApiErrorResponseBody.class, new Annotation[0]);
  }

  @Override
  public Single<HttpResult<DApiData<Integer>>> validatePhoneNumber(
    PhoneNumber phoneNumber
  ) {
    return this.apiService.validatePhoneNumber(phoneNumber.value())
      .map(mapToHttpResult(ValidatePhoneNumberResponseData.mapperFunc()));
  }

  @Override
  public Single<HttpResult<DApiData<Pair<UserData, String>>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin
  ) {
    final Single<Response<UserData>> single = this.apiService.signUp(
      SignUpRequestBody.create(
        email.value(),
        this.deviceManager.getId(),
        phoneNumber.value(),
        password,
        pin.getValue())
    );

    return single.map(mapToHttpResult());
  }

  @Override
  public Single<HttpResult<DApiData<Pair<UserData, String>>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce
  ) {
    final String e = email.value();
    final String pn = phoneNumber.value();
    final String dId = this.deviceManager.getId();

    final Single<Response<UserData>> single;
    if (shouldForce) {
      single = this.apiService.associate(AssociateRequestBody.create(e, pn, dId, password));
    } else {
      single = this.apiService.signIn(SignInRequestBody.create(e, dId, pn, password));
    }

    return single.map(mapToHttpResult());
  }
}
