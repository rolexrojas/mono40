package com.tpago.movil.ui.auth.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.ui.Screen;

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
   * @param phoneNumber
   *   TODO
   */
  void setPhoneNumber(@Nullable String phoneNumber);

  /**
   * TODO
   *
   * @param enabled
   *   TODO
   */
  void setPhoneNumberEnabled(boolean enabled);

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
   * @param email
   *   TODO
   */
  void setEmail(@Nullable String email);

  /**
   * TODO
   *
   * @param enabled
   *   TODO
   */
  void setEmailEnabled(boolean enabled);

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

  void showAlreadyAssociatedDialog();
}
