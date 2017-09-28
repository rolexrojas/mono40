package com.tpago.movil.d.ui.main.recipient.addition;

import com.tpago.movil.Partner;
import com.tpago.movil.d.ui.SwitchableContainer;
import com.tpago.movil.d.domain.Bank;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AddRecipientContainer extends SwitchableContainer<AddRecipientComponent> {

  void onContactClicked(Contact contact);

  void onPartnerClicked(Partner partner);

  void onBankClicked(Bank bank);
}
