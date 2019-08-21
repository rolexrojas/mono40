package com.tpago.movil.d.ui.main.transaction.own;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.DNumPad;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.view.widget.PrefixableTextView;
import com.tpago.movil.dep.App;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.util.UiUtil;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

/**
 * @author Hector Vasquez
 */
public final class OwnTransferActivity
        extends AppCompatActivity
        implements PaymentMethodChooser.OnPaymentMethodChosenListener {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final String KEY_PRODUCT_FUNDING = "fundingProduct";
    private static final String KEY_PRODUCT_DESTINATION = "destinationProduct";

    @NonNull
    private static BigDecimal getFraction(@NonNull BigDecimal value) {
        return value.subtract(BigDecimal.valueOf(value.intValue()));
    }

    private static boolean isFraction(@NonNull BigDecimal value) {
        return value.compareTo(ZERO) > 0 && value.compareTo(ONE) < 0;
    }

    @NonNull
    private static String getFormattedValue(
            @NonNull BigDecimal value,
            @NonNull BigDecimal fractionOffset,
            boolean mustShowDot
    ) {
        String formattedValue = Formatter.amount(value);
        final BigDecimal fraction = getFraction(value);
        if (!isFraction(fraction)) {
            formattedValue = formattedValue.replace(".00", "");
            if (mustShowDot) {
                formattedValue += ".";
            }
        } else if (fraction.multiply(fractionOffset)
                .compareTo(TEN) < 0) {
            formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
        }
        return formattedValue;
    }

    public static Intent createLaunchIntent(Context context, Product originProduct) {
        final Intent intent = new Intent(context, OwnTransferActivity.class);
        intent.putExtra(KEY_PRODUCT_FUNDING, originProduct);
        return intent;
    }

    private BigDecimal fractionOffset = ONE;
    private BigDecimal value = ZERO;
    private Disposable disposable = Disposables.disposed();
    private Product destinationProduct;
    private Product fundingProduct;
    private boolean mustShowDot = false;

    private Unbinder unbinder;

    @BindView(R.id.action_transfer)
    Button transferActionButton;
    @BindView(R.id.payment_method_chooser)
    PaymentMethodChooser paymentMethodChooser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.transaction_creation_amount)
    PrefixableTextView amountTextView;
    @BindView(R.id.transaction_creation_num_pad)
    DNumPad DNumPad;

    @Inject
    DepApiBridge depApiBridge;
    @Inject
    NetworkService networkService;
    @Inject
    ProductManager productManager;
    @Inject
    StringHelper stringHelper;

    private Consumer<Integer> numPadDigitConsumer;
    private Action numPadDotAction;
    private Action numPadDeleteAction;

    private void updateAmountText() {
        this.amountTextView.setContent(
                getFormattedValue(
                        this.value,
                        this.fractionOffset,
                        this.mustShowDot
                )
        );
    }

    private void showTransferButtonAsEnabled() {
        UiUtil.setEnabled(this.transferActionButton, this.destinationProduct != null);
    }

    private void transfer(final String pin) {
        this.disposable = Single.defer(new Callable<SingleSource<Result<String, ErrorCode>>>() {
            @Override
            public SingleSource<Result<String, ErrorCode>> call() throws Exception {
                final Result<String, ErrorCode> result;
                if (networkService.checkIfAvailable()) {
                    final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(pin);
                    if (pinValidationResult.isSuccessful()) {
                        if (pinValidationResult.getData()) {
                            final ApiResult<String> transactionResult = depApiBridge.transferTo(
                                    fundingProduct,
                                    destinationProduct,
                                    value,
                                    pin
                            )
                                    .toBlocking()
                                    .single();
                            if (transactionResult.isSuccessful()) {
                                result = Result.create(transactionResult.getData());
                            } else {
                                result = Result.create(
                                        FailureData.create(
                                                ErrorCode.UNEXPECTED,
                                                transactionResult.getError()
                                                        .getDescription()
                                        )
                                );
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
                .subscribe((result) -> {
                    if (result.isSuccessful()) {
                        setTransferResult(true, result.getSuccessData());
                    } else {
                        setTransferResult(false, null);

                        final FailureData<ErrorCode> failureData = result.getFailureData();
                        switch (failureData.getCode()) {
                            case INCORRECT_PIN:
                                showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                                break;
                            case UNAVAILABLE_NETWORK:
                                showUnavailableNetworkError();
                                break;
                            default:
                                showGenericErrorDialog(failureData.getDescription());
                                break;
                        }
                    }
                }, (throwable) -> {
                    Timber.e(throwable);
                    setTransferResult(false, null);
                    showGenericErrorDialog();
                });
    }

    @OnClick(R.id.action_transfer)
    void onTransferButtonClicked() {
        if (this.value.compareTo(ZERO) > 0) {
            final int[] location = new int[2];
            this.transferActionButton.getLocationOnScreen(location);

            final int x = location[0] + (this.transferActionButton.getWidth() / 2);
            final int y = location[1];

            final String currency = DCurrencies.map(this.fundingProduct.getCurrency());


            final double taxPercentage = 0.15;
            double taxAmount = this.value.doubleValue() * (taxPercentage / 100);
            String taxAmountText = Formatter.amount("RD", new BigDecimal(taxAmount));

            final String description = this.getString(
                    R.string.format_transfer_to,
                    Formatter.amount(currency, this.value),
                    this.destinationProduct.getAlias(),
                    Formatter.amount(
                            currency,
                            this.fundingProduct.getBank()
                                    .calculateTransferCost(this.value)
                    )
            );
            PinConfirmationDialogFragment.show(
                    this.getSupportFragmentManager(),
                    description,
                    OwnTransferActivity.this::transfer,
                    x,
                    y
            );
        } else {
            // TODO: Let the user know that he must insert an amount greater than zero.
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this)
                .component()
                .inject(this);

        final Bundle bundle;
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = getIntent()
                    .getExtras();
        }
        this.fundingProduct = bundle.getParcelable(KEY_PRODUCT_FUNDING);
        this.destinationProduct = bundle.getParcelable(KEY_PRODUCT_DESTINATION);

        setContentView(R.layout.activity_transfer_own);

        this.unbinder = ButterKnife.bind(this);

        setSupportActionBar(this.toolbar);
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        this.toolbar.post(() -> {
            toolbar.setTitle("Transferir entre mis cuentas");
            toolbar.setSubtitle(
                    String.format(
                            "Desde %1$s %2$s",
                            fundingProduct.getBank()
                                    .name(),
                            fundingProduct.getNumberSanitized()
                    )
            );
        });

        this.paymentMethodChooser.setOnPaymentMethodChosenListener(this);
        final List<Product> paymentMethodList = new ArrayList<>();
        for (Product paymentMethod : this.productManager.getPaymentOptionList()) {
            if (Product.checkIfAccount(paymentMethod) && !paymentMethod.equals(this.fundingProduct)) {
                paymentMethodList.add(paymentMethod);
            }
        }
        this.paymentMethodChooser.setPaymentMethodList(paymentMethodList);

        if (!paymentMethodList.isEmpty()) {
            this.destinationProduct = paymentMethodList.get(0);
        }
        this.showTransferButtonAsEnabled();

        this.amountTextView.setPrefix(DCurrencies.map(this.fundingProduct.getCurrency()));
        this.updateAmountText();

        this.numPadDigitConsumer = this::onDigitClicked;
        this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
        this.numPadDotAction = this::onDotClicked;
        this.DNumPad.addDotAction(this.numPadDotAction);
        this.numPadDeleteAction = this::onDeleteClicked;
        this.DNumPad.addDeleteAction(this.numPadDeleteAction);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
        this.numPadDeleteAction = null;
        this.DNumPad.removeDotAction(this.numPadDotAction);
        this.numPadDotAction = null;
        this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
        this.numPadDigitConsumer = null;

        this.unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public void onPaymentMethodChosen(Product product) {
        this.destinationProduct = product;
        this.showTransferButtonAsEnabled();
    }

    public final void onDigitClicked(int digit) {
        BigDecimal addition = BigDecimal.valueOf(digit);
        if (this.mustShowDot && this.fractionOffset.compareTo(HUNDRED) < 0) {
            this.value = this.value.add(
                    addition.divide(
                            TEN.multiply(this.fractionOffset),
                            2,
                            BigDecimal.ROUND_CEILING
                    )
            );
            this.fractionOffset = this.fractionOffset.multiply(TEN);
            this.updateAmountText();
        } else if (!mustShowDot) {
            this.value = this.value.multiply(TEN)
                    .add(addition);
            this.updateAmountText();
        }
    }

    public final void onDotClicked() {
        if (!this.mustShowDot) {
            this.mustShowDot = true;
            this.updateAmountText();
        }
    }

    public final void onDeleteClicked() {
        final int result = this.value.compareTo(ZERO);
        if (result > 0) {
            final BigDecimal fraction = getFraction(this.value);
            if (isFraction(fraction)) {
                this.value = value.subtract(
                        fraction.multiply(this.fractionOffset)
                                .remainder(TEN)
                                .divide(this.fractionOffset, 2, BigDecimal.ROUND_CEILING)
                );
                this.fractionOffset = this.fractionOffset.divide(TEN, 2, RoundingMode.CEILING);
            } else if (this.mustShowDot) {
                this.mustShowDot = false;
            } else {
                this.value = this.value.divideToIntegralValue(TEN);
            }
            this.updateAmountText();
        } else if (this.mustShowDot) {
            this.mustShowDot = false;
            this.updateAmountText();
        }
    }

    public final void setTransferResult(boolean succeeded, String transactionId) {
        PinConfirmationDialogFragment.dismiss(getSupportFragmentManager(), succeeded);
        if (succeeded) {
            this.setResult(RESULT_OK, OwnTransactionCreationActivity.serializeResult(transactionId));
            this.finish();
        }
    }

    public void showGenericErrorDialog(String title, String message) {
        Dialogs.builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.error_positive_button_text, null)
                .show();
    }

    public void showGenericErrorDialog(String message) {
        showGenericErrorDialog(getString(R.string.error_generic_title), message);
    }

    public void showGenericErrorDialog() {
        showGenericErrorDialog(getString(R.string.error_generic));
    }

    public void showUnavailableNetworkError() {
        Toast.makeText(this, R.string.error_unavailable_network, Toast.LENGTH_LONG)
                .show();
    }
}
