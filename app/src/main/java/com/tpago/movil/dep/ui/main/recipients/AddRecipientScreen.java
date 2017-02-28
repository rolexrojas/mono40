package com.tpago.movil.dep.ui.main.recipients;

import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.Refreshable;
import com.tpago.movil.dep.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface AddRecipientScreen extends Screen, Refreshable {
  /**
   * TODO
   */
  void showNotSupportedOperationMessage();

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void finish(@Nullable Recipient recipient);
}
