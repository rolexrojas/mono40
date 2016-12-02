package com.gbh.movil.ui.main.payments.commerce;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolderCreator;

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
