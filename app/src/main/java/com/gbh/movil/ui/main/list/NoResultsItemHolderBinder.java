package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;

import com.gbh.movil.data.MessageHelper;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsItemHolderBinder
  implements ItemHolderBinder<NoResultsItem, NoResultsItemHolder> {
  private final MessageHelper messageHelper;

  /**
   * TODO
   *
   * @param messageHelper
   *   TODO
   */
  public NoResultsItemHolderBinder(@NonNull MessageHelper messageHelper) {
    this.messageHelper = messageHelper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(@NonNull NoResultsItem item, @NonNull NoResultsItemHolder holder) {
    holder.getQueryTextView().setText(messageHelper.noResults(item.getQuery()));
  }
}
