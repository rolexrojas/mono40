package com.tpago.movil.ui.main.accounts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;

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
  @BindView(R.id.image_view_bank_logo)
  ImageView bankLogoImageView;

  /**
   * TODO
   */
  @BindView(R.id.text_view_account_alias)
  TextView accountAliasTextView;

  /**
   * TODO
   */
  @BindView(R.id.text_view_bank_name)
  TextView bankNameTextView;

  /**
   * TODO
   */
  @BindView(R.id.button_query_account_balance)
  Button queryAccountBalanceButton;

  /**
   * TODO
   */
  @BindView(R.id.text_view_account_balance)
  TextView accountBalanceTextView;

  /**
   * TODO
   *
   * @param itemView
   *   TODO
   */
  AccountItemViewHolder(View itemView) {
    super(itemView);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, itemView);
    // Adds a listener that gets notified every time the query account balance button.
    queryAccountBalanceButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view == queryAccountBalanceButton) {
      // TODO: Query account balance.
    }
  }
}
