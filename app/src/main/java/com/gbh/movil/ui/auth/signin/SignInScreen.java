package com.gbh.movil.ui.auth.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.ui.Screen;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface SignInScreen extends Screen {
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
  Observable<String> passwordChanges();

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
   * @param error
   *   TODO
   */
  void setPhoneNumberError(@Nullable String error);

  /**
   * TODO
   *
   * @param error
   *   TODO
   */
  void setEmailError(@Nullable String error);

  /**
   * TODO
   *
   * @param error
   *   TODO
   */
  void setPasswordError(@Nullable String error);

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
  void submit();
}
