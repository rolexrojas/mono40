package com.gbh.movil.ui.main.payments.commerce;

import android.support.annotation.NonNull;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.ui.Presenter;

import java.util.Set;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class CommercePaymentsPresenter extends Presenter<CommercePaymentsScreen> {
  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  private final SchedulerProvider schedulerProvider;
  private final ProductManager productManager;

  CommercePaymentsPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull ProductManager productManager) {
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    subscription = productManager.getAllPaymentOptions()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .doOnNext(new Action1<Set<Product>>() {
        @Override
        public void call(Set<Product> products) {
          screen.clearItemList();
        }
      })
      .compose(RxUtils.<Product>fromCollection())
      .subscribe(new Action1<Product>() {
        @Override
        public void call(Product product) {
          screen.addItemToList(product);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all the payment options");
          // TODO: Let the user know that loading all the payment options failed.
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
