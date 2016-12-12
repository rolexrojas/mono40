package com.gbh.movil.data.session;

import com.gbh.movil.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author hecvasro
 */
@Module
public class SessionFlavorModule {
  @Provides
  @Singleton
  SessionService provideSessionService(Retrofit retrofit) {
    return new RetrofitSessionService(retrofit);
  }
}
