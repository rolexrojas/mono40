package com.tpago.movil.d.data.pos;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import rx.Single;
import rx.SingleSubscriber;

/**
 * @author hecvasro
 */
@Deprecated
final class CubePosBridge implements PosBridge {

  private static final String FILE_NAME = PosBridge.class.getCanonicalName();
  private static final String KEY_COUNT = "count";
  private static final String PLACEHOLDER = "placeholder";

  private static AddCardParams addCardParams(String phoneNumber, String pin, String identifier) {
    final AddCardParams params = new AddCardParams();
    params.setMsisdn(phoneNumber);
    params.setOtp(pin);
    params.setAlias(identifier);
    return params;
  }

  private static SelectCardParams selectCardParams(String identifier, String altPan) {
    final SelectCardParams params = new SelectCardParams();
    params.setAlias(identifier);
    params.setAltpan(altPan);
    return params;
  }

  private final SharedPreferences sharedPreferences;
  private final NfcAdapter nfcAdapter;
  private final Single<CubeSdkImpl> cubeSdk;

  CubePosBridge(Context context) {
    ObjectHelper.checkNotNull(context, "context");

    this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);

    if (ObjectHelper.isNull(this.nfcAdapter)) {
      this.sharedPreferences = null;
      this.cubeSdk = Single
        .error(new UnsupportedOperationException("ObjectHelper.isNull(this.nfcAdapter)"));
    } else {
      this.sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
      this.cubeSdk = Single.defer(() -> Single.just(new CubeSdkImpl(context)));
    }
  }

  private Single<String> getAltPan(final String identifier) {
    final String altPan = this.sharedPreferences.getString(identifier, PLACEHOLDER);
    if (!altPan.equals(PLACEHOLDER)) {
      return Single.just(altPan);
    }
    return this.cubeSdk.flatMap((instance) ->
      Single.<String>create((subscriber) ->
        instance.ListCard(ListCardCallback.create(identifier, subscriber))
      )
    )
      .doOnSuccess((value) ->
        this.sharedPreferences.edit()
          .putString(identifier, value)
          .apply()
      );
  }

  @Override
  public boolean isAvailable() {
    return ObjectHelper.isNotNull(this.nfcAdapter);
  }

  @Override
  public boolean isRegistered(Product product) {
    return this.isAvailable()
      && (
      this.sharedPreferences.contains(product.getSanitizedNumber())
        || this.sharedPreferences.contains(product.getNumberLast4Digits())
    );
  }

  @Override
  public PosResult addCard(final String phoneNumber, final String pin, final Product product) {
    final String numberSanitized = product.getSanitizedNumber();
    final String numberLast4Digits = product.getNumberLast4Digits();
    if (this.sharedPreferences.contains(numberLast4Digits)) {
      return PosResult.create(numberLast4Digits);
    }
    if (this.sharedPreferences.contains(numberSanitized)) {
      final String altPan = this.sharedPreferences.getString(numberSanitized, PLACEHOLDER);
      this.sharedPreferences.edit()
        .remove(numberSanitized)
        .putString(numberLast4Digits, altPan)
        .apply();
      return PosResult.create(numberLast4Digits);
    }
    final PosResult result = this.cubeSdk.<PosResult>flatMap((instance) ->
      Single.create((subscriber) ->
        instance.AddCard(
          addCardParams(phoneNumber, pin, numberLast4Digits),
          AddCardCallback.create(subscriber)
        )
      )
    )
      .toBlocking()
      .value();
    if (result.isSuccessful()) {
      this.sharedPreferences.edit()
        .putString(numberLast4Digits, PLACEHOLDER)
        .putInt(KEY_COUNT, this.sharedPreferences.getInt(KEY_COUNT, 0) + 1)
        .apply();
    }
    return result;
  }

  @Override
  public PosResult removeCard(final Product product) {
    final String numberSanitized = product.getSanitizedNumber();
    final String numberLast4Digits = product.getNumberLast4Digits();
    if (!this.sharedPreferences.contains(numberSanitized) || !this.sharedPreferences.contains(
      numberLast4Digits)) {
      return PosResult.create(numberLast4Digits);
    }
    String identifier;
    Single<String> altPan;
    if (this.sharedPreferences.contains(numberSanitized)) {
      identifier = numberSanitized;
      altPan = Single
        .defer(() -> Single.just(this.sharedPreferences.getString(numberSanitized, PLACEHOLDER)));
    } else {
      identifier = numberLast4Digits;
      altPan = Single
        .defer(() -> Single.just(this.sharedPreferences.getString(numberLast4Digits, PLACEHOLDER)));
    }
    return altPan.flatMap(this::getAltPan)
      .flatMap((value) ->
        this.cubeSdk.flatMap((instance) ->
          Single.<PosResult>create((subscriber) ->
            instance.DeleteCard(
              selectCardParams(value, value),
              DeleteCardCallback.create(subscriber)
            )
          )
        )
      )
      .doOnSuccess((result) -> {
        if (result.isSuccessful()) {
          this.sharedPreferences.edit()
            .remove(identifier)
            .putInt(KEY_COUNT, this.sharedPreferences.getInt(KEY_COUNT, 1) - 1)
            .apply();
        }
      })
      .toBlocking()
      .value();
  }

  @Override
  public void unregister(final PhoneNumber phoneNumber) throws Exception {
    if (this.sharedPreferences.getInt(KEY_COUNT, 0) == 0) {
      return;
    }
    this.cubeSdk.flatMap((instance) ->
      Single.<String>create((subscriber) ->
        instance.Unregister(phoneNumber.value(), UnregisterCallback.create(subscriber))
      )
    )
      .toCompletable()
      .doOnCompleted(() ->
        this.sharedPreferences.edit()
          .clear()
          .apply()
      )
      .await();
  }

  @Override
  public Single<PosResult> selectCard(final Product product) {
    final String numberSanitized = product.getSanitizedNumber();
    final String numberLast4Digits = product.getNumberLast4Digits();
    if (!this.sharedPreferences.contains(numberSanitized) || !this.sharedPreferences.contains(numberLast4Digits)) {
      return Single.error(new IllegalStateException("!this.sharedPreferences.contains(numberSanitized) || !this.sharedPreferences.contains(numberLast4Digits)"));
    }
    String identifier;
    Single<String> altPan;
    if (this.sharedPreferences.contains(numberSanitized)) {
      identifier = numberSanitized;
      altPan = Single
        .defer(() -> Single.just(this.sharedPreferences.getString(numberSanitized, PLACEHOLDER)));
    } else {
      identifier = numberLast4Digits;
      altPan = Single
        .defer(() -> Single.just(this.sharedPreferences.getString(numberLast4Digits, PLACEHOLDER)));
    }
    return altPan.flatMap(this::getAltPan)
      .flatMap((value) ->
        this.cubeSdk.flatMap((instance) ->
          Single.<PosResult>create((subscriber) ->
            instance.SelectCard(
              selectCardParams(identifier, value),
              SelectCardCallback.create(subscriber)
            )
          )
//          .doOnUnsubscribe(instance::CancelPayment)
        )
      );
  }

  private static final class ListCardCallback implements CubeSdkCallback<ListCards, CubeError> {

    private static ListCardCallback create(
      String identifier,
      SingleSubscriber<? super String> subscriber
    ) {
      return new ListCardCallback(identifier, subscriber);
    }

    private final String identifier;
    private final SingleSubscriber<? super String> subscriber;

    private ListCardCallback(String identifier, SingleSubscriber<? super String> subscriber) {
      this.identifier = StringHelper.checkIsNotNullNorEmpty(identifier, "identifier");
      this.subscriber = ObjectHelper.checkNotNull(subscriber, "subscriber");
    }

    private static NullPointerException createError() {
      return new NullPointerException("StringHelper.isNullOrEmpty(altPan)");
    }

    @Override
    public void success(ListCards data) {
      String alias;
      String altPan = null;
      for (Card card : data.getCards()) {
        alias = card.getAlias();
        if (alias.equals(this.identifier)) {
          altPan = card.getAltpan();
        }
      }
      if (StringHelper.isNullOrEmpty(altPan)) {
        this.subscriber.onError(createError());
      } else {
        this.subscriber.onSuccess(altPan);
      }
    }

    @Override
    public void failure(CubeError error) {
      this.subscriber.onError(createError());
    }
  }

  private static final class AddCardCallback implements CubeSdkCallback<String, CubeError> {

    private static AddCardCallback create(SingleSubscriber<? super PosResult> subscriber) {
      return new AddCardCallback(subscriber);
    }

    private final SingleSubscriber<? super PosResult> subscriber;

    private AddCardCallback(SingleSubscriber<? super PosResult> subscriber) {
      this.subscriber = ObjectHelper.checkNotNull(subscriber, "subscriber");
    }

    @Override
    public void success(String data) {
      this.subscriber.onSuccess(PosResult.create(data));
    }

    @Override
    public void failure(CubeError error) {
      this.subscriber.onSuccess(PosResult.create(error));
    }
  }

  private static final class DeleteCardCallback implements CubeSdkCallback<String, CubeError> {

    private static DeleteCardCallback create(SingleSubscriber<? super PosResult> subscriber) {
      return new DeleteCardCallback(subscriber);
    }

    private final SingleSubscriber<? super PosResult> subscriber;

    private DeleteCardCallback(SingleSubscriber<? super PosResult> subscriber) {
      this.subscriber = ObjectHelper.checkNotNull(subscriber, "subscriber");
    }

    @Override
    public void success(String message) {
      this.subscriber.onSuccess(PosResult.create(message));
    }

    @Override
    public void failure(CubeError error) {
      this.subscriber.onSuccess(PosResult.create(error));
    }
  }

  private static final class UnregisterCallback implements CubeSdkCallback<String, CubeError> {

    private static UnregisterCallback create(SingleSubscriber<? super String> subscriber) {
      return new UnregisterCallback(subscriber);
    }

    private final SingleSubscriber<? super String> subscriber;

    private UnregisterCallback(SingleSubscriber<? super String> subscriber) {
      this.subscriber = ObjectHelper.checkNotNull(subscriber, "subscriber");
    }

    @Override
    public void success(String message) {
      this.subscriber.onSuccess(message);
    }

    @Override
    public void failure(CubeError error) {
      final PosResult result = PosResult.create(error);
      this.subscriber.onError(new Exception(result.getData()));
    }
  }

  private static final class SelectCardCallback implements CubeSdkCallback<PaymentInfo, CubeError> {

    private static SelectCardCallback create(SingleSubscriber<? super PosResult> subscriber) {
      return new SelectCardCallback(subscriber);
    }

    private final SingleSubscriber<? super PosResult> subscriber;

    private SelectCardCallback(SingleSubscriber<? super PosResult> subscriber) {
      this.subscriber = ObjectHelper.checkNotNull(subscriber, "subscriber");
    }

    @Override
    public void success(PaymentInfo data) {
      this.subscriber.onSuccess(PosResult.create(data));
    }

    @Override
    public void failure(CubeError error) {
      this.subscriber.onSuccess(PosResult.create(error));
    }
  }
}

