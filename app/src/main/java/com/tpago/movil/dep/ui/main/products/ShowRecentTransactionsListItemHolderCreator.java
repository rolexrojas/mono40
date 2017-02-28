package com.tpago.movil.dep.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ShowRecentTransactionsListItemHolderCreator
  implements ListItemHolderCreator<ShowRecentTransactionsListItemHolder> {
  private final ShowRecentTransactionsListItemHolder
    .OnShowRecentTransactionsButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public ShowRecentTransactionsListItemHolderCreator(@NonNull ShowRecentTransactionsListItemHolder
    .OnShowRecentTransactionsButtonClickedListener listener) {
    this.listener = listener;
  }

  /**
   * TODO
   *
   * @param parent
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  @Override
  public ShowRecentTransactionsListItemHolder create(@NonNull ViewGroup parent) {
    return new ShowRecentTransactionsListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_show_recent_transactions, parent, false), listener);
  }
}
