package com.tpago.movil.d.ui.main.recipients;

import android.support.annotation.NonNull;

import com.tpago.movil.d.ui.SwitchableContainer;

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
