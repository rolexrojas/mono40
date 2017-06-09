package com.tpago.movil.d.ui.main.transactions.products;

import com.tpago.movil.R;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.transactions.TransactionCreationComponent;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * @author hecvasro
 */

public class CreditCardTransactionCreationPresenter
  extends Presenter<CreditCardTransactionCreationPresenter.View> {
  private static BigDecimal amountToPay(CreditCardBillBalance b, CreditCardBillBalance.Option o) {
    switch (o) {
      case PERIOD: return b.periodAmount();
      case MINIMUM: return b.minimumAmount();
      case CURRENT: return b.currentAmount();
      default: return BigDecimal.ZERO;
    }
  }

  @Inject
  Recipient recipient;
  @Inject
  ProductManager productManager;
  @Inject
  DepApiBridge apiBridge;
  @Inject
  SessionManager sessionManager;
  @Inject
  NetworkService networkService;
  @Inject
  StringHelper stringHelper;

  private CreditCardBillBalance.Option option = CreditCardBillBalance.Option.CURRENT;

  private Disposable paymentSubscription = Disposables.disposed();

  CreditCardTransactionCreationPresenter(View view, TransactionCreationComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
  }

  final void onOptionSelectionChanged(CreditCardBillBalance.Option option) {
    this.option = option;
    this.view.setOptionChecked(this.option);
  }

  final void onPayButtonClicked() {
    final ProductRecipient r = (ProductRecipient) recipient;
    final CreditCardBillBalance b = (CreditCardBillBalance) r.getBalance();
    view.requestPin(recipient.getLabel(), Formatter.amount(amountToPay(b, option)));
  }

  final void onPinRequestFinished(final Product product, final String pin) {
    paymentSubscription = Single.defer(new Callable<SingleSource<Result<PaymentResult, ErrorCode>>>() {
      @Override
      public SingleSource<Result<PaymentResult, ErrorCode>> call() throws Exception {
        final Result<PaymentResult, ErrorCode> result;
        if (networkService.checkIfAvailable()) {
          final String authToken = sessionManager.getSession().getAuthToken();
          final ApiResult<Boolean> pinValidationResult = apiBridge.validatePin(authToken, pin);
          if (pinValidationResult.isSuccessful()) {
            if (pinValidationResult.getData()) {
              final ProductRecipient r = (ProductRecipient) recipient;
              final CreditCardBillBalance b = (CreditCardBillBalance) r.getBalance();
              final ApiResult<PaymentResult> transactionResult = apiBridge.payCreditCardBill(
                authToken,
                amountToPay(b, option),
                option,
                pin,
                product,
                r.getProduct())
                .toBlocking()
                .single();
              if (transactionResult.isSuccessful()) {
                result = Result.create(transactionResult.getData());
              } else {
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    transactionResult.getError().getDescription()));
              }
            } else {
              result = Result.create(FailureData.create(ErrorCode.INCORRECT_PIN));
            }
          } else {
            result = Result.create(
              FailureData.create(
                ErrorCode.UNEXPECTED,
                pinValidationResult.getError().getDescription()));
          }
        } else {
          result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
        }
        return Single.just(result);
      }
    })
      .subscribeOn(io.reactivex.schedulers.Schedulers.io())
      .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
      .subscribe(new Consumer<Result<PaymentResult, ErrorCode>>() {
        @Override
        public void accept(Result<PaymentResult, ErrorCode> result) throws Exception {
          boolean succeeded = false;
          String transactionId = null;
          if (result.isSuccessful()) {
            succeeded = true;
            transactionId = result.getSuccessData()
              .id();
          } else {
            final FailureData<ErrorCode> failureData = result.getFailureData();
            switch (failureData.getCode()) {
              case INCORRECT_PIN:
                view.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                break;
              case UNAVAILABLE_NETWORK:
                view.showUnavailableNetworkError();
                break;
              default:
                view.showGenericErrorDialog(failureData.getDescription());
                break;
            }
          }
          view.setPaymentResult(succeeded, transactionId);
        }
      }, new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
          Timber.e(throwable);
          view.setPaymentResult(false, null);
          view.showGenericErrorDialog();
        }
      });
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final ProductRecipient r = (ProductRecipient) recipient;
    view.setCurrencyValue(r.getProduct().getCurrency());
    String dueDate = null;
    BigDecimal totalValue = BigDecimal.ZERO;
    BigDecimal periodValue = BigDecimal.ZERO;
    BigDecimal minimumValue = BigDecimal.ZERO;
    final CreditCardBillBalance b = (CreditCardBillBalance) r.getBalance();
    if (Objects.checkIfNotNull(b)) {
      dueDate = b.dueDate();
      totalValue = b.currentAmount();
      periodValue = b.periodAmount();
      minimumValue = b.minimumAmount();
    }
    view.setOptionChecked(option);
    view.setDueDateValue(dueDate);
    view.setTotalValue(Formatter.amount(totalValue));
    view.setPeriodValue(Formatter.amount(periodValue));
    view.setMinimumValue(Formatter.amount(minimumValue));
    view.setPayButtonEnabled(totalValue.compareTo(BigDecimal.ZERO) > 0);

    final List<Product> paymentOptions = new ArrayList<>();
    for (Product paymentOption : productManager.getPaymentOptionList()) {
      if (!Product.checkIfCreditCard(paymentOption)) {
        paymentOptions.add(paymentOption);
      }
    }
    view.setPaymentOptions(paymentOptions);
  }

  @Override
  public void onViewStopped() {
    super.onViewStopped();
    Disposables.dispose(paymentSubscription);
  }

  public interface View extends Presenter.View {
    void setCurrencyValue(String value);

    void setDueDateValue(String value);

    void setTotalValue(String value);

    void setPeriodValue(String value);

    void setMinimumValue(String value);

    void setPaymentOptions(List<Product> paymentOptions);

    void setOptionChecked(CreditCardBillBalance.Option option);

    void setPayButtonEnabled(boolean enabled);

    void requestPin(String partnerName, String value);

    void setPaymentResult(boolean succeeded, String transactionId);

    void showGenericErrorDialog(String message);

    void showGenericErrorDialog();

    void showUnavailableNetworkError();
  }
}
