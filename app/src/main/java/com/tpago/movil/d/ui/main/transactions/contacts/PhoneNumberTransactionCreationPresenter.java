package com.tpago.movil.d.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class PhoneNumberTransactionCreationPresenter
  extends Presenter<PhoneNumberTransactionCreationScreen> {
  private final ProductManager productManager;
  private final Recipient recipient;

  private final NetworkService networkService;
  private final DepApiBridge depApiBridge;
  private final String authToken;
  private final StringHelper stringHelper;

  private Product paymentOption;

  private Disposable paymentSubscription = Disposables.disposed();

  PhoneNumberTransactionCreationPresenter(
    @NonNull ProductManager productManager,
    @NonNull Recipient recipient,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    String authToken,
    StringHelper stringHelper) {
    this.productManager = productManager;
    this.recipient = recipient;

    this.networkService = networkService;
    this.depApiBridge = depApiBridge;
    this.authToken = authToken;
    this.stringHelper = stringHelper;
  }

  void start() {
    assertScreen();
    screen.setPaymentOptions(productManager.getPaymentOptionList());
  }

  void stop() {
    assertScreen();
    Disposables.dispose(paymentSubscription);
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

  final void transferTo(final BigDecimal value, final String pin) {
    assertScreen();
    paymentSubscription = Single.defer(new Callable<SingleSource<Result<String, ErrorCode>>>() {
      @Override
      public SingleSource<Result<String, ErrorCode>> call() throws Exception {
        final Result<String, ErrorCode> result;
        if (networkService.checkIfAvailable()) {
          final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(authToken, pin);
          if (pinValidationResult.isSuccessful()) {
            if (pinValidationResult.getData()) {
              final ApiResult<String> transactionResult = depApiBridge.transferTo(
                authToken,
                paymentOption,
                recipient,
                value,
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
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Consumer<Result<String,ErrorCode>>() {
          @Override
          public void accept(Result<String, ErrorCode> result) {
            if (result.isSuccessful()) {
              screen.setPaymentResult(true, result.getSuccessData());
            } else {
              screen.setPaymentResult(false, null);
              final FailureData<ErrorCode> failureData = result.getFailureData();
              switch (failureData.getCode()) {
                case INCORRECT_PIN:
                  screen.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                  break;
                case UNAVAILABLE_NETWORK:
                  screen.showUnavailableNetworkError();
                  break;
                default:
                  screen.showGenericErrorDialog(failureData.getDescription());
                  break;
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) {
            Timber.e(throwable);
            screen.setPaymentResult(false, null);
            screen.showGenericErrorDialog();
          }
        });
  }
}
