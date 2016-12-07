package com.gbh.movil.data.pos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.pos.PosCode;
import com.gbh.movil.domain.pos.PosResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class CubePosBridge implements PosBridge {
  private static final String DEFAULT_ALIAS = "alias";
  private static final String DEFAULT_PIN = "123456";

  private final Context context;
  private final CubeSdkImpl cubeSdk;
  private final ComponentName componentName;

  /**
   * TODO
   *
   * @param context
   *   TODO
   */
  CubePosBridge(@NonNull Context context) {
    this.context = context;
    this.cubeSdk = new CubeSdkImpl(this.context);
    this.componentName = new ComponentName("com.cube.sdk.hce", "TestHostApduService");
  }

  /**
   * TODO
   *
   * @param subscriber
   *   TODO
   * @param data
   *   TODO
   * @param <T>
   *   TODO
   */
  private static <T> void handleSuccessResult(@NonNull Subscriber<? super PosResult<T>> subscriber,
    @Nullable T data) {
    subscriber.onNext(new PosResult<>(PosCode.OK, data));
    subscriber.onCompleted();
  }

  /**
   * TODO
   *
   * @param subscriber
   *   TODo
   * @param <T>
   *   TODO
   */
  private static <T> void handleErrorResult(@NonNull Subscriber<? super PosResult<T>> subscriber,
    @NonNull CubeError error) {
    final int code = Integer.parseInt(error.getErrorCode().trim().replaceAll("[\\D]", ""));
    Timber.d("(%1$d) %2$s - %3$s", code, error.getErrorMessage(), error.getErrorDetails());
    subscriber.onNext(new PosResult<T>(PosCode.fromValue(code)));
    subscriber.onCompleted();
  }

  @Override
  public boolean isDefault() {
    return CardEmulation.getInstance(NfcAdapter.getDefaultAdapter(context))
      .isDefaultServiceForAid(componentName, CardEmulation.CATEGORY_PAYMENT);
  }

  @Override
  public Intent requestToMakeDefault() {
    final Intent intent = new Intent();
    intent.setAction(CardEmulation.ACTION_CHANGE_DEFAULT);
    intent.putExtra(CardEmulation.EXTRA_SERVICE_COMPONENT, componentName);
    intent.putExtra(CardEmulation.EXTRA_CATEGORY, CardEmulation.CATEGORY_PAYMENT);
    return intent;
  }

  @NonNull
  @Override
  public Observable<PosResult<List<String>>> getCards() {
    return Observable.create(new Observable.OnSubscribe<PosResult<List<String>>>() {
      @Override
      public void call(final Subscriber<? super PosResult<List<String>>> subscriber) {
        cubeSdk.ListCard(new CubeSdkCallback<ListCards, CubeError>() {
          @Override
          public void success(ListCards listCards) {
            final List<String> aliases = new ArrayList<>();
            for (Card card : listCards.getCards()) {
              aliases.add(card.getAlias());
            }
            handleSuccessResult(subscriber, aliases);
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(subscriber, error);
          }
        });
      }
    });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> addCard(@NonNull final PhoneNumber phoneNumber,
    @NonNull final String pin, @NonNull final String alias) {
    return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
      @Override
      public void call(final Subscriber<? super PosResult<String>> subscriber) {
        final AddCardParams params = new AddCardParams();
        params.setMsisdn(phoneNumber.toString());
        params.setOtp(DEFAULT_PIN);
        params.setAlias(DEFAULT_ALIAS);
        cubeSdk.AddCard(params, new CubeSdkCallback<String, CubeError>() {
          @Override
          public void success(String message) {
            handleSuccessResult(subscriber, message);
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(subscriber, error);
          }
        });
      }
    });
  }

  @NonNull
  @Override
  public Observable<PosResult<Void>> selectCard(@NonNull final String alias) {
    return Observable.create(new Observable.OnSubscribe<PosResult<Void>>() {
      @Override
      public void call(final Subscriber<? super PosResult<Void>> subscriber) {
        final SelectCardParams params = new SelectCardParams();
        params.setAlias(DEFAULT_ALIAS);
        cubeSdk.SelectCard(params, new CubeSdkCallback<PaymentInfo, CubeError>() {
          @Override
          public void success(PaymentInfo paymentInfo) {
            Timber.d("value = %1$s", paymentInfo.getValue());
            Timber.d("date = %1$s", paymentInfo.getDate());
            Timber.d("reference = %1$s", paymentInfo.getReference());
            Timber.d("time = %1$s", paymentInfo.getTime());
            Timber.d("crypto = %1$s", paymentInfo.getCrypto());
            Timber.d("atc = %1$s", paymentInfo.getAtc());
            handleSuccessResult(subscriber, null);
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(subscriber, error);
          }
        });
      }
    });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> removeCard(@NonNull final String alias) {
    return Observable.create(new Observable.OnSubscribe<PosResult<String>>() {
      @Override
      public void call(final Subscriber<? super PosResult<String>> subscriber) {
        final SelectCardParams params = new SelectCardParams();
        params.setAlias(DEFAULT_ALIAS);
        cubeSdk.DeleteCard(params, new CubeSdkCallback<String, CubeError>() {
          @Override
          public void success(String message) {
            handleSuccessResult(subscriber, message);
          }

          @Override
          public void failure(CubeError error) {
            handleErrorResult(subscriber, error);
          }
        });
      }
    });
  }
}
