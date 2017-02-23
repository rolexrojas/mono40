package com.tpago.movil.ui.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.Digit;
import com.tpago.movil.R;
import com.tpago.movil.ui.widget.NumPad;
import com.tpago.movil.ui.widget.TextInput;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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

  @Inject
  RegistrationData data;

  public static PhoneNumberValidationFragment create() {
    return new PhoneNumberValidationFragment();
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
    presenter = new PhoneNumberValidationPresenter(data);
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
  public void onDigitClicked(Digit digit) {
    presenter.addDigit(digit);
  }

  @Override
  public void onDeleteClicked() {
    presenter.removeDigit();
  }
}
