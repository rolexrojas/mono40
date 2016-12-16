package com.gbh.movil.ui.auth.signup.two;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.ui.Screen;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface StepTwoScreen extends Screen {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> passwordChanges();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> passwordConfirmationChanges();

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setPasswordError(@Nullable String message);

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setPasswordConfirmationError(@Nullable String message);

  /**
   * TODO
   *
   * @param enabled
   *   TODO
   */
  void setSubmitButtonEnabled(boolean enabled);

  /**
   * TODO
   */
  void submit(boolean succeeded);
}
