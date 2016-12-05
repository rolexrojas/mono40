package com.gbh.movil.ui.main.payments.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.main.list.ListItemHolderBinder;

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
