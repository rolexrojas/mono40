package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolderCreator;

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
      .inflate(R.layout.list_item_text, parent, false));
  }
}
