package com.tpago.movil.main.transactions;

import android.view.View;

/**
 * @author hecvasro
 */
final class SelectedPaymentMethodViewHolder
  extends BasePaymentMethodViewHolder<SelectedPaymentMethodHolder> {
  SelectedPaymentMethodViewHolder(
    View itemView,
    OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView, new SelectedPaymentMethodHolder(itemView), onClickedListener);
  }
}
