package com.tpago.movil.time;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class OneOrMoreDaysTimePredicate implements TimePredicate {

  static OneOrMoreDaysTimePredicate create(Clock clock) {
    return new OneOrMoreDaysTimePredicate(clock);
  }

  private final Clock clock;

  private OneOrMoreDaysTimePredicate(Clock clock) {
    this.clock = ObjectHelper.checkNotNull(clock, "clock");
  }

  @Override
  public boolean test(Long time) {
    return TimeHelper.toDays(this.clock.getTime() - time) >= 1;
  }
}
