package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolder;

import butterknife.OnClick;

/**
 * TODO
 *
 * @author hecvasro
 */
class ShowRecentTransactionsItemHolder extends ItemHolder {
  private final OnShowRecentTransactionsButtonClickedListener listener;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param listener
   *   TODO
   */
  ShowRecentTransactionsItemHolder(@NonNull View rootView,
    @NonNull OnShowRecentTransactionsButtonClickedListener listener) {
    super(rootView);
    this.listener = listener;
  }

  /**
   * TODO
   */
  @OnClick(R.id.show_recent_transactions_button)
  void onShowRecentTransactionsButtonClicked() {
    listener.onShowRecentTransactionsButtonClicked();
  }

  /**
   * TODO
   */
  interface OnShowRecentTransactionsButtonClickedListener {
    /**
     * TODO
     */
    void onShowRecentTransactionsButtonClicked();
  }
}
