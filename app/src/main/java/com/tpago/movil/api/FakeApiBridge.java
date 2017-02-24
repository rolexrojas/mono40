package com.tpago.movil.api;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.net.HttpCode;
import com.tpago.movil.net.HttpResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;

/**
 * @author hecvasro
 */
class FakeApiBridge implements ApiBridge {
  private static final long DELAY = 2000L;

  @Override
  public Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(
    final PhoneNumber phoneNumber) {
    return Single.defer(new Callable<SingleSource<? extends HttpResult<ApiData<PhoneNumber.State>>>>() {
      @Override
      public SingleSource<? extends HttpResult<ApiData<PhoneNumber.State>>> call() throws Exception {
        final List<Digit> digits = Digits.getDigits(phoneNumber);
        final Digit lastDigit = digits.get(digits.size() - 1);
        final PhoneNumber.State state;
        if (lastDigit.getValue() % 2 != 0) {
          state = PhoneNumber.State.NONE;
        } else if (lastDigit.equals(Digit.ZERO)) {
          state = PhoneNumber.State.AFFILIATED;
        } else {
          state = PhoneNumber.State.REGISTERED;
        }
        return Single.just(HttpResult.create(HttpCode.OK, ApiData.create(state)));
      }
    })
      .delay(DELAY, TimeUnit.MILLISECONDS);
  }
}
