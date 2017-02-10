package com.tpago.movil.d.ui.auth.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tpago.movil.d.App;
import com.tpago.movil.R;
import com.tpago.movil.d.ui.ActivityModule;
import com.tpago.movil.d.ui.BaseActivity;
import com.tpago.movil.d.ui.main.MainActivity;
import com.tpago.movil.d.ui.text.UiTextHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          presenter.submit();
        }
        return false;
      }
    });
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

  @OnClick(R.id.button_continue)
  void onSubmitButtonClicked() {
    presenter.submit();
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
    if (getCallingActivity() == null) {
      startActivity(MainActivity.getLaunchIntent(this));
    } else {
      setResult(RESULT_OK);
    }
    finish();
  }

  @Override
  public void showAlreadyAssociatedDialog() {
    new AlertDialog.Builder(this)
      .setTitle(R.string.dialog_title_already_associated_device)
      .setMessage(R.string.dialog_message_already_associated_device)
      .setNegativeButton(R.string.dialog_negative_text_already_associated_device, new Listener(false))
      .setPositiveButton(R.string.dialog_positive_text_already_associated_device, new Listener(true))
      .create()
      .show();
  }

  private class Listener implements DialogInterface.OnClickListener {
    private final boolean mustForce;

    Listener(boolean mustForce) {
      this.mustForce = mustForce;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      presenter.setMustForce(mustForce);
      presenter.submit();
    }
  }
}
