package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.ParentScreen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface MainScreen extends ParentScreen<MainComponent> {
  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void showAccountAdditionOrRemovalNotification(@NonNull String message);
}
