package com.tpago.movil.dep.data.pos;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.pos.PosCode;
import com.tpago.movil.dep.domain.pos.PosResult;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class CubePosBridge implements PosBridge {
  private static final String FILE_NAME = PosBridge.class.getCanonicalName();

  private static final String KEY_COUNT = "count";

  private final CubeSdkImpl cubeSdk;
  private final SharedPreferences sharedPreferences;

  CubePosBridge(Context context) {
    cubeSdk = new CubeSdkImpl(context);
    sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
  }

  private static AddCardParams createAddCardParams(String phoneNumber, String pin, String alias) {
    final AddCardParams params = new AddCardParams();
    params.setMsisdn(phoneNumber);
    params.setOtp(pin);
    params.setAlias(alias);
    return params;
  }

  private static SelectCardParams createSelectCardParams(String alias, String altPan) {
    final SelectCardParams params = new SelectCardParams();
    params.setAlias(alias);
    params.setAltpan(altPan);
    return params;
  }

  private static String stringify(Object... parameters) {
    return TextUtils.join(",", parameters);
  }

  private static String stringify(String methodName, Object... parameters) {
    return String.format("%1$s(%2$s)", methodName, stringify(parameters));
  }

  private static String stringify(CubeError error) {
    return String.format(
      "{code:'%1$s',message:'%2$s',details:'%3$s'}",
      error.getErrorCode(),
      error.getErrorMessage(),
      error.getErrorDetails());
  }

  private static PosResult createResult(String message) {
    return new PosResult(PosCode.OK, message);
  }

  private static PosResult createResult(CubeError error) {
    return new PosResult(
      PosCode.fromValue(Integer.parseInt(error.getErrorCode().trim().replaceAll("[\\D]", ""))),
      stringify(error));
  }

  private static PosResult createResult(String methodName, Object... arguments) {
    final CubeError error = new CubeError();
    error.setErrorCode(Integer.toString(PosCode.UNEXPECTED.getValue()));
    error.setErrorMessage(methodName);
    error.setErrorDetails(stringify(arguments));
    return createResult(error);
  }

  private String getAltPan(String alias) {
    return sharedPreferences.getString(alias, null);
  }

  @Override
  public boolean isRegistered(String alias) {
    return sharedPreferences.contains(alias);
  }

  @Override
  public Observable<PosResult> addCard(
    final String phoneNumber,
    final String pin,
    final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = Observable.just(new PosResult(PosCode.OK, getAltPan(alias)));
    } else {
      observable = Observable.create(new Observable.OnSubscribe<PosResult>() {
        @Override
        public void call(final Subscriber<? super PosResult> subscriber) {
          cubeSdk.AddCard(
            createAddCardParams(phoneNumber, pin, alias),
            new CubeSdkCallback<String, CubeError>() {
              @Override
              public void success(String message) {
                cubeSdk.ListCard(new CubeSdkCallback<ListCards, CubeError>() {
                  @Override
                  public void success(ListCards data) {
                    String altPan = null;
                    for (Card card : data.getCards()) {
                      if (card.getAlias().equals(alias)) {
                        altPan = card.getAltpan();
                      }
                    }
                    final PosResult result;
                    if (altPan == null) {
                      result = createResult("addCard", phoneNumber, pin, alias);
                    } else {
                      sharedPreferences.edit()
                        .putString(alias, altPan)
                        .putInt(KEY_COUNT, sharedPreferences.getInt(KEY_COUNT, 0) + 1)
                        .apply();
                      result = createResult(altPan);
                    }
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                  }

                  @Override
                  public void failure(CubeError error) {
                    subscriber.onNext(createResult(error));
                    subscriber.onCompleted();
                  }
                });
              }

              @Override
              public void failure(CubeError error) {
                subscriber.onNext(createResult(error));
                subscriber.onCompleted();
              }
            });
        }
      });
    }
    return observable.doOnNext(new LogAction1("addCard", phoneNumber, pin, alias));
  }

  @Override
  public Observable<PosResult> selectCard(final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = Observable.create(new Observable.OnSubscribe<PosResult>() {
        @Override
        public void call(final Subscriber<? super PosResult> subscriber) {
          cubeSdk.SelectCard(
            createSelectCardParams(alias, getAltPan(alias)),
            new CubeSdkCallback<PaymentInfo, CubeError>() {
              @Override
              public void success(PaymentInfo data) {
                subscriber.onNext(createResult(stringify(
                  data.getValue(),
                  data.getDate(),
                  data.getReference(),
                  data.getTime(),
                  data.getCrypto(),
                  data.getAtc()
                )));
                subscriber.onCompleted();
              }

              @Override
              public void failure(CubeError error) {
                subscriber.onNext(createResult(error));
                subscriber.onCompleted();
              }
            }
          );
        }
      });
    } else {
      observable = Observable.just(createResult("selectCard", alias));
    }
    return observable.doOnNext(new LogAction1("selectCard", alias));
  }

  @Override
  public Observable<PosResult> removeCard(final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = Observable.create(new Observable.OnSubscribe<PosResult>() {
        @Override
        public void call(final Subscriber<? super PosResult> subscriber) {
          cubeSdk.DeleteCard(
            createSelectCardParams(alias, getAltPan(alias)),
            new CubeSdkCallback<String, CubeError>() {
              @Override
              public void success(String message) {
                sharedPreferences.edit()
                  .remove(alias)
                  .putInt(KEY_COUNT, sharedPreferences.getInt(KEY_COUNT, 0) - 1)
                  .apply();
                subscriber.onNext(createResult(message));
                subscriber.onCompleted();
              }

              @Override
              public void failure(CubeError error) {
                subscriber.onNext(createResult(error));
                subscriber.onCompleted();
              }
            });
        }
      });
    } else {
      observable = Observable.just(createResult(alias));
    }
    return observable.doOnNext(new LogAction1("removeCard", alias));
  }

  @Override
  public Observable<PosResult> reset(final String phoneNumber) {
    return Observable.create(new Observable.OnSubscribe<PosResult>() {
      @Override
      public void call(final Subscriber<? super PosResult> subscriber) {
        cubeSdk.Unregister(phoneNumber, new CubeSdkCallback<String, CubeError>() {
          @Override
          public void success(String message) {
            sharedPreferences.edit()
              .clear()
              .apply();
            subscriber.onNext(createResult(message));
            subscriber.onCompleted();
          }

          @Override
          public void failure(CubeError error) {
            subscriber.onNext(createResult(error));
            subscriber.onCompleted();
          }
        });
      }
    })
      .doOnNext(new LogAction1("Unregister", phoneNumber));
  }

  @Override
  public void unregisterSync(String phoneNumber) {
    cubeSdk.Unregister(phoneNumber, new CubeSdkCallback<String, CubeError>() {
      @Override
      public void success(String message) {
        sharedPreferences.edit()
          .clear()
          .apply();
        Timber.d("Unregister -> %1$s", createResult(message));
      }

      @Override
      public void failure(CubeError error) {
        Timber.d("Unregister -> %1$s", createResult(error));
      }
    });
  }

  private class LogAction1 implements Action1<PosResult> {
    private final String operation;

    LogAction1(String methodName, Object... parameters) {
      operation = stringify(methodName, parameters);
    }

    @Override
    public void call(PosResult result) {
      Timber.d("%1$s -> %2$s", operation, result);
    }
  }
}

