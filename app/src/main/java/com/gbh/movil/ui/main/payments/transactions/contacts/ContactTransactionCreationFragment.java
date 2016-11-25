package com.gbh.movil.ui.main.payments.transactions.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.data.Formatter;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.PinConfirmationDialogFragment;
import com.gbh.movil.ui.main.payments.transactions.PaymentOptionAdapter;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationContainer;
import com.gbh.movil.ui.view.widget.CustomAmountView;
import com.gbh.movil.ui.view.widget.NumPad;

import java.math.BigDecimal;
import java.util.Set;

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
public class ContactTransactionCreationFragment extends SubFragment<TransactionCreationContainer>
  implements PhoneNumberTransactionCreationScreen, Spinner.OnItemSelectedListener,
  NumPad.OnButtonClickedListener {
  /**
   * TODO
   */
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  @Inject
  PhoneNumberTransactionCreationPresenter presenter;
  @Inject
  Recipient recipient;

  private Unbinder unbinder;

  private PaymentOptionAdapter paymentOptionAdapter;

  @BindView(R.id.transaction_creation_payment_option_chooser)
  Spinner paymentOptionChooser;
  @BindView(R.id.transaction_creation_amount)
  CustomAmountView amountView;
  @BindView(R.id.transaction_creation_num_pad)
  NumPad numPad;
  @BindView(R.id.action_transfer)
  Button transferActionButton;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static ContactTransactionCreationFragment newInstance() {
    return new ContactTransactionCreationFragment();
  }

  @OnClick(R.id.action_recharge)
  void onRechargeButtonClicked() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_recharge), getString(R.string.ok), null, null, null)
      .show();
  }

  @OnClick(R.id.action_transfer)
  void onTransferButtonClicked() {
    final BigDecimal value = amountView.getValue();
    if (value.compareTo(BigDecimal.ZERO) > 0) {
      final int[] location = new int[2];
      transferActionButton.getLocationOnScreen(location);
      final int x = location[0] + (transferActionButton.getWidth() / 2);
      final int y = location[1];
      final String identifier;
      if (Utils.isNotNull(recipient.getLabel())) {
        identifier = recipient.getLabel();
      } else {
        identifier = recipient.getIdentifier();
      }
      final String description = String.format(getString(R.string.format_transfer_to),
        Formatter.amount(amountView.getCurrency(), value), identifier);
      PinConfirmationDialogFragment.newInstance(x, y, description,
        new PinConfirmationDialogFragment.Callback() {
          @Override
          public void confirm(@NonNull String pin) {
            presenter.transferTo(value, pin);
          }
        }).show(getChildFragmentManager(), TAG_PIN_CONFIRMATION);
    } else {
      // TODO: Let the user know that he must set the amount that he wants to transfer.
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final ContactTransactionCreationComponent component = DaggerContactTransactionCreationComponent
      .builder()
      .transactionCreationComponent(container.getComponent())
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
    paymentOptionAdapter = new PaymentOptionAdapter(getContext());
    paymentOptionChooser.setAdapter(paymentOptionAdapter);
    // Adds a listener that gets notified every time a payment option is chosen.
    paymentOptionChooser.setOnItemSelectedListener(this);
    // Adds a listener that gets notified every time a num pad button is pressed.
    numPad.setOnButtonClickedListener(this);
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
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Removes the listener that gets notified every time a num pad button is pressed.
    numPad.setOnButtonClickedListener(null);
    // Removes the listener that gets notified every time a payment option is chosen.
    paymentOptionChooser.setOnItemSelectedListener(null);
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void setPaymentOptions(@NonNull Set<Product> paymentOptions) {
    paymentOptionAdapter.clear();
    paymentOptionAdapter.addAll(paymentOptions);
  }

  @Override
  public void setPaymentOptionCurrency(@NonNull String currency) {
    amountView.setCurrency(currency);
  }

  @Override
  public void clearAmount() {
    amountView.setValue(BigDecimal.ZERO);
  }

  @Override
  public void setPaymentResult(boolean succeeded) {
    final Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).resolve(succeeded);
    }
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
  public void onTextButtonClicked(@NonNull String content) {
    if (TextUtils.isDigitsOnly(content)) {
      amountView.pushDigit(Integer.parseInt(content));
    } else {
      amountView.pushDot();
    }
  }

  @Override
  public void onDeleteButtonClicked() {
    amountView.pop();
  }
}
