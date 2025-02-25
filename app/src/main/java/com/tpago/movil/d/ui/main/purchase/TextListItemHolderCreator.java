package com.tpago.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class TextListItemHolderCreator implements ListItemHolderCreator<TextListItemHolder> {
  @NonNull
  @Override
  public TextListItemHolder create(@NonNull ViewGroup parent) {
    return new TextListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.d_list_item_text, parent, false));
  }
}
