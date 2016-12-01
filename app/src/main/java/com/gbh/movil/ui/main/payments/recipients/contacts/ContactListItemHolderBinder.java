package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.main.list.ListItemHolderBinder;
import com.gbh.movil.ui.main.payments.recipients.Contact;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListItemHolderBinder implements ListItemHolderBinder<Contact, ContactListItemHolder> {
  @Override
  public void bind(@NonNull Contact item, @NonNull ContactListItemHolder holder) {
    // TODO: Load contact's picture.
    holder.nameTextView.setText(item.getName());
    holder.phoneNumberTextView.setText(item.getPhoneNumber().toString());
  }
}
