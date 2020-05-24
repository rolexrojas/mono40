package com.mono40.movil.d.domain;

import com.mono40.movil.d.domain.util.Event;
import com.mono40.movil.d.domain.util.EventType;
import com.mono40.movil.util.ObjectHelper;

/**
 * {@link Event} that represents the addition a {@link Product creditCard}.
 *
 * @author hecvasro
 */
@Deprecated
public class ProductAdditionEvent extends Event {

  public ProductAdditionEvent() {
    super(EventType.PRODUCT_ADDITION, true);
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
    return ProductAdditionEvent.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
