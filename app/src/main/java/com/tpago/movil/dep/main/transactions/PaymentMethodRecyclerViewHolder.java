package com.tpago.movil.dep.main.transactions;

import android.view.View;

/**
 * @author hecvasro
 */
final class PaymentMethodRecyclerViewHolder extends BasePaymentMethodRecyclerViewHolder<PaymentMethodHolder> {
  PaymentMethodRecyclerViewHolder(
    View itemView,
    OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView, new PaymentMethodHolder(itemView), onClickedListener);
  }
}
