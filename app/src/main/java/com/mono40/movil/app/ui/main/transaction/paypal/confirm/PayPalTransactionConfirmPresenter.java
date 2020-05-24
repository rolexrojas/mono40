package com.mono40.movil.app.ui.main.transaction.paypal.confirm;

import com.mono40.movil.Code;
import com.mono40.movil.Currency;
import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.d.data.Formatter;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.paypal.PayPalTransactionData;
import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.Money;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.TaxUtil;
import com.mono40.movil.util.TransactionType;
import com.mono40.movil.util.digit.Digit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final class PayPalTransactionConfirmPresenter
        extends Presenter<PayPalTransactionConfirmPresentation> {

    static Builder builder() {
        return new Builder();
    }

    private final PayPalAccount recipient;

    private final ProductManager productManager;
    private final Api api;

    private final StringMapper stringMapper;
    private final AlertManager alertManager;
    private final TakeoverLoader takeoverLoader;

    private final Money.Creator amount = Money.creator();

    private Product paymentMethod = null;
    private PayPalTransactionData transactionData = null;

    private CompositeDisposable disposables;

    private PayPalTransactionConfirmPresenter(Builder builder) {
        super(builder.presentation);

        this.recipient = builder.recipient;

        this.productManager = builder.productManager;
        this.api = builder.api;

        this.stringMapper = builder.stringMapper;
        this.alertManager = builder.alertManager;
        this.takeoverLoader = builder.takeoverLoader;
    }

    private void updateAmount() {
        this.presentation.setAmount(this.amount.toString());
    }

    final void onNumPadDigitPressed(@Digit int digit) {
        this.amount.addDigit(digit);
        this.updateAmount();
    }

    final void onNumPadDotPressed() {
        this.amount.switchToFractional();
        this.updateAmount();
    }

    final void onNumPadDeletePressed() {
        this.amount.removeLastDigit();
        this.updateAmount();
    }

    final void onPaymentMethodChanged(Product paymentMethod) {
        this.paymentMethod = ObjectHelper.checkNotNull(paymentMethod, "paymentMethod");
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);

        this.alertManager.showAlertForGenericFailure();
    }

    private void handleTransactionData(PayPalTransactionData data) {
        this.transactionData = data;

        final String currency = Currency.USD.value();
        final String paymentMethodCurrency = this.paymentMethod.getCurrency();

        final String amount = Money.format(currency, this.amount.create());
        final String alias = this.recipient.alias();
        final String fee = Money.format(currency, data.fee());
        final String rate = Money.format(paymentMethodCurrency, data.rate());
        final String total = Money.format(paymentMethodCurrency, data.total());


        final String text = TaxUtil.getConfirmPinTransactionMessage(TransactionType.PAYPAL_RECHARGE,
                this.amount.create().doubleValue(), paymentMethod, alias, currency, Formatter.amount(currency, data.fee()), stringMapper,
                data.fee().doubleValue(), data.rate().doubleValue(), data.total().doubleValue(), 0.0);
        this.presentation.requestPin(text);
    }

    final void onSubmitPressed() {
        final Disposable disposable = this.api.fetchPayPalTransactionData(
                this.recipient,
                this.paymentMethod,
                this.amount.create()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(this::handleTransactionData, this::handleError);
        this.disposables.add(disposable);
    }

    private void handleConfirmationResult(Result<TransactionSummary> result) {
        if (result.isSuccessful()) {
            this.presentation.finish(result.successData());
        } else {
            final FailureData failureData = result.failureData();
            this.alertManager.builder()
                    .message(failureData.description())
                    .show();
        }
    }

    final void onPin(Code pin) {
        final Disposable disposable = this.api.confirmPayPalTransaction(
                this.recipient,
                this.paymentMethod,
                this.amount.create(),
                this.transactionData,
                pin
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(this::handleConfirmationResult, this::handleError);
        this.disposables.add(disposable);
    }

    @Override
    public void onPresentationResumed() {
        this.disposables = new CompositeDisposable();

        // TODO: Set PayPal's logo.

        this.presentation.setAccountAlias(this.recipient.alias());
        this.presentation.setAccountCurrency(Currency.USD.value());

        final List<Product> paymentMethods = this.productManager.getPaymentOptionList();
        if (ObjectHelper.isNull(this.paymentMethod) || !paymentMethods.contains(this.paymentMethod)) {
            this.paymentMethod = paymentMethods.get(0);
        }
        this.presentation.setPaymentMethods(paymentMethods);

        this.updateAmount();
    }

    @Override
    public void onPresentationPaused() {
        if (!this.disposables.isDisposed()) {
            this.disposables.dispose();
        }
    }

    static final class Builder {

        private PayPalAccount recipient;

        private ProductManager productManager;
        private Api api;

        private StringMapper stringMapper;
        private AlertManager alertManager;
        private TakeoverLoader takeoverLoader;

        private PayPalTransactionConfirmPresentation presentation;

        private Builder() {
        }

        final Builder recipient(PayPalAccount recipient) {
            this.recipient = ObjectHelper.checkNotNull(recipient, "recipient");
            return this;
        }

        final Builder productManager(ProductManager productManager) {
            this.productManager = ObjectHelper.checkNotNull(productManager, "productManager");
            return this;
        }

        final Builder api(Api api) {
            this.api = ObjectHelper.checkNotNull(api, "api");
            return this;
        }

        final Builder stringMapper(StringMapper stringMapper) {
            this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
            return this;
        }

        final Builder alertManager(AlertManager alertManager) {
            this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
            return this;
        }

        final Builder takeoverLoader(TakeoverLoader takeoverLoader) {
            this.takeoverLoader = ObjectHelper.checkNotNull(takeoverLoader, "takeoverLoader");
            return this;
        }

        final Builder presentation(PayPalTransactionConfirmPresentation presentation) {
            this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
            return this;
        }

        final PayPalTransactionConfirmPresenter build() {
            BuilderChecker.create()
                    .addPropertyNameIfMissing("recipient", ObjectHelper.isNull(this.recipient))
                    .addPropertyNameIfMissing("productManager", ObjectHelper.isNull(this.productManager))
                    .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
                    .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
                    .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
                    .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
                    .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
                    .checkNoMissingProperties();
            return new PayPalTransactionConfirmPresenter(this);
        }
    }
}
