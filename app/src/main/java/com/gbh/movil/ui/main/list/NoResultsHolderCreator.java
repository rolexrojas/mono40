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
public class NoResultsHolderCreator implements HolderCreator<NoResultsHolder> {
  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public NoResultsHolder create(@NonNull ViewGroup parent) {
    return new NoResultsHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_no_results, parent, false));
  }
}
