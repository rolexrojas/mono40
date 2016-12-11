package com.gbh.movil.data.session;

import com.gbh.movil.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class SessionFlavorModule {
  @Provides
  @Singleton
  SessionService provideSessionService() {
    return new MockSessionService();
  }
}
