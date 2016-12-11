package com.gbh.movil.ui.auth.signup.one;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gbh.movil.R;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.auth.signup.SignUpContainer;
import com.gbh.movil.ui.auth.signup.two.StepTwoFragment;
import com.gbh.movil.ui.text.UiTextHelper;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public class StepOneFragment extends ChildFragment<SignUpContainer> implements StepOneScreen {
  private Unbinder unbinder;

  @Inject
  StepOnePresenter presenter;

  @BindView(R.id.edit_text_phone_number)
  EditText phoneNumberEditText;
  @BindView(R.id.edit_text_email)
  EditText emailEditText;
  @BindView(R.id.edit_text_email_confirmation)
  EditText emailConfirmationEditText;
  @BindView(R.id.button_continue)
  Button submitButton;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static StepOneFragment newInstance() {
    return new StepOneFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final StepOneComponent component = DaggerStepOneComponent.builder()
      .signUpComponent(getContainer().getComponent())
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.screen_sign_up_step_one, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @NonNull
  @Override
  public Observable<String> phoneNumberChanges() {
    return UiTextHelper.textChanges(phoneNumberEditText);
  }

  @NonNull
  @Override
  public Observable<String> emailChanges() {
    return UiTextHelper.textChanges(emailEditText);
  }

  @NonNull
  @Override
  public Observable<String> emailConfirmationChanges() {
    return UiTextHelper.textChanges(emailConfirmationEditText);
  }

  @NonNull
  @Override
  public Observable<Void> submitButtonClicks() {
    return RxView.clicks(submitButton);
  }

  @Override
  public void setPhoneNumberError(@Nullable String message) {
    // TODO
  }

  @Override
  public void setEmailError(@Nullable String message) {
    // TODO
  }

  @Override
  public void setConfirmationError(@Nullable String message) {
    // TODO
  }

  @Override
  public void setSubmitButtonEnabled(boolean enabled) {
    submitButton.setEnabled(enabled);
  }

  @Override
  public void submit(@NonNull String phoneNumber, @NonNull String email) {
    getContainer().setChildFragment(StepTwoFragment.newInstance(phoneNumber, email), true, true);
  }
}
