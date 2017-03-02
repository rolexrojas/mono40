package com.tpago.movil.dep.ui.main.recipients;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.ui.SwitchableContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AddRecipientContainer extends SwitchableContainer<AddRecipientComponent> {
  void onContactClicked(Contact contact);
  void onPartnerClicked(Partner partner);
}
