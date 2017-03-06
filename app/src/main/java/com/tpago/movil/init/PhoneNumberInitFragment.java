package com.tpago.movil.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.Digit;
import com.tpago.movil.R;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.InformationalDialogFragment;
import com.tpago.movil.init.register.RegisterFragment;
import com.tpago.movil.init.signin.SignInFragment;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.NumPad;
import com.tpago.movil.widget.TextInput;

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
  implements PhoneNumberInitPresenter.View,
  NumPad.OnDigitClickedListener,
  NumPad.OnDeleteClickedListener {
  private Unbinder unbinder;
  private LoadIndicator loadIndicator;
  private PhoneNumberInitPresenter presenter;

  @BindView(R.id.text_input) TextInput textInput;
  @BindView(R.id.num_pad) NumPad numPad;
  @BindView(R.id.button_move_to_next_screen) Button nextButton;

  @Inject LogoAnimator logoAnimator;
  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  public static PhoneNumberInitFragment create() {
    return new PhoneNumberInitFragment();
  }

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
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_init_phone_number, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views, resources and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the load indicator.
    loadIndicator = new FullSizeLoadIndicator(getChildFragmentManager());
    // Creates presenter.
    presenter = new PhoneNumberInitPresenter(this, getInitComponent());
  }

  @Override
  public void onStart() {
    super.onStart();
    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(this);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(this);
    // Starts the presenter.
    presenter.onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Moves the logo out of the screen.
    logoAnimator.moveOutOfScreen();
    // Sets focus on the num pad text input.
    textInput.requestFocus();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.onViewStopped();
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(null);
    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(null);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Destroys the load indicator.
    loadIndicator = null;
    // Binds all annotated views, resources and methods.
    unbinder.unbind();
  }

  @Override
  public void showDialog(int titleId, String message, int positiveButtonTextId) {
    InformationalDialogFragment.create(getString(titleId), message, getString(positiveButtonTextId))
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showDialog(int titleId, int messageId, int positiveButtonTextId) {
    showDialog(titleId, getString(messageId), positiveButtonTextId);
  }

  @Override
  public void setTextInputContent(String text) {
    textInput.setText(text);
  }

  @Override
  public void showTextInputAsErratic(boolean showAsErratic) {
    textInput.setErraticStateEnabled(showAsErratic);
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
  public void startLoading() {
    loadIndicator.start();
  }

  @Override
  public void stopLoading() {
    loadIndicator.stop();
  }

  @Override
  public void moveToSignInScreen() {
    fragmentReplacer.begin(SignInFragment.create())
      .addToBackStack()
      .setTransition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @Override
  public void moveToSignUpScreen() {
    fragmentReplacer.begin(RegisterFragment.create())
      .addToBackStack()
      .setTransition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @Override
  public void onDigitClicked(Digit digit) {
    presenter.addDigit(digit);
  }

  @Override
  public void onDeleteClicked() {
    presenter.removeDigit();
  }
}
