package com.gbh.movil.ui.main.payments;

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
class RecipientHolderCreator implements HolderCreator<RecipientHolder> {
  private final Holder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  RecipientHolderCreator(@NonNull Holder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public RecipientHolder create(@NonNull ViewGroup parent) {
    return new RecipientHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_recipient, parent, false), onClickListener);
  }
}
