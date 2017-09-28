package com.tpago.movil.data.bus;

import com.tpago.movil.BuildConfig;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Hector Vasquez
 */
@Module
public final class BusModule {

  @Provides
  @Singleton
  EventBus bus() {
    return EventBus.builder()
      .addIndex(new BusIndex())
      .logSubscriberExceptions(false)
      .logNoSubscriberMessages(BuildConfig.DEBUG)
      .sendNoSubscriberEvent(false)
      .sendSubscriberExceptionEvent(true)
      .installDefaultEventBus();
  }
}
