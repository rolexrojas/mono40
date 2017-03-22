package com.tpago.movil.dep.ui.main.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsListItemHolderCreator implements ListItemHolderCreator<NoResultsListItemHolder> {
  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public NoResultsListItemHolder create(@NonNull ViewGroup parent) {
    return new NoResultsListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.d_list_item_no_results, parent, false));
  }
}
