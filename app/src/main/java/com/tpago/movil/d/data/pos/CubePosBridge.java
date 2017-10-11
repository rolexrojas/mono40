package com.tpago.movil.d.data.pos;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
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
import com.tpago.movil.util.ObjectHelper;

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
@Deprecated
class CubePosBridge implements PosBridge {

  private static final String FILE_NAME = PosBridge.class.getCanonicalName();
  private static final String KEY_COUNT = "count";
  private static final String PLACEHOLDER = "placeholder";

  private static AddCardParams createAddCardParams(
    String phoneNumber,
    String pin,
    String identifier
  ) {
    final AddCardParams params = new AddCardParams();
    params.setMsisdn(phoneNumber);
    params.setOtp(pin);
    params.setAlias(identifier);
    return params;
  }

  private static SelectCardParams createSelectCardParams(String identifier, String altPan) {
    final SelectCardParams params = new SelectCardParams();
    params.setAlias(identifier);
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
      error.getErrorDetails()
    );
  }

  private static PosResult createResult(String message) {
    return new PosResult(PosCode.OK, message);
  }

  private static PosResult createResult(CubeError error) {
    return new PosResult(
      PosCode.fromValue(Integer.parseInt(error.getErrorCode()
        .trim()
        .replaceAll("[\\D]", ""))),
      stringify(error)
    );
  }

  private static PosResult createResult(String methodName, Object... arguments) {
    final CubeError error = new CubeError();
    error.setErrorCode(Integer.toString(PosCode.UNEXPECTED.getValue()));
    error.setErrorMessage(methodName);
    error.setErrorDetails(stringify(arguments));
    return createResult(error);
  }

  private final Context context;
  private final SharedPreferences sharedPreferences;
  private final NfcAdapter nfcAdapter;

  private CubeSdkImpl cubeSdk;

  CubePosBridge(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
    this.sharedPreferences = this.context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    this.nfcAdapter = NfcAdapter.getDefaultAdapter(this.context);
  }

  private void assertUsable() {
    if (!checkIfUsable()) {
      throw new UnsupportedOperationException("checkIfUsable() == false");
    }
  }

  private CubeSdkImpl getCubeSdk() {
    assertUsable();
    if (ObjectHelper.isNull(cubeSdk)) {
      cubeSdk = new CubeSdkImpl(context);
    }
    return cubeSdk;
  }

  private Observable<String> getAltPan(final String identifier) {
    final String altPan = sharedPreferences.getString(identifier, PLACEHOLDER);
    if (altPan.equals(PLACEHOLDER)) {
      return Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(final Subscriber<? super String> subscriber) {
          try {
            getCubeSdk().ListCard(new CubeSdkCallback<ListCards, CubeError>() {
              @Override
              public void success(ListCards data) {
                String n = null;
                for (Card c : data.getCards()) {
                  if (c.getAlias()
                    .equals(identifier)) {
                    n = c.getAltpan();
                  }
                }
                if (ObjectHelper.isNull(n)) {
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
              .putString(identifier, altPan)
              .apply();
          }
        });
    } else {
      return Observable.just(altPan);
    }
  }

  @Override
  public boolean checkIfUsable() {
    return ObjectHelper.isNotNull(nfcAdapter);
  }

  @Override
  public boolean isRegistered(String identifier) {
    return checkIfUsable() && sharedPreferences.contains(identifier);
  }

  @Override
  public PosResult addCard(
    final String phoneNumber,
    final String pin,
    final String identifier
  ) {
    final Observable<PosResult> observable;
    if (isRegistered(identifier)) {
      observable = Observable.just(createResult(identifier));
    } else {
      observable = Observable.create(new Observable.OnSubscribe<PosResult>() {
        @Override
        public void call(final Subscriber<? super PosResult> subscriber) {
          try {
            getCubeSdk().AddCard(
              createAddCardParams(phoneNumber, pin, identifier),
              new CubeSdkCallback<String, CubeError>() {
                @Override
                public void success(String message) {
                  sharedPreferences.edit()
                    .putString(identifier, PLACEHOLDER)
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
              }
            );
          } catch (Exception exception) {
            subscriber.onError(exception);
          }
        }
      });
    }
    return observable
      .doOnNext(new LogAction1("addCard", phoneNumber, pin, identifier))
      .toBlocking()
      .single();
  }

  @Override
  public Observable<PosResult> selectCard(final String identifier) {
    final Observable<PosResult> observable;
    if (isRegistered(identifier)) {
      observable = getAltPan(identifier)
        .flatMap(new Func1<String, Observable<PosResult>>() {
          @Override
          public Observable<PosResult> call(final String altPan) {
            return Observable.create(new Observable.OnSubscribe<PosResult>() {
              @Override
              public void call(final Subscriber<? super PosResult> subscriber) {
                try {
                  getCubeSdk().SelectCard(
                    createSelectCardParams(identifier, altPan),
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
                } catch (Exception exception) {
                  subscriber.onError(exception);
                }
              }
            });
          }
        });
    } else {
      observable = Observable.just(createResult("selectCard", identifier));
    }
    return observable
      .doOnNext(new LogAction1("selectCard", identifier))
      .doOnUnsubscribe(this.cubeSdk::CancelPayment);
  }

  @Override
  public PosResult removeCard(final String identifier) {
    final Observable<PosResult> observable;
    if (isRegistered(identifier)) {
      observable = getAltPan(identifier)
        .flatMap(new Func1<String, Observable<PosResult>>() {
          @Override
          public Observable<PosResult> call(final String altPan) {
            return Observable.create(new Observable.OnSubscribe<PosResult>() {
              @Override
              public void call(final Subscriber<? super PosResult> subscriber) {
                try {
                  getCubeSdk().DeleteCard(
                    createSelectCardParams(identifier, altPan),
                    new CubeSdkCallback<String, CubeError>() {
                      @Override
                      public void success(String message) {
                        sharedPreferences.edit()
                          .remove(identifier)
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
                    }
                  );
                } catch (Exception exception) {
                  subscriber.onError(exception);
                }
              }
            });
          }
        });
    } else {
      observable = Observable.just(createResult(identifier));
    }
    return observable
      .doOnNext(new LogAction1("removeCard", identifier))
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
            getCubeSdk().Unregister(phoneNumber, new CubeSdkCallback<String, CubeError>() {
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

