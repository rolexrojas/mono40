package com.tpago.movil.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.ui.BaseActivity;
import com.tpago.movil.ui.auth.signin.SignInActivity;
import com.tpago.movil.ui.auth.signup.SignUpActivity;
import com.tpago.movil.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AuthIndexActivity extends BaseActivity {
  private static final int REQUEST_CODE_SIGN_IN = 1;
  private static final int REQUEST_CODE_SIGN_UP = 2;

  private Unbinder unbinder;

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
    return new Intent(context, AuthIndexActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the content layout identifier.
    setContentView(R.layout.screen_auth_index);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_SIGN_IN || requestCode == REQUEST_CODE_SIGN_UP) {
      if (resultCode == RESULT_OK) {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @OnClick(R.id.button_continue)
  void onSignInButtonClicked() {
    startActivityForResult(SignInActivity.getLaunchIntent(this), REQUEST_CODE_SIGN_IN);
  }

  @OnClick(R.id.button_sign_up)
  void onSignUpButtonClicked() {
    startActivityForResult(SignUpActivity.getLaunchIntent(this), REQUEST_CODE_SIGN_UP);
  }
}
