package com.tpago.movil.ui.main.recipients;

import android.support.annotation.Nullable;

import com.tpago.movil.domain.Recipient;
import com.tpago.movil.ui.Refreshable;
import com.tpago.movil.ui.Screen;

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
