package com.tpago.movil.session;

import com.tpago.movil.api.Api;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.user.UserManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class SessionModule {

  @Provides
  @Singleton
  AccessTokenManager sessionTokenStore() {
    return AccessTokenManager.create();
  }

  @Provides
  @Singleton
  SessionManager sessionManager(
    AccessTokenManager accessTokenManager,
    Api api,
    UserManager userManager,
    AltAuthMethodManager altAuthMethodManager
  ) {
    return SessionManager.builder()
      .accessTokenStore(accessTokenManager)
      .api(api)
      .userManager(userManager)
      .addClearAction(
        () -> {
          if (altAuthMethodManager.isEnabled()) {
            altAuthMethodManager.disable()
              .blockingAwait();
          }
        }
      )
      .build();
  }
}
