package com.tpago.movil.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.Digit;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiBridge;
import com.tpago.movil.app.InformationalDialogFragment;
import com.tpago.movil.content.StringResolver;
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
public final class PhoneNumberValidationFragment
  extends ChildRegistrationFragment
  implements PhoneNumberValidationPresenter.View,
  NumPad.OnDigitClickedListener,
  NumPad.OnDeleteClickedListener {
  private Unbinder unbinder;
  private PhoneNumberValidationPresenter presenter;

  @BindView(R.id.text_input)
  TextInput textInput;
  @BindView(R.id.num_pad)
  NumPad numPad;
  @BindView(R.id.button_next)
  Button nextButton;

  @Inject
  ApiBridge apiBridge;
  @Inject
  RegistrationData data;
  @Inject
  StringResolver stringResolver;

  public static PhoneNumberValidationFragment create() {
    return new PhoneNumberValidationFragment();
  }

  @OnClick(R.id.button_next)
  void onNextButtonClicked() {
    presenter.validate();
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_phone_number_validation, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views, resources and methods.
    unbinder = ButterKnife.bind(this, view);
    // Injects all annotated dependencies.
    getRegistrationComponent().inject(this);
    // Creates presenter.
    presenter = new PhoneNumberValidationPresenter(apiBridge, data, stringResolver);
    presenter.setView(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(this);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Sets focus on the num pad text input.
    textInput.requestFocus();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(null);
    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(null);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter.setView(null);
    presenter = null;
    // Binds all annotated views, resources and methods.
    unbinder.unbind();
  }

  @Override
  public void setText(String text) {
    textInput.setText(text);
  }

  @Override
  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    textInput.setErraticStateEnabled(erraticStateEnabled);
  }

  @Override
  public void showNextButtonAsEnabled(boolean enabled) {
    nextButton.setAlpha(enabled ? 1.0F : 0.5F);
  }

  @Override
  public void moveToNextScreen() {
    // TODO
  }

  @Override
  public void showError(String title, String message, String positiveButtonText) {
    final DialogFragment fragment = InformationalDialogFragment.create(
      title,
      message,
      positiveButtonText);
    fragment.setTargetFragment(this, 0);
    fragment.show(getChildFragmentManager(), null);
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
