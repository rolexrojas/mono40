package com.gbh.movil.data.session;

import android.content.Context;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.session.SessionRepo;
import com.gbh.movil.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author hecvasro
 */
@Module
public class SessionModule {
  @Provides
  @Singleton
  SessionRepo provideSessionRepo(Context context) {
    return new SharedPreferencesSessionRepo(context);
  }

  @Provides
  @Singleton
  SessionService provideSessionService(Retrofit retrofit) {
    return new RetrofitSessionService(retrofit);
  }

  @Provides
  @Singleton
  SessionManager provideSessionManager(DeviceManager deviceManager, SessionRepo sessionRepo,
    SessionService sessionService) {
    return new SessionManager(deviceManager, sessionRepo, sessionService);
  }
}
