package com.tpago.movil.dep.main;

import com.tpago.movil.company.LogoCatalogMapper;
import com.tpago.movil.dep.Avatar;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.user.User;
import com.tpago.movil.user.UserManager;
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
  com.tpago.movil.dep.User user(Avatar avatar, UserManager userManager) {
    final User user = userManager.get();

    final com.tpago.movil.dep.User depUser = com.tpago.movil.dep.User.createBuilder()
      .phoneNumber(user.phoneNumber())
      .email(user.email())
      .avatar(avatar)
      .build();

    depUser.name(user.firstName(), user.lastName());

    user.addNameConsumer(depUser::name);

    final Carrier carrier = user.carrier();
    if (ObjectHelper.isNotNull(carrier)) {
      depUser.carrier(this.mapPartner(carrier));
    }

    user.addCarrierConsumer(this::mapPartner);

    return depUser;
  }

  @Provides
  @ActivityScope
  TimeOutManager provideTimeOutManager(ConfigManager configManager) {
    return new TimeOutManager(configManager, timeOutHandler);
  }
}
