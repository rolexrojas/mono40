package com.tpago.movil.d.ui.main.recipients;

import com.tpago.movil.Partner;
import com.tpago.movil.d.ui.SwitchableContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AddRecipientContainer extends SwitchableContainer<AddRecipientComponent> {
  void onContactClicked(Contact contact);
  void onPartnerClicked(Partner partner);
}
