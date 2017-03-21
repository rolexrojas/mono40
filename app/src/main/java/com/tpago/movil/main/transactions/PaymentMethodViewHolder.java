package com.tpago.movil.main.transactions;

import android.view.View;

/**
 * @author hecvasro
 */
final class PaymentMethodViewHolder extends BasePaymentMethodViewHolder<PaymentMethodHolder> {
  PaymentMethodViewHolder(
    View itemView,
    OnPaymentMethodViewHolderClickedListener onClickedListener) {
    super(itemView, new PaymentMethodHolder(itemView), onClickedListener);
  }
}
