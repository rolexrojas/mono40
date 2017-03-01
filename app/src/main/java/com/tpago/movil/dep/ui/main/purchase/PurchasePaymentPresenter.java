package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.pos.PosResult;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.Presenter;

import dagger.Lazy;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
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
  private final DepApiBridge apiBridge;
  private final SessionManager sessionManager;

  private Subscription subscription = Subscriptions.unsubscribed();

  PurchasePaymentPresenter(@NonNull StringHelper stringHelper, @NonNull Product paymentOption,
    @NonNull ProductManager productManager, @NonNull Lazy<PosBridge> posBridge,
    @NonNull DepApiBridge apiBridge, @NonNull SessionManager sessionManager) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
    this.productManager = productManager;
    this.posBridge = posBridge;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  void start() {
    assertScreen();
    screen.setMessage(stringHelper.bringDeviceCloserToTerminal());
    screen.setPaymentOption(paymentOption);
    subscription = Observable.just(paymentOption)
      .subscribeOn(Schedulers.io())
      .zipWith(productManager.getDefaultPaymentOption(), new Func2<Product, Product,
        Pair<Product, Product>>() {
        @Override
        public Pair<Product, Product> call(Product paymentOption, Product currentDefault) {
          return Pair.create(paymentOption, currentDefault);
        }
      })
      .flatMap(new Func1<Pair<Product, Product>, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(final Pair<Product, Product> pair) {
          final Product po = pair.first; // Payment option
          final Product cdpo = pair.second; // Current default payment option
          if (po.equals(cdpo)) {
            return posBridge.get().selectCard(po.getAlias())
              .subscribeOn(Schedulers.io())
              .map(new Func1<PosResult, Boolean>() {
                @Override
                public Boolean call(PosResult result) {
                  return result.isSuccessful();
                }
              });
          } else {
            return apiBridge.setDefaultPaymentOption(sessionManager.getSession().getAuthToken(), po)
              .subscribeOn(Schedulers.io())
              .flatMap(new Func1<ApiResult<Void>, Observable<PosResult>>() {
                @Override
                public Observable<PosResult> call(ApiResult<Void> result) {
                  // TODO: Propagate errors to the caller.
                  return posBridge.get().selectCard(po.getAlias())
                    .subscribeOn(Schedulers.io());
                }
              })
              .map(new Func1<PosResult, Boolean>() {
                @Override
                public Boolean call(PosResult result) {
                  return result.isSuccessful();
                }
              })
              .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(final Boolean flag) {
                  // TODO: This must be added to a persistent queue.
                  if (Utils.isNull(cdpo)) {
                    return Observable.just(flag);
                  } else {
                    return apiBridge.setDefaultPaymentOption(
                      sessionManager.getSession().getAuthToken(), cdpo)
                      .subscribeOn(Schedulers.io())
                      .map(new Func1<ApiResult<Void>, Boolean>() {
                        @Override
                        public Boolean call(ApiResult<Void> result) {
                          return flag;
                        }
                      });
                  }
                }
              });
          }
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(final Boolean result) {
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
