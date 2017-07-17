package com.tpago.movil.d.data.session;

import com.tpago.movil.d.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author hecvasro
 */
@Module
@Deprecated
public class SessionFlavorModule {
  @Provides
  @Singleton
  SessionService provideSessionService(Retrofit retrofit) {
    return new RetrofitSessionService(retrofit);
  }
}
