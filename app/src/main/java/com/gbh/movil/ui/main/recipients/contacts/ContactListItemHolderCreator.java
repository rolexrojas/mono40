package com.gbh.movil.ui.main.recipients.contacts;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;
import com.gbh.movil.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListItemHolderCreator implements ListItemHolderCreator<ContactListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  ContactListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public ContactListItemHolder create(@NonNull ViewGroup parent) {
    return new ContactListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_contact, parent, false), onClickListener);
  }
}
