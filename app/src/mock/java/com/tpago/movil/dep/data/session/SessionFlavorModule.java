package com.tpago.movil.d.data.session;

import com.tpago.movil.d.domain.session.SessionService;

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
