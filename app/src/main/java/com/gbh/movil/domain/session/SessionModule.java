package com.gbh.movil.domain.session;

import com.gbh.movil.domain.DeviceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public final class SessionModule {
  /**
   * TODO
   *
   * @param deviceManager
   *   TODO
   * @param sessionRepo
   *   TODO
   * @param sessionService
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  SessionManager provideSessionManager(DeviceManager deviceManager, SessionRepo sessionRepo,
    SessionService sessionService) {
    return new SessionManager(deviceManager, sessionRepo, sessionService);
  }
}
