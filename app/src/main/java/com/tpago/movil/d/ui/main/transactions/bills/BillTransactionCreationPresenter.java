package com.tpago.movil.d.ui.main.transactions.bills;

import com.tpago.movil.R;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
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

public class BillTransactionCreationPresenter
  extends Presenter<BillTransactionCreationPresenter.View> {
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

  private BillRecipient.Option option = BillRecipient.Option.TOTAL;

  private Disposable paymentSubscription = Disposables.disposed();

  BillTransactionCreationPresenter(View view, TransactionCreationComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
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

  final void onPinRequestFinished(final Product product, final String pin) {
    paymentSubscription = Single.defer(new Callable<SingleSource<Result<String, ErrorCode>>>() {
      @Override
      public SingleSource<Result<String, ErrorCode>> call() throws Exception {
        final Result<String, ErrorCode> result;
        if (networkService.checkIfAvailable()) {
          final String authToken = sessionManager.getSession().getAuthToken();
          final ApiResult<Boolean> pinValidationResult = apiBridge.validatePin(authToken, pin);
          if (pinValidationResult.isSuccessful()) {
            if (pinValidationResult.getData()) {
              final ApiResult<String> transactionResult = apiBridge.payBill(
                authToken,
                (BillRecipient) recipient,
                product,
                option,
                pin)
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
              result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
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
      .subscribe(new Consumer<Result<String, ErrorCode>>() {
        @Override
        public void accept(Result<String, ErrorCode> result) throws Exception {
          boolean succeeded = false;
          String transactionId = null;
          if (result.isSuccessful()) {
            succeeded = true;
            transactionId = result.getSuccessData();
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
    final BillRecipient r = (BillRecipient) recipient;
    view.setCurrencyValue(r.getCurrency());
    String dueDate = null;
    BigDecimal totalValue = BigDecimal.ZERO;
    BigDecimal minimumValue = BigDecimal.ZERO;
    final BillBalance b = r.getBalance();
    if (Objects.checkIfNotNull(b)) {
      dueDate = b.getDate();
      totalValue = b.getTotal();
      minimumValue = b.getMinimum();
    }
    view.setOptionChecked(option);
    view.setDueDateValue(dueDate);
    view.setTotalValue(Formatter.amount(totalValue));
    view.setMinimumValue(Formatter.amount(minimumValue));
    view.setPayButtonEnabled(totalValue.compareTo(BigDecimal.ZERO) > 0);
    view.setPaymentOptions(productManager.getPaymentOptionList());
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

    void setMinimumValue(String value);

    void setPaymentOptions(List<Product> paymentOptions);

    void setOptionChecked(BillRecipient.Option option);

    void setPayButtonEnabled(boolean enabled);

    void requestPin(String partnerName, String value);

    void setPaymentResult(boolean succeeded, String transactionId);

    void showGenericErrorDialog(String message);

    void showGenericErrorDialog();

    void showUnavailableNetworkError();
  }
}
