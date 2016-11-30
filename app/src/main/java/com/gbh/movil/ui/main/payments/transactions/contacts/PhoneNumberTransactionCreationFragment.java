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
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.PinConfirmationDialogFragment;
import com.gbh.movil.ui.main.payments.transactions.PaymentOptionAdapter;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationContainer;
import com.gbh.movil.ui.view.widget.NumPad;
import com.gbh.movil.ui.view.widget.PrefixableTextView;

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
public class PhoneNumberTransactionCreationFragment extends SubFragment<TransactionCreationContainer>
  implements PhoneNumberTransactionCreationScreen, Spinner.OnItemSelectedListener,
  NumPad.OnDigitClickedListener, NumPad.OnDotClickedListener, NumPad.OnDeleteClickedListener {
  /**
   * TODO
   */
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

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
  NumPad numPad;
  @BindView(R.id.action_transfer)
  Button transferActionButton;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static PhoneNumberTransactionCreationFragment newInstance() {
    return new PhoneNumberTransactionCreationFragment();
  }

  @OnClick(R.id.action_recharge)
  void onRechargeButtonClicked() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_recharge), getString(R.string.ok), null, null, null)
      .show();
  }

  @OnClick(R.id.action_transfer)
  void onTransferButtonClicked() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final PhoneNumberTransactionCreationComponent component = DaggerPhoneNumberTransactionCreationComponent
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
  public void setPaymentOptions(@NonNull Set<Product> paymentOptions) {
    paymentOptionAdapter.clear();
    paymentOptionAdapter.addAll(paymentOptions);
  }

  @Override
  public void setPaymentOptionCurrency(@NonNull String currency) {
    amountTextView.setPrefix(currency);
  }

  @Override
  public void clearAmount() {
    // TODO
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
  public void onDigitClicked(@NonNull NumPad.Digit digit) {
    // TODO
  }

  @Override
  public void onDotClicked() {
    // TODO
  }

  @Override
  public void onDeleteClicked() {
    // TODO
  }
}
