package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.d.ui.main.recipient.addition.Contact;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListItemHolderBinder implements ListItemHolderBinder<Contact, ContactListItemHolder> {
  @Override
  public void bind(@NonNull Contact item, @NonNull ContactListItemHolder holder) {
    Picasso.with(holder.getContext())
      .load(item.getPictureUri())
      .into(holder.pictureImageView);
    holder.nameTextView.setText(item.getName());
    holder.phoneNumberTextView.setText(PhoneNumber.format(item.getPhoneNumber().toString()));
  }
}
