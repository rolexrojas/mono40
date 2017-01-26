package com.tpago.movil.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.misc.Utils;
import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.TransactionManager;
import com.tpago.movil.misc.rx.RxUtils;
import com.tpago.movil.ui.Presenter;

import java.math.BigDecimal;
import java.util.List;

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
  private final TransactionManager transactionManager;
  private final Recipient recipient;

  private Product paymentOption;

  private Subscription paymentOptionSubscription = Subscriptions.unsubscribed();
  private Subscription paymentSubscription = Subscriptions.unsubscribed();

  PhoneNumberTransactionCreationPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull ProductManager productManager, @NonNull TransactionManager transactionManager,
    @NonNull Recipient recipient) {
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
    this.transactionManager = transactionManager;
    this.recipient = recipient;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    paymentOptionSubscription = productManager.getAllPaymentOptions()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<List<Product>>() {
        @Override
        public void call(List<Product> paymentOptions) {
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
    RxUtils.unsubscribe(paymentSubscription);
    RxUtils.unsubscribe(paymentOptionSubscription);
  }

  /**
   * TODO
   *
   * @param paymentOption
   *   TODO
   */
  void setPaymentOption(@NonNull Product paymentOption) {
    this.assertScreen();
    this.paymentOption = paymentOption;
    this.screen.setPaymentOptionCurrency(this.paymentOption.getCurrency());
    this.screen.clearAmount();
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   * @param pin
   *   TODO
   */
  void transferTo(@NonNull BigDecimal value, @NonNull String pin) {
    assertScreen();
    if (Utils.isNotNull(paymentOption)) {
      paymentSubscription = transactionManager.transferTo(paymentOption, recipient, value, pin)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean succeeded) {
            screen.setPaymentResult(succeeded);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Paying a recipient");
            // TODO: Let the user know that paying a recipient failed.
            screen.setPaymentResult(false);
          }
        });
    } else {
      // TODO: Let the user know that he must select a payment option.
    }
  }
}
