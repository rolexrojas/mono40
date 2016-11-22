package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.gbh.movil.Utils;
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
    final String contactName = contact.getName();
    final String contactPhoneNumber = contact.getPhoneNumber().toString();
    if (Utils.isNotNull(contactName)) {
      holder.contactNameTextView.setText(contactName);
      holder.contactNameTextView.setGravity(Gravity.BOTTOM);
      holder.contactPhoneNumberTextView.setVisibility(View.VISIBLE);
      holder.contactPhoneNumberTextView.setText(contactPhoneNumber);
    } else {
      holder.contactNameTextView.setText(contactPhoneNumber);
      holder.contactNameTextView.setGravity(Gravity.CENTER_VERTICAL);
      holder.contactPhoneNumberTextView.setText(null);
      holder.contactPhoneNumberTextView.setVisibility(View.GONE);
    }
  }
}
