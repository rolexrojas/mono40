package com.tpago.movil.dep.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.dep.init.capture.CaptureFragment;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.widget.EditableLabel;
import com.tpago.movil.app.ui.DNumPad;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class PhoneNumberInitFragment
  extends BaseInitFragment
  implements PhoneNumberInitPresenter.View {

  public static PhoneNumberInitFragment create() {
    return new PhoneNumberInitFragment();
  }

  private Unbinder unbinder;
  private PhoneNumberInitPresenter presenter;

  @BindView(R.id.editable_label_phone_number) EditableLabel phoneNumberEditableLabel;
  @BindView(R.id.num_pad) DNumPad DNumPad;
  @BindView(R.id.button_move_to_next_screen) Button nextButton;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;
  @Inject LogoAnimator logoAnimator;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  @OnClick(R.id.button_move_to_next_screen)
  void onNextButtonClicked() {
    presenter.validate();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getInitComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_init_phone_number, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views, resources and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates presenter.
    presenter = new PhoneNumberInitPresenter(this, getInitComponent());
  }

  @Override
  public void onResume() {
    super.onResume();

    // Moves the logo out of the screen.
    logoAnimator.moveOutOfScreen();

    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    this.numPadDigitConsumer = this::onDigitClicked;
    this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    this.numPadDeleteAction = this::onDeleteClicked;
    this.DNumPad.addDeleteAction(this.numPadDeleteAction);

    // Starts the presenter.
    presenter.onViewStarted();
  }

  @Override
  public void onPause() {
    // Stops the presenter.
    presenter.onViewStopped();

    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;

    super.onPause();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Binds all annotated views, resources and methods.
    unbinder.unbind();
  }

  @Override
  public void setTextInputContent(String text) {
    phoneNumberEditableLabel.setText(text);
  }

  @Override
  public void showTextInputAsErratic(boolean showAsErratic) {
    phoneNumberEditableLabel.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setNextButtonEnabled(boolean enabled) {
    nextButton.setEnabled(enabled);
  }

  @Override
  public void showNextButtonAsEnabled(boolean showAsEnabled) {
    nextButton.setAlpha(showAsEnabled ? 1.0F : 0.5F);
  }

  @Override
  public void moveToOneTimePasswordScreen(boolean shouldMoveToSignUpScreen) {
    Bundle bundle = new Bundle();
    bundle.putBoolean("shouldMoveToSignUpScreen", shouldMoveToSignUpScreen);

    final PhoneNumber phoneNumber = PhoneNumber.create(phoneNumberEditableLabel.getText().toString());
    bundle.putString("msisdn", phoneNumber.value());

    OneTimePasswordFragment otpFragment = OneTimePasswordFragment.create();
    otpFragment.setArguments(bundle);

    fragmentReplacer.begin(otpFragment)
        .addToBackStack()
        .transition(FragmentReplacer.Transition.SRFO)
        .commit();
  }

  @Override
  public void moveToCaptureScreen() {
    Bundle bundle = new Bundle();
    final PhoneNumber phoneNumber = PhoneNumber.create(phoneNumberEditableLabel.getText().toString());
    bundle.putString("msisdn", phoneNumber.value());

    CaptureFragment captureFragment = CaptureFragment.create();
    captureFragment.setArguments(bundle);

    fragmentReplacer.begin(captureFragment)
        .addToBackStack()
        .transition(FragmentReplacer.Transition.SRFO)
        .commit();
  }

  public final void onDigitClicked(@Digit int digit) {
    presenter.addDigit(digit);
  }

  public final void onDeleteClicked() {
    presenter.removeDigit();
  }
}
