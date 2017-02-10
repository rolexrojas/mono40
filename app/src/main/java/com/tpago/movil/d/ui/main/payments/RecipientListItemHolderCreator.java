package com.tpago.movil.d.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecipientListItemHolderCreator implements ListItemHolderCreator<RecipientListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  RecipientListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public RecipientListItemHolder create(@NonNull ViewGroup parent) {
    return new RecipientListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_recipient, parent, false), onClickListener);
  }
}
