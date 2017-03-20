package com.tpago.movil.main;

import com.tpago.movil.ConfigManager;
import com.tpago.movil.Session;
import com.tpago.movil.TimeOutManager;
import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.util.Preconditions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class MainModule {
  private final Session session;
  private final TimeOutManager.TimeOutHandler timeOutHandler;

  public MainModule(Session session, TimeOutManager.TimeOutHandler timeOutHandler) {
    this.session = Preconditions.checkNotNull(session, "session == null");
    this.timeOutHandler = Preconditions.checkNotNull(timeOutHandler, "timeOutHandler == null");
  }

  @Provides
  @ActivityScope
  User provideUser(UserStore userStore) {
    return userStore.get();
  }

  @Provides
  @ActivityScope
  Session provideSession() {
    return session;
  }

  @Provides
  @ActivityScope
  TimeOutManager provideTimeOutManager(ConfigManager configManager) {
    return new TimeOutManager(configManager, timeOutHandler);
  }
}
