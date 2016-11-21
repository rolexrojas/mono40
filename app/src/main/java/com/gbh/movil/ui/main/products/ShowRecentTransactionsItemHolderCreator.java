package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ShowRecentTransactionsItemHolderCreator
  implements ItemHolderCreator<ShowRecentTransactionsItemHolder> {
  private final ShowRecentTransactionsItemHolder
    .OnShowRecentTransactionsButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public ShowRecentTransactionsItemHolderCreator(@NonNull ShowRecentTransactionsItemHolder
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
  public ShowRecentTransactionsItemHolder create(@NonNull ViewGroup parent) {
    return new ShowRecentTransactionsItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_show_recent_transactions, parent, false), listener);
  }
}
