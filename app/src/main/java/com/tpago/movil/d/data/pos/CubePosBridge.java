package com.tpago.movil.d.data.pos;

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
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosCode;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.util.Objects;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class CubePosBridge implements PosBridge {
  private static final String FILE_NAME = PosBridge.class.getCanonicalName();

  private static final String KEY_COUNT = "count";

  private static final String PLACEHOLDER = "placeholder";

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

  private Observable<String> getAltPan(final String alias) {
    final String altPan = sharedPreferences.getString(alias, PLACEHOLDER);
    if (altPan.equals(PLACEHOLDER)) {
      return Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(final Subscriber<? super String> subscriber) {
          try {
            cubeSdk.ListCard(new CubeSdkCallback<ListCards, CubeError>() {
              @Override
              public void success(ListCards data) {
                String n = null;
                for (Card c : data.getCards()) {
                  if (c.getAlias().equals(alias)) {
                    n = c.getAltpan();
                  }
                }
                if (Objects.isNull(n)) {
                  subscriber.onError(new NullPointerException("altPan == null"));
                } else {
                  subscriber.onNext(n);
                  subscriber.onCompleted();
                }
              }

              @Override
              public void failure(CubeError error) {
                subscriber.onError(new NullPointerException("altPan == null"));
              }
            });
          } catch (Exception exception) {
            subscriber.onError(exception);
          }
        }
      })
        .doOnNext(new Action1<String>() {
          @Override
          public void call(String altPan) {
            sharedPreferences.edit()
              .putString(alias, altPan)
              .apply();
          }
        });
    } else {
      return Observable.just(altPan);
    }
  }

  @Override
  public boolean isRegistered(String alias) {
    return sharedPreferences.contains(alias);
  }

  @Override
  public PosResult addCard(
    final String phoneNumber,
    final String pin,
    final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = Observable.just(createResult(alias));
    } else {
      observable = Observable.create(new Observable.OnSubscribe<PosResult>() {
        @Override
        public void call(final Subscriber<? super PosResult> subscriber) {
          cubeSdk.AddCard(
            createAddCardParams(phoneNumber, pin, alias),
            new CubeSdkCallback<String, CubeError>() {
              @Override
              public void success(String message) {
                sharedPreferences.edit()
                  .putString(alias, PLACEHOLDER)
                  .putInt(KEY_COUNT, sharedPreferences.getInt(KEY_COUNT, 0) + 1)
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
    }
    return observable
      .doOnNext(new LogAction1("addCard", phoneNumber, pin, alias))
      .toBlocking()
      .single();
  }

  @Override
  public Observable<PosResult> selectCard(final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = getAltPan(alias)
        .flatMap(new Func1<String, Observable<PosResult>>() {
          @Override
          public Observable<PosResult> call(final String altPan) {
            return Observable.create(new Observable.OnSubscribe<PosResult>() {
              @Override
              public void call(final Subscriber<? super PosResult> subscriber) {
                cubeSdk.SelectCard(
                  createSelectCardParams(alias, altPan),
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
          }
        });
    } else {
      observable = Observable.just(createResult("selectCard", alias));
    }
    return observable.doOnNext(new LogAction1("selectCard", alias));
  }

  @Override
  public PosResult removeCard(final String alias) {
    final Observable<PosResult> observable;
    if (isRegistered(alias)) {
      observable = getAltPan(alias)
        .flatMap(new Func1<String, Observable<PosResult>>() {
          @Override
          public Observable<PosResult> call(final String altPan) {
            return Observable.create(new Observable.OnSubscribe<PosResult>() {
              @Override
              public void call(final Subscriber<? super PosResult> subscriber) {
                cubeSdk.DeleteCard(
                  createSelectCardParams(alias, altPan),
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
          }
        });
    } else {
      observable = Observable.just(createResult(alias));
    }
    return observable
      .doOnNext(new LogAction1("removeCard", alias))
      .toBlocking()
      .single();
  }

  @Override
  public Single<PosResult> unregister(final String phoneNumber) {
    if (sharedPreferences.getInt(KEY_COUNT, 0) > 0) {
      return Single.create(new Single.OnSubscribe<PosResult>() {
        @Override
        public void call(final SingleSubscriber<? super PosResult> subscriber) {
          try {
            cubeSdk.Unregister(phoneNumber, new CubeSdkCallback<String, CubeError>() {
              @Override
              public void success(String message) {
                sharedPreferences.edit()
                  .clear()
                  .apply();
                subscriber.onSuccess(createResult(message));
              }

              @Override
              public void failure(CubeError error) {
                subscriber.onSuccess(createResult(error));
              }
            });
          } catch (Exception exception) {
            subscriber.onError(exception);
          }
        }
      })
        .doOnSuccess(new LogAction1("unregister", phoneNumber));
    } else {
      return Single.just(createResult("No need to unregister"));
    }
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

