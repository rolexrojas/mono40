package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface MainScreen extends Screen {
  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void showAccountAdditionOrRemovalNotification(@NonNull String message);
}
