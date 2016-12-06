package com.gbh.movil.ui.main.recipients;

import android.support.annotation.Nullable;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.Refreshable;
import com.gbh.movil.ui.Screen;

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
