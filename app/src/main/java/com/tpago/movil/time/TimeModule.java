package com.tpago.movil.time;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class TimeModule {

  @Provides
  @Singleton
  Clock clock() {
    return SystemClock.create();
  }

  @Provides
  @Singleton
  OneOrMoreDaysPredicate oneOrMoreDaysDiff(Clock clock) {
    return OneOrMoreDaysPredicate.create(clock);
  }
}
