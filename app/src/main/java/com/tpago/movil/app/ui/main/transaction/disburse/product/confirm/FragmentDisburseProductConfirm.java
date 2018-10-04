package com.tpago.movil.app.ui.main.transaction.disburse.product.confirm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;

import com.tpago.movil.Code;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.Label;
import com.tpago.movil.app.ui.LabelPrefix;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.transaction.TransactionSummary;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class FragmentDisburseProductConfirm extends FragmentBase
  implements PresentationDisburseProductConfirm {

  public static FragmentDisburseProductConfirm create() {
    return new FragmentDisburseProductConfirm();
  }

  @BindView(R.id.label_amount) LabelPrefix amountLabel;
  @BindView(R.id.label_term) Label termLabel;
  @BindView(R.id.label_rate) LabelPrefix rateLabel;
  @BindView(R.id.label_insurance) LabelPrefix insuranceLabel;
  @BindView(R.id.label_balance) LabelPrefix balanceLabel;
  @BindView(R.id.label_fee) LabelPrefix feeLabel;

  @BindView(R.id.radio_button_accept) RadioButton acceptRadioButton;
  @BindView(R.id.radio_button_decline) RadioButton declineRadioButton;

  @Inject AlertManager alertManager;

  @Inject PresenterDisburseProductConfirm presenter;

  @OnClick(R.id.radio_button_accept)
  final void onAcceptRadioButtonPressed() {
    this.acceptRadioButton.setChecked(true);
    this.declineRadioButton.setChecked(false);
  }

  @OnClick(R.id.radio_button_decline)
  final void onDeclineRadioButtonPressed() {
    this.acceptRadioButton.setChecked(false);
    this.declineRadioButton.setChecked(true);
  }

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    if (this.acceptRadioButton.isChecked()) {
      this.presenter.onSubmitButtonPressed();
    } else {
      this.alertManager.builder()
        .title("Términos")
        .message("Para realizar un desembolso debes aceptar los términos y condiciones.")
        .show();
    }
  }

  @OnClick(R.id.label_terms_conditions)
  final void onTermsConditionsLabelPressed() {
    String disbursementType = this.presenter.getDisbursementType().toLowerCase();
    String URL = "";

    if (Bank.Transaction.Type.EXTRA_CREDIT.toLowerCase().equals(disbursementType)) {
      URL = getString(R.string.termsconditionsxtra);
    } else if(Bank.Transaction.Type.SALARY_ADVANCE.toLowerCase().equals(disbursementType)) {
      URL = getString(R.string.termsconditionsasue);
    } else {
      URL = getString(R.string.popularenlineaurl);
    }

    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
    startActivity(i);
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.fragment_disburse_product_confirm;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final FragmentComponentDisburseProductConfirm component = ActivityToolbar.get(this.getContext())
      .retainedFragment()
      .componentBuilderSupplier()
      .get(
        FragmentDisburseProductConfirm.class,
        FragmentComponentDisburseProductConfirm.Builder.class
      )
      .confirm(FragmentModuleDisburseProductConfirm.create(this))
      .build();

    // Injects all annotated dependencies.
    component.inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setCurrency(String text) {
    this.amountLabel.setPrefix(text);
    this.insuranceLabel.setPrefix(text);
    this.balanceLabel.setPrefix(text);
    this.feeLabel.setPrefix(text);
  }

  @Override
  public void setAmount(String text) {
    this.amountLabel.setValue(text);
  }

  @Override
  public void setTerm(String text) {
    this.termLabel.setText(text);
  }

  @Override
  public void setRate(String text) {
    this.rateLabel.setValue(text);
  }

  @Override
  public void setInsurance(String text) {
    this.insuranceLabel.setValue(text);
  }

  @Override
  public void setBalance(String text) {
    this.balanceLabel.setValue(text);
  }

  @Override
  public void setFee(String text) {
    this.feeLabel.setValue(text);
  }

  @Deprecated
  @Override
  public void requestPin(String text) {
    final FragmentManager manager = this.getChildFragmentManager();
    PinConfirmationDialogFragment
      .show(
        this.getChildFragmentManager(),
        text,
        (pin) -> {
          PinConfirmationDialogFragment.dismiss(manager, true);
          this.presenter.consumePin(Code.create(pin));
        },
        0,
        0
      );
  }

  @Override
  public void finish(TransactionSummary summary) {
    final ActivityToolbar activity = ActivityToolbar.get(this.getContext());
    activity.setResult(ActivityToolbar.RESULT_OK, TransactionSummaryUtil.wrap(summary));
    activity.finish();
  }
}
