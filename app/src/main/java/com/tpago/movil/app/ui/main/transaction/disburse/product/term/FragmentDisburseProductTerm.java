package com.tpago.movil.app.ui.main.transaction.disburse.product.term;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.Label;
import com.tpago.movil.app.ui.NumPad;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.main.transaction.disburse.product.confirm.FragmentDisburseProductConfirm;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class FragmentDisburseProductTerm extends FragmentBase
  implements PresentationDisburseProductTerm {

  public static FragmentDisburseProductTerm create() {
    return new FragmentDisburseProductTerm();
  }

  @BindView(R.id.label_description) Label descriptionLabel;
  @BindView(R.id.label_term) Label termLabel;
  @BindView(R.id.num_pad) NumPad numPad;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject PresenterDisburseProductTerm presenter;

  private Consumer<Integer> digitConsumer;
  private Action deleteAction;

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    this.presenter.onSubmitButtonPressed();
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.fragment_disburse_product_term;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final FragmentComponentDisburseProductTerm component = ActivityToolbar.get(this.getContext())
      .retainedFragment()
      .componentBuilderSupplier()
      .get(
        FragmentDisburseProductTerm.class,
        FragmentComponentDisburseProductTerm.Builder.class
      )
      .term(FragmentModuleDisburseProductTerm.create(this))
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
  }

  @Override
  public void onPause() {
    // Unbinds the the num pad from the presenter.
    this.numPad.removeDeleteAction(this.deleteAction);
    this.deleteAction = null;
    this.numPad.removeDigitConsumer(this.digitConsumer);
    this.digitConsumer = null;

    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setDescription(String text) {
    this.descriptionLabel.setText(text);
  }

  @Override
  public void setTerm(String text) {
    this.termLabel.setText(text);
  }

  @Override
  public void moveToNextScreen() {
    this.fragmentReplacer.begin(FragmentDisburseProductConfirm.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack()
      .commit();
  }
}
