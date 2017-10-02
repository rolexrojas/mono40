package com.tpago.movil.dep.main;

import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.Session;
import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.dep.Preconditions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public final class MainModule {
  private final Session session;
  private final TimeOutManager.TimeOutHandler timeOutHandler;

  public MainModule(Session session, TimeOutManager.TimeOutHandler timeOutHandler) {
    this.session = Preconditions.assertNotNull(session, "session == null");
    this.timeOutHandler = Preconditions.assertNotNull(timeOutHandler, "timeOutHandler == null");
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
