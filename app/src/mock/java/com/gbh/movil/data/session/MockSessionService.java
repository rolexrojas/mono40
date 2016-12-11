package com.gbh.movil.data.session;

import android.support.annotation.NonNull;

import com.gbh.movil.data.Delayer;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.session.SessionService;
import com.gbh.movil.domain.text.TextHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Singleton
class MockSessionService implements SessionService {
  private static final String AUTH_TOKEN = "123456789123456789123456789";

  @Inject
  MockSessionService() {
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> signIn(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId) {
    return Observable.just(ApiResult.create(ApiCode.OK, AUTH_TOKEN))
      .compose(Delayer.<ApiResult<String>>apply());
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> signUp(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId, @NonNull String pin) {
    final Observable<ApiResult<String>> observable;
    if (TextHelper.isEmpty(pin) || !TextHelper.isDigitsOnly(pin)) {
      observable = Observable.just(ApiResult.<String>create(ApiCode.BAD_REQUEST, null));
    } else if (Integer.parseInt(pin.substring(pin.length() - 1, pin.length())) % 2 != 0) {
      observable = Observable.just(ApiResult.<String>create(ApiCode.FORBIDDEN, null));
    } else {
      observable = Observable.just(ApiResult.create(ApiCode.OK, AUTH_TOKEN));
    }
    return observable
      .compose(Delayer.<ApiResult<String>>apply());
  }
}
