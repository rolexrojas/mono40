package com.tpago.movil.dep.ui.main.products;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolder;
import com.tpago.movil.dep.ui.view.widget.PrefixableTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
class ProductListItemHolder extends ListItemHolder {
  private OnQueryActionButtonClickedListener listener;

  @BindView(R.id.button_query_balance) Button queryBalanceButton;
  @BindView(R.id.image_view_bank_logo) ImageView bankLogoImageView;
  @BindView(R.id.prefixable_text_view_product_balance) PrefixableTextView productBalanceTextView;
  @BindView(R.id.text_view_bank_name) TextView bankNameTextView;
  @BindView(R.id.text_view_product_identifier) TextView productIdentifierTextView;

  ProductListItemHolder(View rootView, OnQueryActionButtonClickedListener listener) {
    super(rootView);
    this.listener = listener;
  }

  @OnClick(R.id.button_query_balance)
  void onQueryBalanceButtonClicked(View view) {
    final int[] location = new int[2];
    view.getLocationOnScreen(location);
    listener.onQueryBalanceButtonClicked(
      getAdapterPosition(),
      location[0] + (view.getWidth() / 2),
      location[1]);
  }

  interface OnQueryActionButtonClickedListener {
    void onQueryBalanceButtonClicked(int position, int x, int y);
  }
}
