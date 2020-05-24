package com.mono40.movil.dep.main;

import android.net.Uri;

import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.dep.ConfigManager;
import com.mono40.movil.dep.TimeOutManager;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.session.User;
import com.mono40.movil.util.ObjectHelper;

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
  com.mono40.movil.dep.User user(SessionManager sessionManager) {
    final User user = sessionManager.getUser();

    final com.mono40.movil.dep.User depUser = com.mono40.movil.dep.User.createBuilder()
      .phoneNumber(user.phoneNumber())
      .email(user.email())
      .build();

    depUser.name(user.firstName(), user.lastName());

    user.nameChanges()
      .subscribe((name) -> depUser.name(name.first(), name.last()));

    final Integer id = user.id();
    if (ObjectHelper.isNotNull(id)) {
      depUser.id(id);
    }

    user.idObservable()
      .subscribe(depUser::id);

    final Uri picture = user.picture();
    if (ObjectHelper.isNotNull(picture)) {
      depUser.picture(picture);
    }

    user.pictureChanges()
      .subscribe(depUser::picture);

    final Partner carrier = user.carrier();
    if (ObjectHelper.isNotNull(carrier)) {
      depUser.carrier(carrier);
    }

    user.carrierChanges()
      .subscribe(depUser::carrier);

    return depUser;
  }

  @Provides
  @ActivityScope
  TimeOutManager provideTimeOutManager(ConfigManager configManager) {
    return new TimeOutManager(configManager, timeOutHandler);
  }
}
