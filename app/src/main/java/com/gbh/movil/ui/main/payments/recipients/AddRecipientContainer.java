package com.gbh.movil.ui.main.payments.recipients;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.SwitchableContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AddRecipientContainer extends SwitchableContainer<AddRecipientComponent> {
  /**
   * TODO
   *
   * @param contact
   *   TODO
   */
  void onContactClicked(@NonNull Contact contact);
}
