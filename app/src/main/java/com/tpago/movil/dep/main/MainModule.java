package com.tpago.movil.dep.main;

import android.net.Uri;

import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;
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

  private Partner mapPartner(Carrier carrier) {
    return Partner.builder()
      .setType(Partner.TYPE_CARRIER)
      .setCode(carrier.code())
      .setId(carrier.id())
      .setName(carrier.name())
      .setImageUriTemplate(carrier.logoTemplate())
      .build();
  }

  @Provides
  @ActivityScope
  com.tpago.movil.dep.User user(SessionManager sessionManager) {
    final User user = sessionManager.getUser();

    final com.tpago.movil.dep.User depUser = com.tpago.movil.dep.User.createBuilder()
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

    final Carrier carrier = user.carrier();
    if (ObjectHelper.isNotNull(carrier)) {
      depUser.carrier(this.mapPartner(carrier));
    }

    user.carrierChanges()
      .map(this::mapPartner)
      .subscribe(depUser::carrier);

    return depUser;
  }

  @Provides
  @ActivityScope
  TimeOutManager provideTimeOutManager(ConfigManager configManager) {
    return new TimeOutManager(configManager, timeOutHandler);
  }
}
