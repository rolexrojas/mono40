package com.tpago.movil.dep.main.transactions;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodRecyclerViewHolder<H extends BasePaymentMethodHolder>
  extends RecyclerView.ViewHolder {

  final H internalHolder;

  BasePaymentMethodRecyclerViewHolder(
    final View itemView,
    final H internalHolder,
    final OnPaymentMethodViewHolderClickedListener onClickedListener
  ) {
    super(itemView);
    this.internalHolder = ObjectHelper.checkNotNull(internalHolder, "internalHolder");
    ObjectHelper.checkNotNull(onClickedListener, "onClickedListener");
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
