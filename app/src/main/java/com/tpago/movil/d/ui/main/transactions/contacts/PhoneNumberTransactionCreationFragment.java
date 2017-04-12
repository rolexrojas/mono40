package com.tpago.movil.d.ui.main.transactions.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.transactions.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.pad.Digit;
import com.tpago.movil.d.ui.view.widget.pad.Dot;
import com.tpago.movil.d.ui.view.widget.pad.DepNumPad;
import com.tpago.movil.d.ui.view.widget.PrefixableTextView;
import com.tpago.movil.main.transactions.PaymentMethodChooser;
import com.tpago.movil.text.Texts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class PhoneNumberTransactionCreationFragment
  extends ChildFragment<TransactionCreationContainer>
  implements PhoneNumberTransactionCreationScreen,
  PaymentMethodChooser.OnPaymentMethodChosenListener,
  DepNumPad.OnDigitClickedListener,
  DepNumPad.OnDotClickedListener,
  DepNumPad.OnDeleteClickedListener {
  private static final BigDecimal ZERO = BigDecimal.ZERO;
  private static final BigDecimal ONE = BigDecimal.ONE;
  private static final BigDecimal TEN = BigDecimal.TEN;
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  @Inject
  StringHelper stringHelper;
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
  DepNumPad numPad;
  @BindView(R.id.action_transfer)
  Button transferActionButton;

  @BindView(R.id.payment_method_chooser) PaymentMethodChooser paymentMethodChooser;

  private boolean mustShowDot = false;
  private BigDecimal fractionOffset = ONE;

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
  private static String getFormattedValue(@NonNull BigDecimal value,
    @NonNull BigDecimal fractionOffset, boolean mustShowDot) {
    String formattedValue = Formatter.amount(value);
    final BigDecimal fraction = getFraction(value);
    if (!isFraction(fraction)) {
      formattedValue = formattedValue.replace(".00", "");
      if (mustShowDot) {
        formattedValue += ".";
      }
    } else if (fraction.multiply(fractionOffset).compareTo(BigDecimal.TEN) < 0) {
      formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
    }
    return formattedValue;
  }

  private void updateAmountText() {
    amountTextView.setContent(getFormattedValue(value.get(), fractionOffset, mustShowDot));
  }

  @OnClick(R.id.action_recharge)
  void onRechargeButtonClicked() {
    Dialogs.featureNotAvailable(getActivity())
      .show();
  }

  @OnClick(R.id.action_transfer)
  void onTransferButtonClicked() {
    presenter.onTransferButtonClicked();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final PhoneNumberTransactionCreationComponent component = DaggerPhoneNumberTransactionCreationComponent
      .builder()
      .transactionCreationComponent(getContainer().getComponent())
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.d_fragment_transaction_creation_phone_number, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Adds a listener that gets notified every time a payment option is chosen.
    paymentMethodChooser.setOnPaymentMethodChosenListener(this);
    // Adds a listener that gets notified every time a num pad button is pressed.
    numPad.setOnDigitClickedListener(this);
    numPad.setOnDotClickedListener(this);
    numPad.setOnDeleteClickedListener(this);
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
    numPad.setOnDeleteClickedListener(null);
    numPad.setOnDotClickedListener(null);
    numPad.setOnDigitClickedListener(null);
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
    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), succeeded);
    if (succeeded) {
      getContainer().finish(true, transactionId);
    }
  }

  @Override
  public void requestPin() {
    if (value.get().compareTo(ZERO) > 0) {
      final int[] location = new int[2];
      transferActionButton.getLocationOnScreen(location);
      final int x = location[0] + (transferActionButton.getWidth() / 2);
      final int y = location[1];
      final String currency = amountTextView.getPrefix().toString();
      String label = recipient.getLabel();
      if (Texts.checkIfEmpty(label)) {
        label = recipient.getIdentifier();
      }
      final String description = getString(
        R.string.format_transfer_to,
        Formatter.amount(currency, value.get()),
        label,
        Formatter.amount(currency, BigDecimal.ZERO)); // TODO: Add the cost of a transfer.
      PinConfirmationDialogFragment.show(
        getChildFragmentManager(),
        description,
        new PinConfirmationDialogFragment.Callback() {
          @Override
          public void confirm(String pin) {
            presenter.transferTo(value.get(), pin);
          }
        },
        x,
        y);
    } else {
      // TODO: Let the user know that he must insert an amount greater than zero.
    }
  }

  @Override
  public void requestBankAndAccountNumber() {
    getContainer().setChildFragment(new NonAffiliatedPhoneNumberTransactionCreation1Fragment());
  }

  @Override
  public void finish() {
    getContainer().finish(false, null);
  }

  @Override
  public void showTransferButtonAsEnabled(boolean showAsEnabled) {
    transferActionButton.setAlpha(showAsEnabled ? 1.0F : 0.5F);
  }

  @Override
  public void onDigitClicked(@NonNull Digit digit) {
    BigDecimal addition = BigDecimal.valueOf(digit.getValue());
    if (mustShowDot && fractionOffset.compareTo(HUNDRED) < 0) {
      value.set(
        value.get()
          .add(addition.divide(TEN.multiply(fractionOffset),
            2,
            BigDecimal.ROUND_CEILING)));
      fractionOffset = fractionOffset.multiply(TEN);
      updateAmountText();
    } else if (!mustShowDot) {
      value.set(value.get().multiply(TEN).add(addition));
      updateAmountText();
    }
  }

  @Override
  public void onDotClicked(@NonNull Dot dot) {
    if (!mustShowDot) {
      mustShowDot = true;
      updateAmountText();
    }
  }

  @Override
  public void onDeleteClicked() {
    final int result = value.get().compareTo(ZERO);
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
        value.set(value.get().divideToIntegralValue(TEN));
      }
      updateAmountText();
    } else if (mustShowDot) {
      mustShowDot = false;
      updateAmountText();
    }
  }

  @Override
  public void onPaymentMethodChosen(Product product) {
    fundingAccount.set(product);
    presenter.setPaymentOption(product);
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
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG).show();
  }
}
