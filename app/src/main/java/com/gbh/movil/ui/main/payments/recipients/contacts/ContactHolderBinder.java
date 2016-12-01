package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.main.list.HolderBinder;
import com.gbh.movil.ui.main.payments.recipients.Contact;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactHolderBinder implements HolderBinder<Contact, ContactHolder> {
  @Override
  public void bind(@NonNull Contact item, @NonNull ContactHolder holder) {
    // TODO: Load contact's picture.
    holder.nameTextView.setText(item.getName());
    holder.phoneNumberTextView.setText(item.getPhoneNumber().toString());
  }
}
