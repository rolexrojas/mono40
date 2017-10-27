package com.tpago.movil.d.ui.main.recipient.addition;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
class AddRecipientPresenter extends Presenter<AddRecipientScreen> {

  private final RecipientManager recipientManager;

  AddRecipientPresenter(RecipientManager recipientManager) {
    this.recipientManager = ObjectHelper.checkNotNull(recipientManager, "recipientManager");
  }

  final void add(@NonNull final Contact contact) {
    final Recipient recipient = new PhoneNumberRecipient(ObjectHelper.checkNotNull(contact, "contact"));
    this.recipientManager.add(recipient);
    this.screen.finish(recipient);
  }
}
