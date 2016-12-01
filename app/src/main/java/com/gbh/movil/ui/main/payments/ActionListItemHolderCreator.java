package com.gbh.movil.ui.main.payments;

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
class ActionListItemHolderCreator implements ListItemHolderCreator<ActionListItemHolder> {
  private final ListItemHolder.OnClickListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ActionListItemHolderCreator(@NonNull ListItemHolder.OnClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ActionListItemHolder create(@NonNull ViewGroup parent) {
    return new ActionListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_action, parent, false), listener);
  }
}
