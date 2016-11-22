package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.HolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ShowRecentTransactionsHolderCreator
  implements HolderCreator<ShowRecentTransactionsHolder> {
  private final ShowRecentTransactionsHolder
    .OnShowRecentTransactionsButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  public ShowRecentTransactionsHolderCreator(@NonNull ShowRecentTransactionsHolder
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
  public ShowRecentTransactionsHolder create(@NonNull ViewGroup parent) {
    return new ShowRecentTransactionsHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_show_recent_transactions, parent, false), listener);
  }
}
