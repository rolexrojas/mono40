package com.gbh.movil.ui.auth.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.R;
import com.gbh.movil.ui.BaseActivity;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SignInActivity extends BaseActivity implements SignInScreen {
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
    // Sets the content layout identifier.
    setContentView(R.layout.screen_sign_in);
  }
}
