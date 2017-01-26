package com.tpago.movil.domain;

import com.tpago.movil.misc.Utils;
import com.tpago.movil.domain.util.Event;
import com.tpago.movil.domain.util.EventType;

/**
 * {@link Event} that represents the removal of a {@link Product product}.
 *
 * @author hecvasro
 */
public class ProductRemovalEvent extends Event {
  public ProductRemovalEvent() {
    super(EventType.PRODUCT_REMOVAL, true);
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
    return ProductRemovalEvent.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
