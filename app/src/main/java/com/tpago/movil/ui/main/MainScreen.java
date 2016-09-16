package com.tpago.movil.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface MainScreen {
  /**
   * TODO
   */
  void showAccountsScreen();

  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void showMessage(@NonNull String message);

  /**
   * TODO
   *
   * @param title
   *   TODO
   * @param description
   *   TODO
   * @param positiveOptionText
   *   TODO
   * @param neutralOptionText
   *   TODO
   */
  void showDialog(@NonNull String title, @NonNull String description,
    @NonNull String positiveOptionText, @Nullable OnOptionClickedListener positiveOptionListener,
    @Nullable String neutralOptionText, @Nullable OnOptionClickedListener neutralOptionListener);

  /**
   * TODO
   */
  void showSplashScreen();

  /**
   * TODO
   */
  void hideSplashScreen();

  /**
   * TODO
   */
  interface OnOptionClickedListener {
    /**
     * TODO
     */
    void onClick();
  }
}
