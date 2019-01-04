package com.tpago.movil.d.ui.main.transaction.products;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.PrefixableTextView;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.util.UiUtil;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class LoanTransactionCreationFragment extends ChildFragment<TransactionCreationContainer>
  implements LoanTransactionCreationPresenter.View {

  private LoanTransactionCreationPresenter presenter;

  private Unbinder unbinder;

  @Inject StringHelper stringHelper;

  @BindView(R.id.button) Button button;
  @BindView(R.id.payment_method_chooser) PaymentMethodChooser paymentMethodChooser;
  @BindView(R.id.prefixable_text_view_period) PrefixableTextView periodPrefixableTextView;
  @BindView(R.id.prefixable_text_view_total) PrefixableTextView totalPrefixableTextView;
  @BindView(R.id.prefixable_text_view_total_owed) PrefixableTextView totalOwedPrefixableTextView;
  @BindView(R.id.radio_button_pay_period) RadioButton periodRadioButton;
  @BindView(R.id.radio_button_pay_total) RadioButton totalRadioButton;
  @BindView(R.id.text_view_due_date) TextView dueDateTextView;
  @BindView(R.id.view_period) View periodView;
  @BindView(R.id.view_total) View totalView;

  @BindView(R.id.containerLinearLayout) LinearLayout containerLinearLayout;

  @BindView(R.id.otherAmountCurrencyTextView) TextView otherAmountCurrencyTextView;
  @BindView(R.id.otherAmountEditText) TextView otherAmountEditText;
  @BindView(R.id.otherAmountRadioButton) RadioButton otherAmountRadioButton;
  private TextWatcher otherAmountTextWatcher;

  public static LoanTransactionCreationFragment create() {
    return new LoanTransactionCreationFragment();
  }

  @OnClick(R.id.view_total)
  final void onPayTotalButtonClicked() {
    presenter.onOptionSelectionChanged(LoanBillBalance.Option.CURRENT);
  }

  @OnClick(R.id.view_period)
  final void onPayPeriodButtonClicked() {
    presenter.onOptionSelectionChanged(LoanBillBalance.Option.PERIOD);
  }

  @OnClick(R.id.otherAmountView)
  final void onOtherAmountClicked() {
    this.presenter.onOptionSelectionChanged(LoanBillBalance.Option.OTHER);
  }

  @OnClick(R.id.button)
  final void onPayButtonClicked() {
    presenter.onPayButtonClicked();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final TransactionCreationContainer container = getContainer();
    final TransactionCreationComponent component = container.getComponent();
    component.inject(this);
    presenter = new LoanTransactionCreationPresenter(this, component);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_transaction_creation_loan, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    this.otherAmountTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0){
          presenter.onOtherAmountChanged(new BigDecimal(s.toString()));
        }else{
          presenter.onOtherAmountChanged(new BigDecimal(0));
        }
      }
    };
    this.otherAmountEditText.addTextChangedListener(this.otherAmountTextWatcher);
  }

  @Override
  public void onPause() {
    this.otherAmountEditText.removeTextChangedListener(this.otherAmountTextWatcher);
    this.otherAmountTextWatcher = null;
    super.onPause();
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
    periodPrefixableTextView.setPrefix(value);
    totalPrefixableTextView.setPrefix(value);
    otherAmountCurrencyTextView.setText(value);
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
  public void setTotalValueEnabled(boolean enabled) {
    this.totalRadioButton.setEnabled(enabled);
    this.totalView.setEnabled(enabled);
  }

  @Override
  public void setPeriodValue(String value) {
    periodPrefixableTextView.setContent(value);
  }

  @Override
  public void setPeriodValueEnabled(boolean enabled) {
    this.periodRadioButton.setEnabled(enabled);
    this.periodView.setEnabled(enabled);
  }

  @Override
  public void setPaymentOptions(List<Product> paymentOptionList) {
    paymentMethodChooser.setPaymentMethodList(paymentOptionList);
  }

  @Override
  public void setOptionChecked(LoanBillBalance.Option option) {
    boolean isPeriodOption = false;
    boolean isCurrentOption = false;
    boolean isOtherOption = false;

    final String buttonText;

    if (option == LoanBillBalance.Option.PERIOD) {
      isPeriodOption = true;
      buttonText = "Pagar cuota";
    } else if (option == LoanBillBalance.Option.CURRENT) {
      isCurrentOption = true;
      buttonText = "Pagar a la fecha";
    } else {
      isOtherOption = true;
      buttonText = "Pagar otro monto";
    }

    this.periodRadioButton.setChecked(isPeriodOption);
    this.totalRadioButton.setChecked(isCurrentOption);

    this.otherAmountEditText.setEnabled(isOtherOption);
    this.otherAmountRadioButton.setChecked(isOtherOption);
    if (isOtherOption) {
      Keyboard.show(this.otherAmountEditText);
    } else {
      Keyboard.hide(this);
    }

    button.setText(buttonText);
  }

  @Override
  public void setPayButtonEnabled(boolean enabled) {
    UiUtil.setEnabled(button, enabled);
  }

  @Override
  public void requestPin(String partnerName, String value) {
    final int x = Math.round((button.getRight() - button.getLeft()) / 2);
    final int y = Math.round(button.getY() + ((button.getBottom() - button.getTop()) / 2));
    PinConfirmationDialogFragment.show(
      getChildFragmentManager(),
      getString(R.string.transaction_creation_bill_confirmation, value, partnerName),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(String pin) {
          presenter.onPinRequestFinished(paymentMethodChooser.getSelectedItem(), pin);
        }
      },
      x,
      y
    );
  }

  @Override
  public void setPaymentResult(boolean succeeded, String message) {
    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), succeeded);
    if (succeeded) {
      getContainer().finish(true, message);
    }
  }

  @Override
  public void showGenericErrorDialog(String message) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(message)
      .setPositiveButton(R.string.error_positive_button_text, null)
      .show();
  }

  @Override
  public void showGenericErrorDialog() {
    showGenericErrorDialog(getString(R.string.error_generic));
  }

  @Override
  public void showUnavailableNetworkError() {
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
      .show();
  }
}
