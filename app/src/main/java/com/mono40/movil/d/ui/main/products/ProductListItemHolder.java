package com.mono40.movil.d.ui.main.products;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.list.ListItemHolder;
import com.mono40.movil.d.ui.view.Views;
import com.mono40.movil.d.ui.view.widget.PrefixableTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
final class ProductListItemHolder extends ListItemHolder {
  private final OnQueryBalanceButtonPressedListener onQueryButtonClickedListener;

  @BindView(R.id.button_query_balance)
  Button queryBalanceButton;
  @BindView(R.id.image_view_bank_logo)
  ImageView bankLogoImageView;
  @BindView(R.id.prefixable_text_view_product_balance)
  PrefixableTextView productBalanceTextView;
  @BindView(R.id.text_view_product_identifier)
  TextView productIdentifierTextView;
  @BindView(R.id.text_view_product_type)
  TextView productTypeTextView;
  @BindView(R.id.text_view_query_time)
  TextView queryTimeTextView;

  ProductListItemHolder(
    View rootView,
    OnClickListener onClickListener,
    OnQueryBalanceButtonPressedListener onQueryButtonClickedListener) {
    super(rootView, onClickListener);
    this.onQueryButtonClickedListener = onQueryButtonClickedListener;
  }

  @OnClick(R.id.button_query_balance)
  void onQueryBalanceButtonClicked(View view) {
    final int[] location = Views.getLocationOnScreen(view);
    onQueryButtonClickedListener.onQueryBalanceButtonPressed(
      getAdapterPosition(),
      location[0],
      location[1]);
  }

  interface OnQueryBalanceButtonPressedListener {
    void onQueryBalanceButtonPressed(int position, int x, int y);
  }
}
