package com.tpago.movil.app.ui.main.code;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.InjectableDialogFragment;
import com.tpago.movil.app.ui.NumPad;
import com.tpago.movil.util.Action;

import java.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class CodeCreatorDialogFragment extends InjectableDialogFragment
  implements CodeCreatorPresentation {

  static CodeCreatorDialogFragment create() {
    return new CodeCreatorDialogFragment();
  }

  @BindView(R.id.titleTextView) TextView titleTextView;
  @BindView(R.id.subtitleTextView) TextView subtitleTextView;
  @BindView(R.id.valueTextView) TextView valueTextView;
  @BindView(R.id.numPad) NumPad numPad;

  @Inject CodeCreatorPresenter presenter;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.code_creator;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CodeCreatorTheme);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.parentComponentBuilderSupplier
      .get(CodeCreatorDialogFragment.class, CodeCreatorPresentationComponent.Builder.class)
      .codeCreatorModule(CodeCreatorPresentationModule.create(this))
      .build()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.numPadDigitConsumer = this.presenter::onNumPadDigitButtonClicked;
    this.numPad.addDigitConsumer(this.numPadDigitConsumer);
    this.numPadDeleteAction = this.presenter::onNumPadDeleteButtonClicked;
    this.numPad.addDeleteAction(this.numPadDeleteAction);

    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    this.presenter.onPresentationPaused();

    this.numPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    this.numPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;

    super.onPause();
  }

  @Override
  public void setTitle(String text) {
    this.titleTextView.setText(text);
  }

  @Override
  public void setSubtitle(String text) {
    this.subtitleTextView.setText(text);
  }

  @Override
  public void setValue(String text) {
    this.valueTextView.setText(text);
  }
}
