package com.gbh.movil.ui.auth.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.main.MainActivity;
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
public class SignInActivity extends BaseActivity implements SignInScreen {
  private Unbinder unbinder;

  @Inject
  SignInPresenter presenter;

  @BindView(R.id.edit_text_phone_number)
  EditText phoneNumberEditText;
  @BindView(R.id.edit_text_email)
  EditText emailEditText;
  @BindView(R.id.edit_text_password)
  EditText passwordEditText;
  @BindView(R.id.button_continue)
  Button signInButton;

  /**
   * TODO
   *
   * @param context
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, SignInActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final SignInComponent component = DaggerSignInComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .activityModule(new ActivityModule(this))
      .build();
    component.inject(this);
    // Sets the content layout identifier.
    setContentView(R.layout.screen_sign_in);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
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
  public Observable<String> passwordChanges() {
    return UiTextHelper.textChanges(passwordEditText);
  }

  @NonNull
  @Override
  public Observable<Void> submitButtonClicks() {
    return RxView.clicks(signInButton);
  }

  @Override
  public void setPhoneNumber(@Nullable String phoneNumber) {
    phoneNumberEditText.setText(phoneNumber);
  }

  @Override
  public void setPhoneNumberEnabled(boolean enabled) {
    phoneNumberEditText.setEnabled(enabled);
  }

  @Override
  public void setPhoneNumberError(@Nullable String error) {
    // TODO
  }

  @Override
  public void setEmail(@Nullable String email) {
    emailEditText.setText(email);
  }

  @Override
  public void setEmailEnabled(boolean enabled) {
    emailEditText.setEnabled(enabled);
  }

  @Override
  public void setEmailError(@Nullable String error) {
    // TODO
  }

  @Override
  public void setPasswordError(@Nullable String error) {
    // TODO
  }

  @Override
  public void setSubmitButtonEnabled(boolean enabled) {
    signInButton.setEnabled(enabled);
  }

  @Override
  public void submit() {
    startActivity(MainActivity.getLaunchIntent(this));
    finish();
  }
}
