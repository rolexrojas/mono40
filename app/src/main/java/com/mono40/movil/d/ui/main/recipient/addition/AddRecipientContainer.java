package com.mono40.movil.d.ui.main.recipient.addition;

import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.ui.SwitchableContainer;

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
