package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.util.Event;
import com.tpago.movil.dep.domain.util.EventType;

/**
 * {@link Event} that represents the expiration of the {@link Balance balance} of a {@link Product
 * product}.
 *
 * @author hecvasro
 */
public class BalanceExpirationEvent extends Event {
  /**
   * TODO
   */
  private final Product product;

  /**
   * TODO
   *
   * @param product
   *   TODO
   */
  public BalanceExpirationEvent(@NonNull Product product) {
    super(EventType.PRODUCT_BALANCE_EXPIRATION, false);
    this.product = product;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Product getProduct() {
    return product;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object)
      && object instanceof BalanceExpirationEvent
      && ((BalanceExpirationEvent) object).product.equals(product));
  }

  @Override
  public int hashCode() {
    return product.hashCode();
  }

  @Override
  public String toString() {
    return BalanceExpirationEvent.class.getSimpleName() + ":{super=" + super.toString()
      + ",account=" + product + "}";
  }
}
