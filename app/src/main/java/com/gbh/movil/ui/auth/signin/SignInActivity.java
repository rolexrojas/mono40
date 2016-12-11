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
import com.gbh.movil.ui.text.UiTextHelper;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import timber.log.Timber;

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
  @BindView(R.id.button_sign_in)
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
  public Observable<String> afterPhoneNumberChanged() {
    return UiTextHelper.afterTextChanges(phoneNumberEditText);
  }

  @NonNull
  @Override
  public Observable<String> afterEmailChanged() {
    return UiTextHelper.afterTextChanges(emailEditText);
  }

  @NonNull
  @Override
  public Observable<String> afterPasswordChanged() {
    return UiTextHelper.afterTextChanges(passwordEditText);
  }

  @NonNull
  @Override
  public Observable<Void> onSignInButtonClicked() {
    return RxView.clicks(signInButton);
  }

  @Override
  public void setPhoneNumberError(@Nullable String error) {
    // TODO
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
  public void setSignInButtonEnabled(boolean enabled) {
    signInButton.setEnabled(enabled);
  }
}
