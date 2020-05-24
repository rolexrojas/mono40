package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.confirm;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.mono40.movil.Code;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.Label;
import com.mono40.movil.app.ui.LabelPrefix;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexFragment;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.mono40.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.ui.main.PinConfirmationDialogFragment;
import com.mono40.movil.dep.main.transactions.PaymentMethodChooser;
import com.mono40.movil.transaction.TransactionSummary;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public final class MicroInsurancePurchaseConfirmFragment extends FragmentBase
  implements MicroInsurancePurchaseConfirmPresentation {

  public static MicroInsurancePurchaseConfirmFragment create() {
    return new MicroInsurancePurchaseConfirmFragment();
  }

  private MicroInsurancePurchaseConfirmPresenter presenter;

  @BindView(R.id.label_coverage) LabelPrefix coverageLabel;
  @BindView(R.id.label_term) Label termLabel;
  @BindView(R.id.total) LabelPrefix totalPayment;
  @BindView(R.id.payment_method_chooser) PaymentMethodChooser paymentMethodChooser;

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    this.presenter.onSubmitButtonPressed();
  }

  @Override
  protected int layoutResId() {
    return R.layout.main_transaction_insurance_micro_purchase_confirm;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Instantiates the presenter.
    final MicroInsurancePurchaseComponent component = MicroInsurancePurchaseActivity
      .get(this.getContext())
      .component();
    this.presenter = MicroInsurancePurchaseConfirmPresenter
      .create(this, component);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Binds the presenter to the payment method chooser.
    this.paymentMethodChooser
      .setOnPaymentMethodChosenListener(this.presenter::onPaymentMethodSelected);

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    // Unbinds the presenter from the payment method chooser.
    this.paymentMethodChooser.setOnPaymentMethodChosenListener(null);

    this.paymentMethodChooser.onPause();
    super.onPause();
  }

  @Override
  public void setCurrency(String text) {
    final String prefix = "$ ";
    this.coverageLabel.setPrefix(text+prefix);
    this.totalPayment.setPrefix(text+prefix);
  }

  @Override
  public void setCoverage(String text) {
    this.coverageLabel.setValue(text);
  }

  @Override
  public void setPremium(String text) {
  }

  @Override
  public void setTerm(String text) {
    this.termLabel.setText(text);
  }

  @Override
  public void setDueDate(String text) {
  }

  @Override
  public void setTotal(String text) {
    this.totalPayment.setValue(text);
  }

  @Override
  public void setPaymentMethods(List<Product> paymentMethods) {
    this.paymentMethodChooser.setPaymentMethodList(paymentMethods);
  }

  @Override
  public void requestPin(String text) {
    final FragmentManager manager = this.getChildFragmentManager();
    PinConfirmationDialogFragment
      .show(
        this.getChildFragmentManager(),
        text,
        (pin) -> {
          PinConfirmationDialogFragment.dismiss(manager, true);
          this.presenter.onPinInputted(Code.create(pin));
        },
        0,
        0
      );
  }

  @Override
  public void finish(TransactionSummary summary) {
    final Activity activity = MicroInsurancePurchaseActivity.get(this.getContext());
    activity.setResult(Activity.RESULT_OK, TransactionSummaryUtil.wrap(summary));
    activity.finish();
  }
}
