package com.tpago.movil.d.ui.main.transactions.bills;

import com.tpago.movil.app.Presenter;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.transactions.TransactionCreationComponent;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */

public class BillTransactionCreationPresenter
  extends Presenter<BillTransactionCreationPresenter.View> {
  @Inject Recipient recipient;
  @Inject ProductManager productManager;
  @Inject DepApiBridge apiBridge;
  @Inject SessionManager sessionManager;

  private BillRecipient.Option option = BillRecipient.Option.TOTAL;

  private Subscription paymentOptionSubscription = Subscriptions.unsubscribed();
  private Subscription paymentSubscription = Subscriptions.unsubscribed();

  BillTransactionCreationPresenter(View view, TransactionCreationComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.checkNotNull(component, "component == null")
      .inject(this);
  }

  final void onOptionSelectionChanged(BillRecipient.Option option) {
    this.option = option;
    this.view.setOptionChecked(this.option);
  }

  final void onPayButtonClicked() {
    final BillRecipient r = (BillRecipient) recipient;
    final BillBalance b = r.getBalance();
    view.requestPin(
      recipient.getIdentifier(),
      Formatter.amount(
        r.getCurrency(),
        option.equals(BillRecipient.Option.TOTAL) ? b.getTotal() : b.getMinimum()));
  }

  final void onPinRequestFinished(Product product, String pin) {
    paymentSubscription = apiBridge.payBill(
      sessionManager.getSession().getAuthToken(),
      (BillRecipient) recipient,
      product,
      option,
      pin)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          if (result.isSuccessful()) {
            view.setPaymentResult(true, result.getData());
          } else {
            view.setPaymentResult(false, result.getError().getDescription());
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable);
          view.setPaymentResult(false, null);
        }
      });
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final BillRecipient r = (BillRecipient) recipient;
    view.setCurrencyValue(r.getCurrency());
    String dueDate = null;
    BigDecimal totalValue = BigDecimal.ZERO;
    BigDecimal minimumValue = BigDecimal.ZERO;
    final BillBalance b = r.getBalance();
    if (Objects.isNotNull(b)) {
      dueDate = b.getDate();
//      dueDate = new SimpleDateFormat("dd MMMM", new Locale("es", "DO"))
//        .format(b.getDate())
//        .toUpperCase();
      totalValue = b.getTotal();
      minimumValue = b.getMinimum();
    }
    view.setOptionChecked(option);
    view.setDueDateValue(dueDate);
    view.setTotalValue(Formatter.amount(totalValue));
    view.setMinimumValue(Formatter.amount(minimumValue));
    view.setPayButtonEnabled(totalValue.compareTo(BigDecimal.ZERO) > 0);
    paymentOptionSubscription = productManager.getAllPaymentOptions()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        new Action1<List<Product>>() {
          @Override
          public void call(List<Product> paymentOptions) {
            view.setPaymentOptions(paymentOptions);
          }
        },
        new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Loading all payment options");
          }
        });
  }

  @Override
  public void onViewStopped() {
    super.onViewStopped();
    if (!paymentSubscription.isUnsubscribed()) {
      paymentSubscription.unsubscribe();
    }
    if (!paymentOptionSubscription.isUnsubscribed()) {
      paymentOptionSubscription.unsubscribe();
    }
  }

  public interface View extends Presenter.View {
    void setCurrencyValue(String value);

    void setDueDateValue(String value);

    void setTotalValue(String value);

    void setMinimumValue(String value);

    void setPaymentOptions(List<Product> paymentOptions);

    void setOptionChecked(BillRecipient.Option option);

    void setPayButtonEnabled(boolean enabled);

    void requestPin(String partnerName, String value);

    void setPaymentResult(boolean succeeded, String message);
  }
}
