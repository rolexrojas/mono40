package com.tpago.movil.dep.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.Presenter;

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

  PhoneNumberTransactionCreationPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull ProductManager productManager,
    @NonNull TransactionManager transactionManager,
    @NonNull Recipient recipient) {
    this.schedulerProvider = schedulerProvider;
    this.productManager = productManager;
    this.transactionManager = transactionManager;
    this.recipient = recipient;
  }

  void start() {
    assertScreen();
    paymentOptionSubscription = productManager.getAllPaymentOptions()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<List<Product>>() {
        @Override
        public void call(List<Product> paymentOptions) {
          screen.setPaymentOptions(paymentOptions);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all payment options");
          // TODO: Let the user know that loading all the payment options failed.
        }
      });
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(paymentSubscription);
    RxUtils.unsubscribe(paymentOptionSubscription);
  }

  void setPaymentOption(@NonNull Product paymentOption) {
    this.assertScreen();
    this.paymentOption = paymentOption;
    this.screen.setPaymentOptionCurrency(this.paymentOption.getCurrency());
  }

  void onTransferButtonClicked() {
    if (!(recipient instanceof NonAffiliatedPhoneNumberRecipient)
      || ((NonAffiliatedPhoneNumberRecipient) recipient).canBeTransferTo()) {
      screen.requestPin();
    } else {
      screen.requestBankAndAccountNumber();
    }
  }

  void transferTo(@NonNull BigDecimal value, @NonNull String pin) {
    assertScreen();
    paymentSubscription = transactionManager.transferTo(paymentOption, recipient, value, pin)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          final boolean flag;
          final String message;
          if (result.isSuccessful()) {
            flag = true;
            message = result.getData();
          } else {
            flag = false;
            message = result.getError().getDescription();
          }
          screen.setPaymentResult(flag, message);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Paying a recipient");
          screen.setPaymentResult(false, null);
        }
      });
  }
}
