package com.tpago.movil.api;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.net.HttpCode;
import com.tpago.movil.net.HttpResult;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import rx.functions.Func0;

/**
 * @author hecvasro
 */
final class FakeApiBridge implements ApiBridge {
  @Override
  public Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(
    final PhoneNumber phoneNumber) {
    return Single.defer(new Callable<SingleSource<? extends HttpResult<ApiData<PhoneNumber.State>>>>() {
      @Override
      public SingleSource<? extends HttpResult<ApiData<PhoneNumber.State>>> call() throws Exception {
        final List<Digit> dL = Digits.getDigits(phoneNumber);
        final int v = dL.get(dL.size() - 1).getValue();
        final PhoneNumber.State s;
        if (v == 0) {
          s = PhoneNumber.State.REGISTERED;
        } else if (v % 2 == 0) {
          s = PhoneNumber.State.AFFILIATED;
        } else {
          s = PhoneNumber.State.NONE;
        }
        return Single.just(HttpResult.create(HttpCode.OK, ApiData.create(s)));
      }
    })
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signUp(
    final PhoneNumber phoneNumber,
    final Email email,
    final String password,
    final Pin pin) {
    return Single.defer(new Func0<SingleSource<? extends HttpResult<ApiData<String>>>>() {
      @Override
      public SingleSource<? extends HttpResult<ApiData<String>>> call() {
        final String p = pin.getValue();
        final int v = Integer.parseInt(Character.toString(p.charAt(p.length() - 1)));
        final HttpCode c;
        final ApiData<String> d;
        if (v % 2 == 0) {
          c = HttpCode.OK;
          d = ApiData.create(UUID.randomUUID().toString());
        } else {
          c = HttpCode.BAD_REQUEST;
          d = ApiData.create(ApiError.create(ApiError.Code.INVALID_PIN, "Incorrect PIN"));
        }
        return Single.just(HttpResult.create(c, d));
      }
    })
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Single<HttpResult<ApiData<String>>> signIn(
    final PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce) {
    return Single.defer(new Callable<SingleSource<? extends HttpResult<ApiData<String>>>>() {
      @Override
      public SingleSource<? extends HttpResult<ApiData<String>>> call() throws Exception {
        final List<Digit> dL = Digits.getDigits(phoneNumber);
        final int v = dL.get(dL.size() - 1).getValue();
        final HttpCode c;
        final ApiData<String> d;
        if (v % 2 == 0) {
          c = HttpCode.OK;
          d = ApiData.create(UUID.randomUUID().toString());
        } else {
          c = HttpCode.BAD_REQUEST;
          d = ApiData.create(ApiError.create(
            ApiError.Code.ALREADY_ASSOCIATED_DEVICE,
            "Already associated device"));
        }
        return Single.just(HttpResult.create(c, d));
      }
    })
      .delay(1L, TimeUnit.SECONDS);
  }
}
