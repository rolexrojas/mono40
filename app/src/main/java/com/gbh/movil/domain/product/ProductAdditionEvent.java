package com.gbh.movil.domain.product;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Event;
import com.gbh.movil.domain.EventType;

/**
 * {@link Event} that represents the addition a {@link Product product}.
 *
 * @author hecvasro
 */
public class ProductAdditionEvent extends Event {
  public ProductAdditionEvent() {
    super(EventType.PRODUCT_ADDITION, true);
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Event
      && ((Event) object).getType().equals(getType()));
  }

  @Override
  public int hashCode() {
    return getType().hashCode();
  }

  @Override
  public String toString() {
    return ProductAdditionEvent.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
