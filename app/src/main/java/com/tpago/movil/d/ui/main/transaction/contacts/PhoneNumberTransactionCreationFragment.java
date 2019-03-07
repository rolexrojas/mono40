package com.tpago.movil.d.ui.main.transaction.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.DNumPad;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCategory;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.PrefixableTextView;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tpago.movil.d.ui.main.transaction.TransactionCategory.TRANSFER;

/**
 * @author hecvasro
 */
public class PhoneNumberTransactionCreationFragment
        extends ChildFragment<TransactionCreationContainer>
        implements PhoneNumberTransactionCreationScreen,
        PaymentMethodChooser.OnPaymentMethodChosenListener {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal TEN = BigDecimal.TEN;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private TransactionCreationActivityBase activity;

    @Inject
    StringHelper stringHelper;
    @Inject
    TransactionCategory transactionCategory;
    @Inject
    PhoneNumberTransactionCreationPresenter presenter;
    @Inject
    Recipient recipient;
    @Inject
    AtomicReference<Product> fundingAccount;
    @Inject
    AtomicReference<BigDecimal> value;

    private Unbinder unbinder;

    @BindView(R.id.transaction_creation_amount)
    PrefixableTextView amountTextView;
    @BindView(R.id.transaction_creation_num_pad)
    DNumPad DNumPad;
    @BindView(R.id.action_recharge)
    Button rechargeActionButton;
    @BindView(R.id.action_transfer)
    Button transferActionButton;

    @BindView(R.id.payment_method_chooser)
    PaymentMethodChooser paymentMethodChooser;

    private boolean mustShowDot = false;
    private BigDecimal fractionOffset = ONE;

    private Consumer<Integer> numPadDigitConsumer;
    private Action numPadDotAction;
    private Action numPadDeleteAction;

    @NonNull
    public static PhoneNumberTransactionCreationFragment newInstance() {
        return new PhoneNumberTransactionCreationFragment();
    }

    @NonNull
    private static BigDecimal getFraction(@NonNull BigDecimal value) {
        return value.subtract(BigDecimal.valueOf(value.intValue()));
    }

    private static boolean isFraction(@NonNull BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.ONE) < 0;
    }

    @NonNull
    private static String getFormattedValue(
            @NonNull BigDecimal value,
            @NonNull BigDecimal fractionOffset, boolean mustShowDot
    ) {
        String formattedValue = Formatter.amount(value);
        final BigDecimal fraction = getFraction(value);
        if (!isFraction(fraction)) {
            formattedValue = formattedValue.replace(".00", "");
            if (mustShowDot) {
                formattedValue += ".";
            }
        } else if (fraction.multiply(fractionOffset)
                .compareTo(BigDecimal.TEN) < 0) {
            formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
        }
        return formattedValue;
    }

    private void updateAmountText() {
        amountTextView.setContent(getFormattedValue(value.get(), fractionOffset, mustShowDot));
    }

    @OnClick(R.id.action_transfer)
    final void onTransferButtonClicked() {
        if (this.value.get()
                .compareTo(BigDecimal.ZERO) <= 0) {
            // TODO: Let the user know that he must insert an amount greater than zero.
        } else {
            this.presenter.onTransferButtonClicked();
        }
    }

    @OnClick(R.id.action_recharge)
    final void onRechargeButtonClicked() {
        if (this.value.get()
                .compareTo(BigDecimal.ZERO) <= 0) {
            // TODO: Let the user know that he must insert an amount greater than zero.
        } else {
            this.presenter.onRechargeButtonClicked();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Injects all the annotated dependencies.
        final PhoneNumberTransactionCreationComponent component
                = DaggerPhoneNumberTransactionCreationComponent
                .builder()
                .transactionCreationComponent(getContainer().getComponent())
                .build();
        component.inject(this);
        activity = (TransactionCreationActivityBase) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater
                .inflate(R.layout.d_fragment_transaction_creation_phone_number, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Binds all the annotated views and methods.
        unbinder = ButterKnife.bind(this, view);
        // Adds a listener that gets notified every time a payment option is chosen.
        paymentMethodChooser.setOnPaymentMethodChosenListener(this);
        // Adds a listener that gets notified every time a num pad button is pressed.
        this.numPadDigitConsumer = this::onDigitClicked;
        this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
        this.numPadDotAction = this::onDotClicked;
        this.DNumPad.addDotAction(this.numPadDotAction);
        this.numPadDeleteAction = this::onDeleteClicked;
        this.DNumPad.addDeleteAction(this.numPadDeleteAction);

        if (this.transactionCategory == TRANSFER) {
            this.transferActionButton.setVisibility(View.VISIBLE);
            this.rechargeActionButton.setVisibility(View.GONE);
        } else {
            this.transferActionButton.setVisibility(View.GONE);
            this.rechargeActionButton.setVisibility(View.VISIBLE);
        }

        // Attaches the screen to the presenter.
        presenter.attachScreen(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Starts the presenter.
        presenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAmountText();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stops the presenter.
        presenter.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Removes the listener that gets notified every time a num pad button is pressed.
        this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
        this.numPadDeleteAction = null;
        this.DNumPad.removeDotAction(this.numPadDotAction);
        this.numPadDotAction = null;
        this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
        this.numPadDigitConsumer = null;

        // Removes the listener that gets notified every time a payment option is chosen.
        paymentMethodChooser.setOnPaymentMethodChosenListener(null);
        // Detaches the screen from the presenter.
        presenter.detachScreen();
        // Unbinds all the annotated views and methods.
        unbinder.unbind();
    }

    @Override
    public void setPaymentOptions(@NonNull List<Product> paymentMethodList) {
        paymentMethodChooser.setPaymentMethodList(paymentMethodList);
    }

    @Override
    public void setPaymentOptionCurrency(@NonNull String currency) {
        amountTextView.setPrefix(currency);
    }

    @Override
    public void setPaymentResult(boolean succeeded, String transactionId) {
        PinConfirmationDialogFragment.dismiss(this.getChildFragmentManager(), succeeded);
        if (succeeded) {
            this.getContainer()
                    .finish(true, transactionId);
        }
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s == "";
    }

    private String selectLabelToShow(String recipientName, String label, String identifier) {
        if (!isNullOrEmpty(recipientName)) {
            return recipientName;
        }

        if (!isNullOrEmpty(label)) {
            return label;
        }

        return identifier;
    }

    @Override
    public void requestPin() {
        if (value.get()
                .compareTo(ZERO) > 0) {
            final int[] location = new int[2];
            transferActionButton.getLocationOnScreen(location);
            final int x = location[0] + (transferActionButton.getWidth() / 2);
            final int y = location[1];
            final String currency = amountTextView.getPrefix()
                    .toString();
            String label = selectLabelToShow(activity.getRecipientName(), recipient.getLabel(), recipient.getIdentifier());
            final String description;

            final double taxPercentage = 0.15;
            double taxAmount = value.get().doubleValue() * (taxPercentage / 100);
            String taxAmountText = Formatter.amount("RD", new BigDecimal(taxAmount));

            if (transactionCategory == TRANSFER) {
                description = getString(
                        R.string.format_transfer_to,
                        Formatter.amount(currency, value.get()),
                        label,
                        Formatter.amount(
                                currency,
                                this.fundingAccount.get()
                                        .getBank()
                                        .calculateTransferCost(this.value.get())
                        ),
                        taxPercentage + "%",
                        taxAmountText
                );
            } else {
                description = String.format(
                        "Recargar %1$s a %2$s\nImp. DGII %3$s RD$%4$s",
                        Formatter.amount(
                                currency,
                                value.get()
                        ),
                        label
                );
            }
            PinConfirmationDialogFragment.show(
                    getChildFragmentManager(),
                    description,
                    (PinConfirmationDialogFragment.Callback) pin -> presenter.transferTo(value.get(), pin),
                    x,
                    y
            );
        } else {
            // TODO: Let the user know that he must insert an amount greater than zero.
        }
    }

    @Override
    public void requestBankAndAccountNumber() {
        this.getContainer()
                .setChildFragment(new NonAffiliatedPhoneNumberTransactionCreation1Fragment());
    }

    @Override
    public void requestCarrier() {
        this.getContainer()
                .setChildFragment(CarrierSelectionFragment.create());
    }

    @Override
    public void finish() {
        this.getContainer()
                .finish(false, null);
    }

    public final void onDigitClicked(int digit) {
        BigDecimal addition = BigDecimal.valueOf(digit);
        if (mustShowDot && fractionOffset.compareTo(HUNDRED) < 0) {
            value.set(
                    value.get()
                            .add(addition.divide(
                                    TEN.multiply(fractionOffset),
                                    2,
                                    BigDecimal.ROUND_CEILING
                            )));
            fractionOffset = fractionOffset.multiply(TEN);
            updateAmountText();
        } else if (!mustShowDot) {
            value.set(value.get()
                    .multiply(TEN)
                    .add(addition));
            updateAmountText();
        }
    }

    public final void onDotClicked() {
        if (!mustShowDot) {
            mustShowDot = true;
            updateAmountText();
        }
    }

    public final void onDeleteClicked() {
        final int result = value.get()
                .compareTo(ZERO);
        if (result > 0) {
            final BigDecimal fraction = getFraction(value.get());
            if (isFraction(fraction)) {
                value.set(
                        value.get()
                                .subtract(fraction.multiply(fractionOffset)
                                        .remainder(TEN)
                                        .divide(fractionOffset, 2, BigDecimal.ROUND_CEILING)));
                fractionOffset = fractionOffset.divide(TEN, 2, RoundingMode.CEILING);
            } else if (mustShowDot) {
                mustShowDot = false;
            } else {
                value.set(value.get()
                        .divideToIntegralValue(TEN));
            }
            updateAmountText();
        } else if (mustShowDot) {
            mustShowDot = false;
            updateAmountText();
        }
    }

    @Override
    public void onPaymentMethodChosen(Product product) {
        this.fundingAccount.set(product);
        this.presenter.setPaymentOption(product);
    }

    public void showGenericErrorDialog(String title, String message) {
        Dialogs.builder(getContext())
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
        Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
                .show();
    }
}
