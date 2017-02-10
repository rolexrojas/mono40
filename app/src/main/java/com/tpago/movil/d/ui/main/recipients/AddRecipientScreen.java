package com.tpago.movil.d.ui.main.recipients;

import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.Refreshable;
import com.tpago.movil.d.ui.Screen;

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
