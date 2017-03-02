package com.tpago.movil.dep.data.session;

import com.tpago.movil.dep.domain.session.SessionService;

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
    return new FakeSessionService();
  }
}
