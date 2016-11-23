package com.gbh.movil.ui.main.payments.recipients;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.Container;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AddRecipientContainer extends Container<AddRecipientComponent> {
  /**
   * TODO
   *
   * @param contact
   *   TODO
   */
  void onContactClicked(@NonNull Contact contact);
}
