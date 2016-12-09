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
  Observable<String> afterPhoneNumberChanged();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> afterEmailChanged();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<String> afterPasswordChanged();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Void> onSignInButtonClicked();

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
  void setSignInButtonEnabled(boolean enabled);
}
