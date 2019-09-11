package com.tpago.movil.dep.init.register;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.DNumPad;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.widget.EditableLabel;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class EmailOTPRegisterFormFragment extends BaseRegisterFragment
  implements EmailOTPRegisterFormPresenter.View {

  private Unbinder unbinder;
  private EmailOTPRegisterFormPresenter presenter;

  @Inject
  @FragmentQualifier FragmentReplacer fragmentReplacer;
  @Inject RegisterData registerData;
  @Inject Api api;
  @Inject AlertManager alertManager;

  @BindView(R.id.editable_label_otp_pin) EditableLabel pinEditableLabel;
  @BindView(R.id.num_pad) DNumPad DNumPad;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  public static EmailOTPRegisterFormFragment create() {
    return new EmailOTPRegisterFormFragment();
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
    return inflater.inflate(R.layout.fragment_register_form_email_code, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the presenter.
    presenter = new EmailOTPRegisterFormPresenter(this, getRegisterComponent());
    this.presenter.requestVerificationCode();
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

  @OnClick(R.id.sendcodeagain)
  void onSendCodeAgainClicked(){
    this.presenter.requestVerificationCode();
  }

  @Override
  public void setTextInputContent(String content) {
    pinEditableLabel.setText(content);
  }

  @Override
  public void moveToNextScreen() {
    fragmentReplacer.begin(PasswordRegisterFormFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }
}
