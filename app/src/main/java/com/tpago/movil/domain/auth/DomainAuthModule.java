package com.tpago.movil.domain.auth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class DomainAuthModule {

  @Provides
  @Singleton
  AuthorizationTokenStore authorizationTokenStore() {
    return AuthorizationTokenStore.create();
  }
}
