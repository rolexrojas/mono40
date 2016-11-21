package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsItemHolderBinder
  implements ItemHolderBinder<NoResultsItem, NoResultsItemHolder> {
  private final StringHelper stringHelper;

  /**
   * TODO
   *
   * @param stringHelper
   *   TODO
   */
  public NoResultsItemHolderBinder(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(@NonNull NoResultsItem item, @NonNull NoResultsItemHolder holder) {
    holder.getQueryTextView().setText(stringHelper.noResults(item.getQuery()));
  }
}
