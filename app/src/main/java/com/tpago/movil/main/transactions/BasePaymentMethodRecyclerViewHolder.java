package com.tpago.movil.main.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodRecyclerViewHolder<H extends BasePaymentMethodHolder>
  extends RecyclerView.ViewHolder {

  final H internalHolder;

  BasePaymentMethodRecyclerViewHolder(
    final View itemView,
    final H internalHolder,
    final OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView);
    this.internalHolder = Preconditions.assertNotNull(internalHolder, "internalHolder == null");
    Preconditions.assertNotNull(onClickedListener, "onClickedListener == null");
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
