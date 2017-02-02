package com.tpago.movil.data.pos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.pos.PosCode;
import com.tpago.movil.domain.pos.PosResult;
import com.tpago.movil.domain.text.TextHelper;
import com.tpago.movil.misc.Utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class CubePosBridge implements PosBridge {
  private final CubeSdkImpl cubeSdk;
  private final SharedPreferences sharedPreferences;

  CubePosBridge(Context context) {
    cubeSdk = new CubeSdkImpl(context);
    sharedPreferences = context
      .getSharedPreferences(PosBridge.class.getCanonicalName(), Context.MODE_PRIVATE);
  }

  private static <T> void handleSuccessResult(
    String method,
    Subscriber<? super PosResult<T>> subscriber,
    T data) {
    Timber.d("methodName: %1$s, data: %2$s", method, data);
    subscriber.onNext(new PosResult<>(PosCode.OK, data));
    subscriber.onCompleted();
  }

  private static <T> void handleErrorResult(
    String method,
    Subscriber<? super PosResult<T>> subscriber,
    CubeError error) {
    final int code = Integer.parseInt(error.getErrorCode().trim().replaceAll("[\\D]", ""));
    Timber.d(
      "methodName: %1$s, error: {code: %2$d, message: %3$s, details: %4$s}",
      method,
      code,
      error.getErrorMessage(),
      error.getErrorDetails());
    subscriber.onNext(new PosResult<T>(PosCode.fromValue(code), null));
    subscriber.onCompleted();
  }

  private static <T> void handleErrorResult(
    String method,
    Subscriber<? super PosResult<T>> subscriber,
    PosCode code) {
    final CubeError error = new CubeError();
    error.setErrorCode(Integer.toString(code.getValue()));
    handleErrorResult(method, subscriber, error);
  }

  @NonNull
  private Observable<PosResult<String>> getAllPan(final String alias) {
    return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
      @Override
      public void call(final Subscriber<? super PosResult<String>> subscriber) {
        final String methodName = "getAllPan";
        Utils.checkIfMainThread(methodName);
        Timber.d("%1$s(%2$s)", methodName, alias);
        cubeSdk.ListCard(new CubeSdkCallback<ListCards, CubeError>() {
          @Override
          public void success(ListCards listCards) {
            String altPan = null;
            for (Card card : listCards.getCards()) {
              if (card.getAlias().equals(alias)) {
                altPan = card.getAltpan();
                break;
              }
            }
            if (TextHelper.isEmpty(altPan)) {
              handleErrorResult(methodName, subscriber, PosCode.UNREGISTERED_PRODUCT);
            } else {
              handleSuccessResult(methodName, subscriber, altPan);
            }
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(methodName, subscriber, error);
          }
        });
      }
    });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> addCard(
    final String phoneNumber,
    final String pin,
    final String alias) {
    return getAllPan(alias)
      .flatMap(new Func1<PosResult<String>, Observable<PosResult<String>>>() {
        @Override
        public Observable<PosResult<String>> call(final PosResult<String> result) {
          final boolean flag = result.isSuccessful();
          if (flag) {
            return Observable.just(result);
          } else {
            return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
              @Override
              public void call(final Subscriber<? super PosResult<String>> subscriber) {
                final String methodName = "addCard";
                Timber.d("%1$s(%2$s,%3$s,%4$s)", methodName, phoneNumber, pin, alias);
                Utils.checkIfMainThread(methodName);
                final AddCardParams params = new AddCardParams();
                params.setMsisdn(phoneNumber);
                params.setOtp(pin);
                params.setAlias(alias);
                cubeSdk.AddCard(params, new CubeSdkCallback<String, CubeError>() {
                  @Override
                  public void success(String message) {
                    sharedPreferences.edit()
                      .putBoolean(alias, true)
                      .apply();
                    handleSuccessResult(methodName, subscriber, message);
                  }

                  @Override
                  public void failure(CubeError error) {
                    handleErrorResult(methodName, subscriber, error);
                  }
                });
              }
            });
          }
        }
      });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> selectCard(@NonNull final String alias) {
    return getAllPan(alias)
      .flatMap(new Func1<PosResult<String>, Observable<PosResult<String>>>() {
        @Override
        public Observable<PosResult<String>> call(final PosResult<String> result) {
          if (result.isSuccessful()) {
            return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
              @Override
              public void call(final Subscriber<? super PosResult<String>> subscriber) {
                final String methodName = "selectCard";
                Utils.checkIfMainThread(methodName);
                Timber.d("%1$s(%2$s)", methodName, alias);
                final SelectCardParams params = new SelectCardParams();
                params.setAlias(alias);
                params.setAltpan(result.getData());
                cubeSdk.SelectCard(params, new CubeSdkCallback<PaymentInfo, CubeError>() {
                  @Override
                  public void success(PaymentInfo paymentInfo) {
                    Timber.d("value = %1$s", paymentInfo.getValue());
                    Timber.d("date = %1$s", paymentInfo.getDate());
                    Timber.d("reference = %1$s", paymentInfo.getReference());
                    Timber.d("time = %1$s", paymentInfo.getTime());
                    Timber.d("crypto = %1$s", paymentInfo.getCrypto());
                    Timber.d("atc = %1$s", paymentInfo.getAtc());
                    handleSuccessResult(methodName, subscriber, paymentInfo.getAtc());
                  }

                  @Override
                  public void failure(CubeError error) {
                    handleErrorResult(methodName, subscriber, error);
                  }
                });
              }
            });
          } else {
            return Observable.just(result);
          }
        }
      });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> removeCard(@NonNull final String alias) {
    return getAllPan(alias)
      .flatMap(new Func1<PosResult<String>, Observable<PosResult<String>>>() {
        @Override
        public Observable<PosResult<String>> call(final PosResult<String> result) {
          if (result.isSuccessful()) {
            return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
              @Override
              public void call(final Subscriber<? super PosResult<String>> subscriber) {
                final String methodName = "removeCard";
                Utils.checkIfMainThread(methodName);
                Timber.d("%1$s(%2$s)", methodName, alias);
                final SelectCardParams params = new SelectCardParams();
                params.setAlias(alias);
                params.setAltpan(result.getData());
                cubeSdk.DeleteCard(params, new CubeSdkCallback<String, CubeError>() {
                  @Override
                  public void success(String message) {
                    sharedPreferences.edit()
                      .remove(alias)
                      .apply();
                    handleSuccessResult(methodName, subscriber, message);
                  }

                  @Override
                  public void failure(CubeError error) {
                    handleErrorResult(methodName, subscriber, error);
                  }
                });
              }
            });
          } else {
            return Observable.just(result);
          }
        }
      });
  }

  @Override
  public Observable<PosResult<String>> reset(@NonNull final String phoneNumber) {
    return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
      @Override
      public void call(final Subscriber<? super PosResult<String>> subscriber) {
        final String methodName = "reset";
        Utils.checkIfMainThread(methodName);
        Timber.d("%1$s(%2$s)", methodName, phoneNumber);
        cubeSdk.Unregister(phoneNumber, new CubeSdkCallback<String, CubeError>() {
          @Override
          public void success(String message) {
            handleSuccessResult(methodName, subscriber, message);
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(methodName, subscriber, error);
          }
        });
      }
    });
  }

  @Override
  public boolean isActive(String alias) {
    return sharedPreferences.getBoolean(alias, false);
  }
}

