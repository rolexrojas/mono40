package com.mono40.movil.d.ui.main.transaction.contacts;

import static com.mono40.movil.d.domain.Product.checkIfCreditCard;
import static com.mono40.movil.d.ui.main.transaction.TransactionCategory.PAY;
import static com.mono40.movil.d.ui.main.transaction.TransactionCategory.RECHARGE;
import static com.mono40.movil.d.ui.main.transaction.TransactionCategory.TRANSFER;

import androidx.annotation.NonNull;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.R;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.domain.MerchantRecipient;
import com.mono40.movil.dep.api.DCurrencies;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.AccountRecipient;
import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.mono40.movil.d.domain.PhoneNumberRecipient;
import com.mono40.movil.d.domain.UserRecipient;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.Presenter;
import com.mono40.movil.d.ui.main.transaction.TransactionCategory;
import com.mono40.movil.d.domain.ErrorCode;
import com.mono40.movil.d.domain.FailureData;
import com.mono40.movil.d.domain.Result;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.dep.reactivex.Disposables;
import com.mono40.movil.util.ObjectHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
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
    private final StringHelper stringHelper;

    private final TransactionCategory transactionCategory;

    private Product paymentOption;

    private Disposable paymentSubscription = Disposables.disposed();
    private Subscription rechargeSubscription = Subscriptions.unsubscribed();

    PhoneNumberTransactionCreationPresenter(
            @NonNull ProductManager productManager,
            @NonNull Recipient recipient,
            NetworkService networkService,
            DepApiBridge depApiBridge,
            StringHelper stringHelper,
            TransactionCategory transactionCategory
    ) {
        this.productManager = productManager;
        this.recipient = recipient;

        this.networkService = networkService;
        this.depApiBridge = depApiBridge;
        this.stringHelper = stringHelper;

        this.transactionCategory = transactionCategory;
    }

    final void start() {
        assertScreen();

        final List<Product> paymentOptionList = new ArrayList<>();
        for (Product product : this.productManager.getPaymentOptionList()) {
            if (this.transactionCategory == RECHARGE || !checkIfCreditCard(product)) {
                paymentOptionList.add(product);
            }
        }

        this.screen.setPaymentOptions(paymentOptionList);
    }

    final void stop() {
        assertScreen();
        Disposables.dispose(paymentSubscription);
        if (!this.rechargeSubscription.isUnsubscribed()) {
            this.rechargeSubscription.unsubscribe();
        }
    }

    void setPaymentOption(@NonNull Product paymentOption) {
        this.assertScreen();
        this.paymentOption = paymentOption;
        this.screen.setPaymentOptionCurrency(DCurrencies.map(this.paymentOption.getCurrency()));
    }

    final void onTransferButtonClicked() {
        if (this.recipient instanceof NonAffiliatedPhoneNumberRecipient) {
            final NonAffiliatedPhoneNumberRecipient r
                    = (NonAffiliatedPhoneNumberRecipient) this.recipient;
            if (r.canAcceptTransfers()) {
                this.screen.requestPin();
            } else {
                this.screen.requestBankAndAccountNumber();
            }
        } else if (this.recipient instanceof AccountRecipient) {
            final AccountRecipient r = (AccountRecipient) this.recipient;
            if (r.canAcceptTransfers()) {
                this.screen.requestPin();
            } else {
                this.screen.requestBankAndAccountNumber();
            }
        } else {
            this.screen.requestPin();
        }
    }

    final void onRechargeButtonClicked() {
        Partner carrier;
        if (this.recipient instanceof UserRecipient) {
            final UserRecipient r = (UserRecipient) this.recipient;
            carrier = r.getCarrier();
        } else if (this.recipient instanceof NonAffiliatedPhoneNumberRecipient) {
            final NonAffiliatedPhoneNumberRecipient r
                    = (NonAffiliatedPhoneNumberRecipient) this.recipient;
            carrier = r.getCarrier();
        } else {
            final PhoneNumberRecipient r = (PhoneNumberRecipient) this.recipient;
            carrier = r.getCarrier();
        }

        if (ObjectHelper.isNull(carrier)) {
            this.screen.requestCarrier();
        } else {
            this.screen.requestPin();
        }
    }

    final void transfer(final BigDecimal value, final String pin) {
        paymentSubscription = Single.defer(new Callable<SingleSource<Result<String, ErrorCode>>>() {
            @Override
            public SingleSource<Result<String, ErrorCode>> call() throws Exception {
                final Result<String, ErrorCode> result;
                if (networkService.checkIfAvailable()) {
                    final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(pin);
                    if (pinValidationResult.isSuccessful()) {
                        if (pinValidationResult.getData()) {
                          ApiResult<String> transactionResult = null;
                            if (recipient instanceof MerchantRecipient) {
                                transactionResult = depApiBridge
                                        .payToMerchant(paymentOption, recipient, value, pin)
                                        .toBlocking()
                                        .single();
                                ;
                            } else {
                                transactionResult = depApiBridge.transferTo(
                                        paymentOption,
                                        recipient,
                                        value,
                                        pin
                                )
                                        .toBlocking()
                                        .single();
                            }
                            if (transactionResult.isSuccessful()) {
                                result = Result.create(transactionResult.getData());
                            } else {
                                result = Result.create(
                                        FailureData.create(
                                                ErrorCode.UNEXPECTED,
                                                transactionResult.getError()
                                                        .getDescription()
                                        ));
                            }
                        } else {
                            result = Result.create(FailureData.create(ErrorCode.INCORRECT_PIN));
                        }
                    } else {
                        result = Result.create(
                                FailureData.create(
                                        ErrorCode.UNEXPECTED,
                                        pinValidationResult.getError()
                                                .getDescription()
                                ));
                    }
                } else {
                    result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
                }
                return Single.just(result);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result<String, ErrorCode>>() {
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

    final void recharge(final BigDecimal value, final String pin) {
        final Partner carrier;
        final PhoneNumber phoneNumber;
        if (this.recipient instanceof UserRecipient) {
            final UserRecipient r = (UserRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.phoneNumber();
        } else if (this.recipient instanceof NonAffiliatedPhoneNumberRecipient) {
            final NonAffiliatedPhoneNumberRecipient r
                    = (NonAffiliatedPhoneNumberRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.getPhoneNumber();
        } else {
            final PhoneNumberRecipient r = (PhoneNumberRecipient) this.recipient;

            carrier = r.getCarrier();
            phoneNumber = r.getPhoneNumber();
        }

        rechargeSubscription = depApiBridge.recharge(
                carrier,
                phoneNumber,
                paymentOption,
                value,
                pin
        )
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult<String>>() {
                    @Override
                    public void call(ApiResult<String> result) {
                        if (result.isSuccessful()) {
                            screen.setPaymentResult(true, result.getData());
                        } else {
                            screen.setPaymentResult(false, null);
                            screen.showGenericErrorDialog(
                                    result.getError()
                                            .getDescription()
                            );
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable);
                        screen.setPaymentResult(false, null);
                        screen.showGenericErrorDialog();
                    }
                });
    }

    final void transferTo(final BigDecimal value, final String pin) {
        if (this.transactionCategory == TRANSFER || this.transactionCategory == PAY) {
            this.transfer(value, pin);
        } else {
            this.recharge(value, pin);
        }
    }
}
