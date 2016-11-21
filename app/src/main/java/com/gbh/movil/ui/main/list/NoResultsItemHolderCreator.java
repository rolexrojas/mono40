package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsItemHolderCreator implements ItemHolderCreator<NoResultsItemHolder> {
  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public NoResultsItemHolder create(@NonNull ViewGroup parent) {
    return new NoResultsItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_no_results, parent, false));
  }
}
