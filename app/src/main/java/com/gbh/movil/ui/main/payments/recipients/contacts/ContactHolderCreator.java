package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.Holder;
import com.gbh.movil.ui.main.list.HolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactHolderCreator implements HolderCreator<ContactHolder> {
  private final Holder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  ContactHolderCreator(@NonNull Holder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public ContactHolder create(@NonNull ViewGroup parent) {
    return new ContactHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_contact, parent, false), onClickListener);
  }
}
