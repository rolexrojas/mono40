package com.mono40.movil.d.ui.main.recipient.index.category;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.list.ListItemHolder;
import com.mono40.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * @author hecvasro
 */
class RecipientListItemHolderCreator implements ListItemHolderCreator<RecipientListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  RecipientListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public RecipientListItemHolder create(@NonNull ViewGroup parent) {
    return new RecipientListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.d_list_item_recipient, parent, false), onClickListener);
  }
}
