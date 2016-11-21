package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.ContactRecipient;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactRecipientItem extends RecipientItem<ContactRecipient> {
  ContactRecipientItem(@NonNull ContactRecipient recipient) {
    super(recipient);
  }

  @Override
  public String toString() {
    return ContactRecipientItem.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
