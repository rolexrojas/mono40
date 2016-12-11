package com.gbh.movil.ui.auth.signup.one;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.ui.Screen;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface StepOneScreen extends Screen {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> phoneNumberChanges();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> emailChanges();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> emailConfirmationChanges();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Void> submitButtonClicks();

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setPhoneNumberError(@Nullable String message);

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setEmailError(@Nullable String message);

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setConfirmationError(@Nullable String message);

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
  void submit(@NonNull String phoneNumber, @NonNull String email);
}
