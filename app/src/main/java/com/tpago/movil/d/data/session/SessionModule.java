package com.tpago.movil.d.data.session;

import android.content.Context;

import com.tpago.movil.d.domain.DeviceManager;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.session.SessionRepo;
import com.tpago.movil.d.domain.session.SessionService;
import com.tpago.movil.d.domain.util.EventBus;

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
    SessionService sessionService, EventBus eventBus) {
    return new SessionManager(deviceManager, sessionRepo, sessionService, eventBus);
  }
}
