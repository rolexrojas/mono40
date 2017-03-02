package com.tpago.movil.dep.ui.main.transactions.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.tpago.movil.R;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.dep.ui.main.recipients.NonAffiliatedPhoneNumberRecipientAdditionActivity;
import com.tpago.movil.dep.ui.main.transactions.PaymentOptionAdapter;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationContainer;
import com.tpago.movil.dep.ui.view.widget.pad.Digit;
import com.tpago.movil.dep.ui.view.widget.pad.Dot;
import com.tpago.movil.dep.ui.view.widget.pad.DepNumPad;
import com.tpago.movil.dep.ui.view.widget.PrefixableTextView;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PhoneNumberTransactionCreationFragment
  extends ChildFragment<TransactionCreationContainer>
  implements PhoneNumberTransactionCreationScreen,
  Spinner.OnItemSelectedListener,
  DepNumPad.OnDigitClickedListener,
  DepNumPad.OnDotClickedListener,
  DepNumPad.OnDeleteClickedListener,
  PinConfirmationDialogFragment.OnDismissListener {
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  private static final int REQUEST_CODE = 0;

  private static final BigDecimal ZERO = BigDecimal.ZERO;
  private static final BigDecimal ONE = BigDecimal.ONE;
  private static final BigDecimal TEN = BigDecimal.TEN;
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  @Inject
  AssetProvider assetProvider;
  @Inject
  PhoneNumberTransactionCreationPresenter presenter;
  @Inject
  Recipient recipient;

  private Unbinder unbinder;

  private PaymentOptionAdapter paymentOptionAdapter;

  @BindView(R.id.transaction_creation_payment_option_chooser)
  Spinner paymentOptionChooser;
  @BindView(R.id.transaction_creation_amount)
  PrefixableTextView amountTextView;
  @BindView(R.id.transaction_creation_num_pad)
  DepNumPad numPad;
  @BindView(R.id.action_transfer)
  Button transferActionButton;

  private boolean mustShowDot = false;
  private BigDecimal amount = ZERO;
  private BigDecimal fractionOffset = ONE;

  private String resultMessage = null;

  private boolean shouldBeClosed = false;
  private Recipient requestResult = null;

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
    amountTextView.setContent(getFormattedValue(amount, fractionOffset, mustShowDot));
  }

  @OnClick(R.id.action_recharge)
  void onRechargeButtonClicked() {
    Dialogs.featureNotAvailable(getActivity())
      .show();
  }

  @OnClick(R.id.action_transfer)
  void onTransferButtonClicked(View view) {
    if (amount.compareTo(ZERO) > 0) {
      final int[] location = new int[2];
      view.getLocationOnScreen(location);
      final int x = location[0] + (view.getWidth() / 2);
      final int y = location[1];
      final String description = String.format(getString(R.string.format_transfer_to),
        recipient.getIdentifier(), Formatter.amount(amountTextView.getPrefix().toString(), amount));
      PinConfirmationDialogFragment.newInstance(x, y, description,
        new PinConfirmationDialogFragment.Callback() {
          @Override
          public void confirm(@NonNull String pin) {
            presenter.transferTo(amount, pin);
          }
        }).show(getChildFragmentManager(), TAG_PIN_CONFIRMATION);
    } else {
      // TODO: Let the user know that he must insert an amount greater than zero.
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      shouldBeClosed = resultCode != Activity.RESULT_OK;
      if (!shouldBeClosed) {
        final Recipient recipient = NonAffiliatedPhoneNumberRecipientAdditionActivity
          .deserializeResult(data);
        if (Objects.isNotNull(recipient)) {
          requestResult = recipient;
        }
      }
    }
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
    return inflater.inflate(R.layout.fragment_transaction_creation_phone_number, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the list of payment options.
    paymentOptionAdapter = new PaymentOptionAdapter(getContext(), assetProvider);
    paymentOptionChooser.setAdapter(paymentOptionAdapter);
    // Adds a listener that gets notified every time a payment option is chosen.
    paymentOptionChooser.setOnItemSelectedListener(this);
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
    presenter.start(shouldBeClosed, requestResult);
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
    paymentOptionChooser.setOnItemSelectedListener(null);
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void setPaymentOptions(@NonNull List<Product> paymentOptions) {
    paymentOptionAdapter.clear();
    paymentOptionAdapter.addAll(paymentOptions);
  }

  @Override
  public void setPaymentOptionCurrency(@NonNull String currency) {
    amountTextView.setPrefix(currency);
  }

  @Override
  public void clearAmount() {
    amount = ZERO;
    mustShowDot = false;
    updateAmountText();
  }

  @Override
  public void setPaymentResult(boolean succeeded, String message) {
    this.resultMessage = message;
    final Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).resolve(succeeded);
    }
  }

  @Override
  public void requestBankAndAccountNumber() {
    startActivityForResult(
      NonAffiliatedPhoneNumberRecipientAdditionActivity.getLaunchIntent(
        getContext(),
        (NonAffiliatedPhoneNumberRecipient) recipient),
      REQUEST_CODE);
  }

  @Override
  public void finish() {
    getContainer().finish(false, null);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    final Product product = paymentOptionAdapter.getItem(position);
    if (Utils.isNotNull(product)) {
      presenter.setPaymentOption(product);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Ignored, there will be always a payment option selected.
  }

  @Override
  public void onDigitClicked(@NonNull Digit digit) {
    BigDecimal addition = BigDecimal.valueOf(digit.getValue());
    if (mustShowDot && fractionOffset.compareTo(HUNDRED) < 0) {
      amount = amount.add(addition.divide(TEN.multiply(fractionOffset), 2,
        BigDecimal.ROUND_CEILING));
      fractionOffset = fractionOffset.multiply(TEN);
      updateAmountText();
    } else if (!mustShowDot) {
      amount = amount.multiply(TEN).add(addition);
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
    final int result = amount.compareTo(ZERO);
    if (result > 0) {
      final BigDecimal fraction = getFraction(amount);
      if (isFraction(fraction)) {
        amount = amount.subtract(fraction.multiply(fractionOffset).remainder(TEN)
          .divide(fractionOffset, 2, BigDecimal.ROUND_CEILING));
        fractionOffset = fractionOffset.divide(TEN, 2, RoundingMode.CEILING);
      } else if (mustShowDot) {
        mustShowDot = false;
      } else {
        amount = amount.divideToIntegralValue(TEN);
      }
      updateAmountText();
    } else if (mustShowDot) {
      mustShowDot = false;
      updateAmountText();
    }
  }

  @Override
  public void onDismiss(boolean succeeded) {
    if (succeeded) {
      getContainer().finish(true, resultMessage);
    } else {
      final String message = Texts.isEmpty(resultMessage) ? getString(R.string.error_message) : resultMessage;
      Dialogs.builder(getContext())
        .setTitle(R.string.error_title)
        .setMessage(message)
        .setPositiveButton(R.string.error_positive_button_text, null)
        .create()
        .show();
    }
  }
}
