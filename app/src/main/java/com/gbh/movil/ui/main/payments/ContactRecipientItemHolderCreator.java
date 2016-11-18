package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactRecipientItemHolderCreator implements ItemHolderCreator<ContactRecipientItemHolder> {
  @NonNull
  @Override
  public ContactRecipientItemHolder create(@NonNull ViewGroup parent) {
    return new ContactRecipientItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_recipient_contact, parent, false));
  }
}
