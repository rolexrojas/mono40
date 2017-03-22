package com.tpago.movil.d.data.session;

import android.content.Context;

import com.tpago.movil.d.domain.DepDeviceManager;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.session.SessionRepo;
import com.tpago.movil.d.domain.session.SessionService;
import com.tpago.movil.d.domain.util.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module(includes = SessionFlavorModule.class)
public class SessionModule {
  @Provides
  @Singleton
  SessionRepo provideSessionRepo(Context context) {
    return new SharedPreferencesSessionRepo(context);
  }

  @Provides
  @Singleton
  SessionManager provideSessionManager(DepDeviceManager deviceManager, SessionRepo sessionRepo,
    SessionService sessionService, EventBus eventBus) {
    return new SessionManager(deviceManager, sessionRepo, sessionService, eventBus);
  }
}
