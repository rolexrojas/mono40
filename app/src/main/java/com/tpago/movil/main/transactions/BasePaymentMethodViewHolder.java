package com.tpago.movil.main.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodViewHolder<H extends BasePaymentMethodHolder>
  extends RecyclerView.ViewHolder {
  private final OnPaymentMethodViewHolderClickedListener onClickedListener;

  final H internalHolder;

  BasePaymentMethodViewHolder(
    View itemView,
    H internalHolder,
    final OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView);
    this.internalHolder = Preconditions
      .checkNotNull(internalHolder, "internalHolder == null");
    this.onClickedListener = Preconditions
      .checkNotNull(onClickedListener, "onClickedListener == null");
    this.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onClickedListener.onPaymentMethodViewHolderClicked(getAdapterPosition());
      }
    });
  }

  interface OnPaymentMethodViewHolderClickedListener {
    void onPaymentMethodViewHolderClicked(int position);
  }
}
