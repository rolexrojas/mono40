package com.tpago.movil.d.ui.main.recipient.addition;

import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.ui.SwitchableContainer;

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
