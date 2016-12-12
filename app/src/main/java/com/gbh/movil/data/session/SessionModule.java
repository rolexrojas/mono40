package com.gbh.movil.data.session;

import android.content.Context;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.InitialDataLoader;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.session.SessionRepo;
import com.gbh.movil.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = SessionFlavorModule.class)
public class SessionModule {
  @Provides
  @Singleton
  SessionRepo provideSessionRepo(Context context) {
    return new SharedPreferencesSessionRepo(context);
  }

  @Provides
  @Singleton
  SessionManager provideSessionManager(DeviceManager deviceManager, SessionRepo sessionRepo,
    SessionService sessionService, InitialDataLoader initialDataLoader) {
    return new SessionManager(deviceManager, sessionRepo, sessionService, initialDataLoader);
  }
}
