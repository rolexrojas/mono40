package com.tpago.movil.ui.auth.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.App;
import com.tpago.movil.R;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.SwitchableContainerActivity;
import com.tpago.movil.ui.auth.signup.one.StepOneFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SignUpActivity extends SwitchableContainerActivity<SignUpComponent>
  implements SignUpContainer {
  private SignUpComponent component;

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
    return new Intent(context, SignUpActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    component = DaggerSignUpComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .activityModule(new ActivityModule(this))
      .build();
    component.inject(this);
    // Sets the content layout identifier.
    setContentView(R.layout.screen_sign_up);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Sets the startup screen.
    setChildFragment(StepOneFragment.newInstance(), false, false);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Nullable
  @Override
  public SignUpComponent getComponent() {
    return component;
  }
}
