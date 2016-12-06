package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.pos.PosResult;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.Presenter;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentPresenter extends Presenter<PurchasePaymentScreen> {
  private final StringHelper stringHelper;
  private final Product paymentOption;
  private final PosBridge posBridge;

  private Subscription subscription = Subscriptions.unsubscribed();

  PurchasePaymentPresenter(@NonNull StringHelper stringHelper, @NonNull Product paymentOption,
    @NonNull PosBridge posBridge) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
    this.posBridge = posBridge;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    screen.setMessage(stringHelper.bringDeviceCloserToTerminal());
    screen.setPaymentOption(paymentOption);
    subscription = posBridge.selectCard(paymentOption.getAlias())
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<PosResult<Void>>() {
        @Override
        public void call(PosResult<Void> result) {
          if (result.isSuccessful()) {
            screen.animateAndTerminate();
          } else {
            screen.setMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Failed selecting a card");
          screen.setMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }
}
