package com.tpago.movil.app.ui.main.transaction.paypal.confirm;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.tpago.movil.Code;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.LabelPrefix;
import com.tpago.movil.app.ui.NumPad;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public final class PayPalTransactionConfirmFragment extends FragmentBase
  implements PayPalTransactionConfirmPresentation {

  public static PayPalTransactionConfirmFragment create() {
    return new PayPalTransactionConfirmFragment();
  }

  @BindView(R.id.label_amount) LabelPrefix amountLabel;
  @BindView(R.id.num_pad) NumPad numPad;
  @BindView(R.id.payment_method_chooser) PaymentMethodChooser paymentMethodChooser;

  @Inject PayPalTransactionConfirmPresenter presenter;

  private Consumer<Integer> digitConsumer;
  private Action dotAction;
  private Action deleteAction;

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    this.presenter.onSubmitPressed();
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.fragment_pay_pal_transaction_confirm;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final PayPalTransactionConfirmComponent component = ActivityToolbar.get(this.getContext())
      .retainedFragment()
      .componentBuilderSupplier()
      .get(
        PayPalTransactionConfirmFragment.class,
        PayPalTransactionConfirmComponent.Builder.class
      )
      .payPalTransactionConfirmModule(PayPalTransactionConfirmModule.create(this))
      .build();

    // Injects all annotated dependencies.
    component.inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.presenter.onPresentationResumed();

    this.paymentMethodChooser.setOnPaymentMethodChosenListener(this.presenter::onPaymentMethodChanged);

    this.digitConsumer = this.presenter::onNumPadDigitPressed;
    this.numPad.addDigitConsumer(this.digitConsumer);
    this.dotAction = this.presenter::onNumPadDotPressed;
    this.numPad.addDotAction(this.dotAction);
    this.deleteAction = this.presenter::onNumPadDeletePressed;
    this.numPad.addDeleteAction(this.deleteAction);
  }

  @Override
  public void onPause() {
    this.numPad.removeDeleteAction(this.deleteAction);
    this.deleteAction = null;
    this.numPad.removeDotAction(this.dotAction);
    this.dotAction = null;
    this.numPad.removeDigitConsumer(this.digitConsumer);
    this.digitConsumer = null;

    this.paymentMethodChooser.setOnPaymentMethodChosenListener(null);

    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setLogo(Uri uri) {
    // TODO
  }

  @Override
  public void setAccountAlias(String text) {
    ActivityToolbar.get(this.getContext())
      .toolbarManager()
      .setTitleText(String.format("Recargar %1$s", text));
  }

  @Override
  public void setAccountCurrency(String text) {
    this.amountLabel.setPrefix(text);
  }

  @Override
  public void setPaymentMethods(List<Product> paymentMethods) {
    this.paymentMethodChooser.setPaymentMethodList(paymentMethods);
  }

  @Override
  public void setAmount(String text) {
    this.amountLabel.setValue(text);
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
          this.presenter.onPin(Code.create(pin));
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
