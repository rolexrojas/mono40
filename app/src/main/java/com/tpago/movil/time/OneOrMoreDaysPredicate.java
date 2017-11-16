package com.tpago.movil.time;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class OneOrMoreDaysPredicate implements TimePredicate {

  static OneOrMoreDaysPredicate create(Clock clock) {
    return new OneOrMoreDaysPredicate(clock);
  }

  private final Clock clock;

  private OneOrMoreDaysPredicate(Clock clock) {
    this.clock = ObjectHelper.checkNotNull(clock, "clock");
  }

  @Override
  public boolean test(Long time) {
    return TimeHelper.toDays(this.clock.getTime() - time) >= 1;
  }
}
