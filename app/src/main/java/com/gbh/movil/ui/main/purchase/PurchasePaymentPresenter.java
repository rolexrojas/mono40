package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.pos.PosResult;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.Presenter;

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
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentPresenter extends Presenter<PurchasePaymentScreen> {
  private final StringHelper stringHelper;
  private final Product paymentOption;
  private final ProductManager productManager;
  private final Lazy<PosBridge> posBridge;
  private final ApiBridge apiBridge;
  private final SessionManager sessionManager;

  private Subscription subscription = Subscriptions.unsubscribed();

  PurchasePaymentPresenter(@NonNull StringHelper stringHelper, @NonNull Product paymentOption,
    @NonNull ProductManager productManager, @NonNull Lazy<PosBridge> posBridge,
    @NonNull ApiBridge apiBridge, @NonNull SessionManager sessionManager) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
    this.productManager = productManager;
    this.posBridge = posBridge;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    screen.setMessage(stringHelper.bringDeviceCloserToTerminal());
    screen.setPaymentOption(paymentOption);
    subscription = Observable.just(paymentOption)
      .zipWith(productManager.getDefaultPaymentOption(), new Func2<Product, Product,
        Pair<Product, Product>>() {
        @Override
        public Pair<Product, Product> call(Product paymentOption, Product currentDefault) {
          return Pair.create(paymentOption, currentDefault);
        }
      })
      .flatMap(new Func1<Pair<Product, Product>, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(Pair<Product, Product> pair) {
          final Product po = pair.first;
          final Product cdpo = pair.second;
          if (po.equals(cdpo)) {
            return posBridge.get().selectCard(po.getAlias())
              .map(new Func1<PosResult<String>, Boolean>() {
                @Override
                public Boolean call(PosResult<String> result) {
                  return result.isSuccessful();
                }
              });
          } else {
            return apiBridge.setDefaultPaymentOption(sessionManager.getSession().getAuthToken(), po)
              .flatMap(new Func1<ApiResult<Void>, Observable<PosResult<String>>>() {
                @Override
                public Observable<PosResult<String>> call(ApiResult<Void> result) {
                  // TODO: Propagate errors to the caller.
                  return posBridge.get().selectCard(po.getAlias());
                }
              })
              .map(new Func1<PosResult<String>, Boolean>() {
                @Override
                public Boolean call(PosResult<String> result) {
                  return result.isSuccessful();
                }
              })
              .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(final Boolean flag) {
                  // TODO: This must be added to a persistent queue.
                  return apiBridge.setDefaultPaymentOption(
                    sessionManager.getSession().getAuthToken(), cdpo)
                    .map(new Func1<ApiResult<Void>, Boolean>() {
                      @Override
                      public Boolean call(ApiResult<Void> result) {
                        return flag;
                      }
                    });
                }
              });
          }
        }
      })
      .subscribeOn(Schedulers.io())
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

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }
}
