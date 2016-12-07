package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;
import com.gbh.movil.ui.view.widget.PrefixableTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductListItemHolder extends ListItemHolder {
  private OnQueryActionButtonClickedListener listener;

  @BindView(R.id.bank_logo)
  ImageView bankLogoImageView;
  @BindView(R.id.product_number)
  TextView productNumberTextView;
  @BindView(R.id.bank_name)
  TextView bankNameTextView;
  @BindView(R.id.action_query)
  Button queryActionButton;
  @BindView(R.id.product_balance)
  PrefixableTextView productBalanceTextView;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param listener
   *   TODO
   */
  ProductListItemHolder(@NonNull View rootView, @NonNull OnQueryActionButtonClickedListener listener) {
    super(rootView);
    this.listener = listener;
  }

  @OnClick(R.id.action_query)
  void onQueryBalanceButtonClicked(@NonNull View view) {
    final int[] location = new int[2];
    view.getLocationOnScreen(location);
    listener.onQueryBalanceButtonClicked(getAdapterPosition(), location[0] + (view.getWidth() / 2),
      location[1]);
  }

  /**
   * TODO
   */
  interface OnQueryActionButtonClickedListener {
    /**
     * TODO
     *
     * @param position
     *   TODO
     */
    void onQueryBalanceButtonClicked(int position, int x, int y);
  }
}
