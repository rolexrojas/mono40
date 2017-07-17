package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;

import butterknife.OnClick;

/**
 * TODO
 *
 * @author hecvasro
 */
class ShowRecentTransactionsListItemHolder extends ListItemHolder {
  private final OnShowRecentTransactionsButtonClickedListener listener;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param listener
   *   TODO
   */
  ShowRecentTransactionsListItemHolder(@NonNull View rootView,
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
