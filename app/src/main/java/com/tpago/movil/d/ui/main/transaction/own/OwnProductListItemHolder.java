package com.tpago.movil.d.ui.main.transaction.own;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
final class OwnProductListItemHolder extends ListItemHolder {
  private final OnButtonClickedListener onButtonClickedListenerListener;

  @BindView(R.id.button) Button button;
  @BindView(R.id.image_view_bank_logo) ImageView bankLogoImageView;
  @BindView(R.id.text_view_product_identifier) TextView productIdentifierTextView;
  @BindView(R.id.text_view_product_type) TextView productTypeTextView;

  OwnProductListItemHolder(View rootView, OnButtonClickedListener onButtonClickedListenerListener) {
    super(rootView);
    this.onButtonClickedListenerListener = onButtonClickedListenerListener;
  }

  @OnClick(R.id.button)
  void onQueryBalanceButtonClicked(View view) {
    onButtonClickedListenerListener.onButtonClicked(getAdapterPosition());
  }

  interface OnButtonClickedListener {
    void onButtonClicked(int position);
  }
}
