package com.tpago.movil.dep.ui.main.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tpago.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsListItemHolderBinder
  implements ListItemHolderBinder<NoResultsListItemItem, NoResultsListItemHolder> {
  private final String format;

  /**
   * TODO
   *
   * @param context
   *   {@link android.app.Activity}'s context.
   */
  public NoResultsListItemHolderBinder(@NonNull Context context) {
    this.format = context.getString(R.string.list_no_results);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(@NonNull NoResultsListItemItem item, @NonNull NoResultsListItemHolder holder) {
    holder.getQueryTextView().setText(String.format(format, item.getQuery()));
  }
}
