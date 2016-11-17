package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventType;

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
