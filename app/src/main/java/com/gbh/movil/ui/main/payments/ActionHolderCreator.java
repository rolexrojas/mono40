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
class ActionHolderCreator implements HolderCreator<ActionHolder> {
  private final Holder.OnClickListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ActionHolderCreator(@NonNull Holder.OnClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ActionHolder create(@NonNull ViewGroup parent) {
    return new ActionHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_action, parent, false), listener);
  }
}
