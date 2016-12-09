package com.gbh.movil.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.R;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.auth.signin.SignInActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AuthIndexActivity extends BaseActivity {
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
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @OnClick(R.id.button_sign_in)
  void onSignInButtonClicked() {
    startActivity(SignInActivity.getLaunchIntent(this));
  }

  @OnClick(R.id.button_sign_up)
  void onSignUpButtonClicked() {
    // TODO
  }
}
