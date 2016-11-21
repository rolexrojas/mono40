package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Contact;
import com.gbh.movil.ui.main.list.ItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactRecipientItemHolderBinder
  implements ItemHolderBinder<ContactRecipientItem, ContactRecipientItemHolder> {
  @Override
  public void bind(@NonNull ContactRecipientItem item,
    @NonNull ContactRecipientItemHolder holder) {
    final Contact contact = item.getRecipient().getContact();
    // TODO: Load contact's picture.
    holder.contactNameTextView.setText(contact.getName());
    holder.contactPhoneNumberTextView.setText(contact.getPhoneNumber().toString());
  }
}
