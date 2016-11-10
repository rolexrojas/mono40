package com.gbh.movil.domain;

import com.gbh.movil.Utils;

/**
 * {@link Event} that represents the addition an {@link Account account}.
 *
 * @author hecvasro
 */
public class AccountAdditionEvent extends Event {
  public AccountAdditionEvent() {
    super(EventType.ACCOUNT_ADDITION, true);
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
    return AccountAdditionEvent.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
