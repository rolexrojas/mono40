package com.gbh.movil.ui.main.payments.transactions.contacts;

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
class PhoneNumberTransactionCreationPresenter
  extends Presenter<PhoneNumberTransactionCreationScreen> {
  private final SchedulerProvider schedulerProvider;
  private final ProductManager productManager;

  private Subscription paymentOptionSubscription = Subscriptions.unsubscribed();

  PhoneNumberTransactionCreationPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull ProductManager productManager) {
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    paymentOptionSubscription = productManager.getAllPaymentOptions()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Set<Product>>() {
        @Override
        public void call(Set<Product> paymentOptions) {
          screen.setPaymentOptions(paymentOptions);
          // TODO: Let the user know that he is now able to choose a payment option.
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all payment options");
          // TODO: Let the user know that loading all the payment options failed.
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(paymentOptionSubscription);
  }
}
