package com.tpago.movil;

import com.tpago.movil.util.Preconditions;

import java.util.List;

/**
 * @author hecvasro
 */
public final class PaymentMethodManager {
  public final List<PaymentMethod<? extends Product>> getAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  public final PaymentMethod<? extends Product> getPrimary() {
    throw new UnsupportedOperationException("Not implemented");
  }

  public final boolean checkIfPrimary(PaymentMethod<? extends Product> paymentMethod) {
    return Preconditions.checkNotNull(paymentMethod, "paymentMethod == null")
      .equals(getPrimary());
  }

  public final void setAsPrimary(PaymentMethod<? extends Product> paymentMethod) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
