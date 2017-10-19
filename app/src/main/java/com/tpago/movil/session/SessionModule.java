package com.tpago.movil.session;

import com.birbit.android.jobqueue.JobManager;
import com.tpago.movil.api.Api;
import com.tpago.movil.store.Store;

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
  AccessTokenStore accessTokenStore() {
    return AccessTokenStore.create();
  }

  @Provides
  @Singleton
  AccessTokenInterceptor accessTokenInterceptor(AccessTokenStore accessTokenStore) {
    return AccessTokenInterceptor.create(accessTokenStore);
  }

  @Provides
  @Singleton
  SessionManager sessionManager(
    AccessTokenStore accessTokenStore,
    Api api,
    JobManager jobManager,
    Store store
  ) {
    return SessionManager.builder()
      .accessTokenStore(accessTokenStore)
      .api(api)
      .jobManager(jobManager)
      .store(store)
      .build();
  }
}
