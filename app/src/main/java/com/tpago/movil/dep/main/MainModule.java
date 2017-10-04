package com.tpago.movil.dep.main;

import com.tpago.movil.dep.Avatar;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public final class MainModule {

  private final TimeOutManager.TimeOutHandler timeOutHandler;

  public MainModule(TimeOutManager.TimeOutHandler timeOutHandler) {
    this.timeOutHandler = ObjectHelper.checkNotNull(timeOutHandler, "timeOutHandler");
  }

  @Provides
  @ActivityScope
  com.tpago.movil.dep.User user(Avatar avatar, SessionManager sessionManager) {
    final User user = sessionManager.getUser();

    final com.tpago.movil.dep.User depUser = com.tpago.movil.dep.User.createBuilder()
      .phoneNumber(user.phoneNumber())
      .email(user.email())
      .avatar(avatar)
      .build();

    depUser.name(user.firstName(), user.lastName());

    return depUser;
  }

  @Provides
  @ActivityScope
  TimeOutManager provideTimeOutManager(ConfigManager configManager) {
    return new TimeOutManager(configManager, timeOutHandler);
  }
}
