package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gbh.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
class ShowRecentTransactionsViewHolder extends RecyclerView.ViewHolder
  implements View.OnClickListener {
  private final Listener listener;

  /**
   * TODO
   */
  @BindView(R.id.button)
  Button button;

  /**
   * TODO
   *
   * @param view
   *   TODO
   */
  ShowRecentTransactionsViewHolder(@NonNull View view, @NonNull Listener listener) {
    super(view);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, view);
    // Adds a listener that gets notified every time the button is clicked.
    this.listener = listener;
    this.button.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view == button) {
      listener.onShowRecentTransactionsButtonClicked();
    }
  }

  /**
   * TODO
   */
  interface Listener {
    /**
     * TODO
     */
    void onShowRecentTransactionsButtonClicked();
  }
}
