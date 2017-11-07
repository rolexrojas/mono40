package com.tpago.movil.time;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class OneOrMoreDaysExpirationPredicate implements ExpirationPredicate {

  static OneOrMoreDaysExpirationPredicate create(Clock clock) {
    return new OneOrMoreDaysExpirationPredicate(clock);
  }

  private final Clock clock;

  private OneOrMoreDaysExpirationPredicate(Clock clock) {
    this.clock = ObjectHelper.checkNotNull(clock, "clock");
  }

  @Override
  public boolean test(Long time) {
    return TimeHelper.toDays(this.clock.getTime() - time) >= 1;
  }
}
