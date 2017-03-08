package com.tpago.movil.dep.ui.main.transactions.bills;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.dep.ui.main.transactions.PaymentOptionAdapter;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationComponent;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationContainer;
import com.tpago.movil.dep.ui.view.widget.PrefixableTextView;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */

public class BillTransactionCreationFragment
  extends ChildFragment<TransactionCreationContainer>
  implements BillTransactionCreationPresenter.View,
  PinConfirmationDialogFragment.OnDismissListener {
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  private BillTransactionCreationPresenter presenter;

  private Unbinder unbinder;
  private PaymentOptionAdapter paymentOptionAdapter;

  private String resultMessage = null;

  @Inject
  DepAssetProvider assetProvider;

  @BindView(R.id.button)
  Button button;
  @BindView(R.id.prefixable_text_view_total_owed)
  PrefixableTextView totalOwedPrefixableTextView;
  @BindView(R.id.text_view_due_date)
  TextView dueDateTextView;
  @BindView(R.id.transaction_creation_payment_option_chooser)
  Spinner paymentOptionChooser;
  @BindView(R.id.prefixable_text_view_total)
  PrefixableTextView totalPrefixableTextView;
  @BindView(R.id.prefixable_text_view_minimum)
  PrefixableTextView minimumPrefixableTextView;
  @BindView(R.id.radio_button_pay_total)
  RadioButton totalRadioButton;
  @BindView(R.id.radio_button_pay_minimum)
  RadioButton minimumRadioButton;

  public static BillTransactionCreationFragment create() {
    return new BillTransactionCreationFragment();
  }

  @OnClick(R.id.view_total)
  void onPayTotalButtonClicked() {
    presenter.onOptionSelectionChanged(BillRecipient.Option.TOTAL);
  }

  @OnClick(R.id.view_minimum)
  void onPayMinimumButtonClicked() {
    presenter.onOptionSelectionChanged(BillRecipient.Option.MINIMUM);
  }

  @OnClick(R.id.button)
  void onPayButtonClicked() {
    presenter.onPayButtonClicked();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final TransactionCreationContainer container = getContainer();
    final TransactionCreationComponent component = container.getComponent();
    component.inject(this);
    presenter = new BillTransactionCreationPresenter(this, component);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_transaction_creation_bill, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    paymentOptionAdapter = new PaymentOptionAdapter(getContext(), assetProvider);
    paymentOptionChooser.setAdapter(paymentOptionAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onViewStarted();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter = null;
  }

  @Override
  public void setCurrencyValue(String value) {
    totalOwedPrefixableTextView.setPrefix(value);
    totalPrefixableTextView.setPrefix(value);
    minimumPrefixableTextView.setPrefix(value);
  }

  @Override
  public void setDueDateValue(String value) {
    dueDateTextView.setText(value);
  }

  @Override
  public void setTotalValue(String value) {
    totalOwedPrefixableTextView.setContent(value);
    totalPrefixableTextView.setContent(value);
  }

  @Override
  public void setMinimumValue(String value) {
    minimumPrefixableTextView.setContent(value);
  }

  @Override
  public void setPaymentOptions(List<Product> paymentOptions) {
    paymentOptionAdapter.clear();
    paymentOptionAdapter.addAll(paymentOptions);
  }

  @Override
  public void setOptionChecked(BillRecipient.Option option) {
    if (option.equals(BillRecipient.Option.TOTAL)) {
      minimumRadioButton.setChecked(false);
      totalRadioButton.setChecked(true);
      button.setText(R.string.pay_total);
    } else {
      totalRadioButton.setChecked(false);
      minimumRadioButton.setChecked(true);
      button.setText(R.string.pay_minimum);
    }
  }

  @Override
  public void setPayButtonEnabled(boolean enabled) {
    button.setEnabled(enabled);
    button.setAlpha(enabled ? 1.00F : 0.50F);
  }

  @Override
  public void requestPin(String partnerName, String value) {
    final int x = Math.round((button.getRight() - button.getLeft()) / 2);
    final int y = Math.round(button.getY() + ((button.getBottom() - button.getTop()) / 2));
    PinConfirmationDialogFragment.newInstance(
      x,
      y,
      getString(R.string.transaction_creation_bill_confirmation, value, partnerName),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(String pin) {
          presenter.onPinRequestFinished(
            paymentOptionAdapter.getItem(paymentOptionChooser.getSelectedItemPosition()),
            pin);
        }
      })
      .show(getChildFragmentManager(), TAG_PIN_CONFIRMATION);
  }

  @Override
  public void setPaymentResult(boolean succeeded, String message) {
    resultMessage = message;
    final Fragment f = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Objects.isNotNull(f)) {
      ((PinConfirmationDialogFragment) f).resolve(succeeded);
    }
  }

  @Override
  public void onDismiss(boolean succeeded) {
    if (succeeded) {
      getContainer().finish(true, resultMessage);
    } else {
      final String message = Texts.isEmpty(resultMessage)
        ? getString(R.string.error_message)
        : resultMessage;
      Dialogs.builder(getContext())
        .setTitle(R.string.error_title)
        .setMessage(message)
        .setPositiveButton(R.string.error_positive_button_text, null)
        .create()
        .show();
    }
  }
}
