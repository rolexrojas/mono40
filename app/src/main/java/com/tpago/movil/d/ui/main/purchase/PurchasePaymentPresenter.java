package com.tpago.movil.d.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.Presenter;

import dagger.Lazy;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class PurchasePaymentPresenter extends Presenter<PurchasePaymentScreen> {

  private final StringHelper stringHelper;
  private final Product paymentOption;
  private final ProductManager productManager;
  private final Lazy<PosBridge> posBridge;

  private Subscription subscription = Subscriptions.unsubscribed();

  PurchasePaymentPresenter(
    @NonNull StringHelper stringHelper,
    @NonNull Product paymentOption,
    @NonNull ProductManager productManager,
    @NonNull Lazy<PosBridge> posBridge
  ) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
    this.productManager = productManager;
    this.posBridge = posBridge;
  }

  private void doOnSubscribe() {
    this.screen.setMessage(this.stringHelper.bringDeviceCloserToTerminal());
    this.screen.setPaymentOption(this.paymentOption);
  }

  private Single<Boolean> flatMap(boolean result) {
    if (!result) {
      return Single.just(false);
    }
    return this.posBridge.get()
      .selectCard(this.paymentOption.getAltpanKey())
      .map(PosResult::isSuccessful);
  }

  private void handleResult(boolean result) {
    if (result) {
      this.screen.animateAndTerminate();
    } else {
      this.screen.setMessage(this.stringHelper.cannotProcessYourRequestAtTheMoment());
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Failed selecting a card");
    this.screen.setMessage(this.stringHelper.cannotProcessYourRequestAtTheMoment());
  }

  final void resume() {
    this.subscription = Single
      .defer(() -> Single.just(this.productManager.setDefaultPaymentOption(this.paymentOption)))
      .flatMap(this::flatMap)
      .subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(this::doOnSubscribe)
      .subscribe(this::handleResult, this::handleError);
  }

  final void pause() {
    RxUtils.unsubscribe(this.subscription);
  }
}
