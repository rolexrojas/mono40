package com.gbh.movil.domain;

import com.gbh.movil.Utils;

/**
 * {@link Event} that represents the removal an {@link Account account}.
 *
 * @author hecvasro
 */
public class AccountRemovalEvent extends Event {
  public AccountRemovalEvent() {
    super(EventType.ACCOUNT_REMOVAL, true);
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
    return AccountRemovalEvent.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
