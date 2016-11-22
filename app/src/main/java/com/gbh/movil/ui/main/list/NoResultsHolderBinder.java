package com.gbh.movil.ui.main.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gbh.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsHolderBinder
  implements HolderBinder<NoResultsItem, NoResultsHolder> {
  private final String format;

  /**
   * TODO
   *
   * @param context
   *   {@link android.app.Activity}'s context.
   */
  public NoResultsHolderBinder(@NonNull Context context) {
    this.format = context.getString(R.string.list_no_results);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(@NonNull NoResultsItem item, @NonNull NoResultsHolder holder) {
    holder.getQueryTextView().setText(String.format(format, item.getQuery()));
  }
}
