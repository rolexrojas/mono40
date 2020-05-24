package com.mono40.movil.dep.init.register;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mono40.movil.util.function.Action;
import com.mono40.movil.util.function.Consumer;
import com.mono40.movil.util.digit.Digit;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.dep.widget.EditableLabel;
import com.mono40.movil.app.ui.DNumPad;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class PinRegisterFormFragment extends BaseRegisterFragment
  implements PinRegisterFormPresenter.View {

  private Unbinder unbinder;
  private PinRegisterFormPresenter presenter;

  @Inject
  @FragmentQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(R.id.editable_label_pin) EditableLabel pinEditableLabel;
  @BindView(R.id.num_pad) DNumPad DNumPad;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  static PinRegisterFormFragment create() {
    return new PinRegisterFormFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all the annotated dependencies.
    getRegisterComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_register_form_pin, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the presenter.
    presenter = new PinRegisterFormPresenter(this, getRegisterComponent());
  }

  @Override
  public void onStart() {
    super.onStart();
    // Notifies the presenter that the view its being started.
    presenter.onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    this.numPadDigitConsumer = this::onDigitClicked;
    this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    this.numPadDeleteAction = this::onDeleteClicked;
    this.DNumPad.addDeleteAction(this.numPadDeleteAction);
  }

  @Override
  public void onPause() {
    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;

    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Notifies the presenter that the view its being stopped.
    presenter.onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }

  public final void onDigitClicked(@Digit int digit) {
    presenter.onDigitButtonClicked(digit);
  }

  public final void onDeleteClicked() {
    presenter.onDeleteButtonClicked();
  }

  @Override
  public void setTextInputContent(String content) {
    pinEditableLabel.setText(content);
  }

  @Override
  public void moveToNextScreen() {
    fragmentReplacer.begin(SummaryRegisterFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }
}
