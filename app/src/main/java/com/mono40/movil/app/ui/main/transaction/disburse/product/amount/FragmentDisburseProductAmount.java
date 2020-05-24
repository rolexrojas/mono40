package com.mono40.movil.app.ui.main.transaction.disburse.product.amount;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.Label;
import com.mono40.movil.app.ui.LabelPrefix;
import com.mono40.movil.app.ui.NumPad;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.transaction.disburse.product.term.FragmentDisburseProductTerm;
import com.mono40.movil.util.function.Action;
import com.mono40.movil.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class FragmentDisburseProductAmount extends FragmentBase
  implements PresentationDisburseProductAmount {

  public static FragmentDisburseProductAmount create() {
    return new FragmentDisburseProductAmount();
  }

  @BindView(R.id.label_balance) LabelPrefix balanceLabel;
  @BindView(R.id.image_bank_logo) ImageView bankLogoImage;
  @BindView(R.id.label_destination_product) Label destinationProductTypeAndNumberLabel;
  @BindView(R.id.label_amount) LabelPrefix amountLabel;
  @BindView(R.id.num_pad) NumPad numPad;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject PresenterDisburseProductAmount presenter;

  private Consumer<Integer> digitConsumer;
  private Action deleteAction;
  private Action dotAction;

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    this.presenter.onSubmitButtonPressed();
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.fragment_disburse_product_amount;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final FragmentComponentDisburseProductAmount component = ActivityToolbar.get(this.getContext())
      .retainedFragment()
      .componentBuilderSupplier()
      .get(
        FragmentDisburseProductAmount.class,
        FragmentComponentDisburseProductAmount.Builder.class
      )
      .amount(FragmentModuleDisburseProductAmount.create(this))
      .build();

    // Injects all annotated dependencies.
    component.inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Resumes the presenter.
    this.presenter.onPresentationResumed();

    // Binds the num pad to the presenter.
    this.digitConsumer = this.presenter::onDigitButtonPressed;
    this.numPad.addDigitConsumer(this.digitConsumer);
    this.deleteAction = this.presenter::onDeleteButtonPressed;
    this.numPad.addDeleteAction(this.deleteAction);
    this.dotAction = this.presenter::onDotButtonPressed;
    this.numPad.addDotAction(this.dotAction);
  }

  @Override
  public void onPause() {
    // Unbinds the the num pad from the presenter.
    this.numPad.removeDotAction(this.dotAction);
    this.dotAction = null;
    this.numPad.removeDeleteAction(this.deleteAction);
    this.deleteAction = null;
    this.numPad.removeDigitConsumer(this.digitConsumer);
    this.digitConsumer = null;

    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setCurrency(String text) {
    this.balanceLabel.setPrefix(text);
    this.amountLabel.setPrefix(text);
  }

  @Override
  public void setBalance(String text) {
    this.balanceLabel.setValue(text);
  }

  @Override
  public void setBankLogo(Uri uri) {
    Picasso.get()
      .load(uri)
      .resizeDimen(R.dimen.icon_size_20, R.dimen.icon_size_20)
      .noFade()
      .into(this.bankLogoImage);
  }

  @Override
  public void setDestinationProductTypeAndName(String text) {
    this.destinationProductTypeAndNumberLabel.setText(text);
  }

  @Override
  public void setAmount(String text) {
    this.amountLabel.setValue(text);
  }

  @Override
  public void moveToNextScreen() {
    this.fragmentReplacer.begin(FragmentDisburseProductTerm.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack()
      .commit();
  }
}
