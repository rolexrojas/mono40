package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.widget.AmountView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  /**
   * TODO
   */
  private final Listener listener;

  @BindView(R.id.image_view_bank_logo)
  ImageView bankLogoImageView;
  @BindView(R.id.text_view_account_alias)
  TextView accountAliasTextView;
  @BindView(R.id.text_view_bank_name)
  TextView bankNameTextView;
  @BindView(R.id.button_query_account_balance)
  Button queryAccountBalanceButton;
  @BindView(R.id.amount_view)
  AmountView amountView;

  /**
   * TODO
   *
   * @param itemView
   *   TODO
   * @param listener
   *   TODO
   */
  AccountItemViewHolder(@NonNull View itemView, @NonNull Listener listener) {
    super(itemView);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, itemView);
    // Adds a listener that gets notified every time the query account balance button.
    this.listener = listener;
    this.queryAccountBalanceButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view == queryAccountBalanceButton) {
      final int[] location = new int[2];
      view.getLocationOnScreen(location);
      listener.onQueryBalanceButtonClicked(getAdapterPosition(),
        location[0] + (view.getWidth() / 2), location[1]);
    }
  }

  /**
   * TODO
   */
  interface Listener {
    /**
     * TODO
     *
     * @param position
     *   TODO
     */
    void onQueryBalanceButtonClicked(int position, int x, int y);
  }
}
