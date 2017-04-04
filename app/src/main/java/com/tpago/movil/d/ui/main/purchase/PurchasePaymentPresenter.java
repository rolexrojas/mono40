package com.tpago.movil.d.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.Session;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.util.Preconditions;

import dagger.Lazy;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
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

  private final Session session;

  private Subscription subscription = Subscriptions.unsubscribed();

  PurchasePaymentPresenter(
    @NonNull StringHelper stringHelper,
    @NonNull Product paymentOption,
    @NonNull ProductManager productManager,
    @NonNull Lazy<PosBridge> posBridge,
    Session session) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
    this.productManager = productManager;
    this.posBridge = posBridge;

    this.session = Preconditions.checkNotNull(session, "session == null");
  }

  void start() {
    assertScreen();
    subscription = Observable.defer(new Func0<Observable<Boolean>>() {
      @Override
      public Observable<Boolean> call() {
        final boolean result = productManager
          .setTemporaryDefaultPaymentOption(session.getToken(), paymentOption);
        return Observable.just(result);
      }
    })
      .flatMap(new Func1<Boolean, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(Boolean result) {
          if (result) {
            return posBridge.get()
              .selectCard(paymentOption.getSanitizedNumber())
              .map(new Func1<PosResult, Boolean>() {
                @Override
                public Boolean call(PosResult posResult) {
                  return posResult.isSuccessful();
                }
              });
          } else {
            return Observable.just(false);
          }
        }
      })
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          productManager.clearTemporaryDefaultPaymentOption(session.getToken());
        }
      })
      .subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          screen.setMessage(stringHelper.bringDeviceCloserToTerminal());
          screen.setPaymentOption(paymentOption);
        }
      })
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean result) {
          if (result) {
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

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }
}
