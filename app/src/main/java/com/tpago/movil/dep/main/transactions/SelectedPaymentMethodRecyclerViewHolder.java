package com.tpago.movil.dep.main.transactions;

import android.view.View;

/**
 * @author hecvasro
 */
final class SelectedPaymentMethodRecyclerViewHolder
  extends BasePaymentMethodRecyclerViewHolder<SelectedPaymentMethodHolder> {
  SelectedPaymentMethodRecyclerViewHolder(
    View itemView,
    OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView, new SelectedPaymentMethodHolder(itemView), onClickedListener);
  }
}
