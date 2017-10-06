package com.tpago.movil.session;

import com.tpago.movil.api.Api;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.user.UserStore;

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
  AccessTokenStore sessionTokenStore() {
    return AccessTokenStore.create();
  }

  @Provides
  @Singleton
  SessionManager sessionManager(
    AccessTokenStore accessTokenStore,
    Api api,
    UserStore userStore,
    AltAuthMethodManager altAuthMethodManager
  ) {
    return SessionManager.builder()
      .accessTokenStore(accessTokenStore)
      .api(api)
      .userStore(userStore)
      .addClearAction(
        () -> altAuthMethodManager.disable()
          .blockingAwait()
      )
      .build();
  }
}
