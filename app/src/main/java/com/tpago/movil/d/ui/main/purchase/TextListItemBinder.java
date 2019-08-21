package com.tpago.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;

import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class TextListItemBinder implements ListItemHolderBinder<String, TextListItemHolder> {
  @Override
  public void bind(@NonNull String item, @NonNull TextListItemHolder holder) {
    holder.getTextView().setText(item);
  }
}
