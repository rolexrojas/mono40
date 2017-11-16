package com.tpago.movil.d.domain;

import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.util.ObjectHelper;

/**
 * {@link Event} that represents the removal of a {@link Product creditCard}.
 *
 * @author hecvasro
 */
@Deprecated
public class ProductRemovalEvent extends Event {

  public ProductRemovalEvent() {
    super(EventType.PRODUCT_REMOVAL, true);
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (ObjectHelper.isNotNull(object) && object instanceof Event
      && ((Event) object).getType()
      .equals(getType()));
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
