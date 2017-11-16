package com.tpago.movil.app.upgrade;

import com.tpago.movil.app.upgrade.action.AppUpgradeAction;
import com.tpago.movil.app.upgrade.action.AppUpgradeActionModule;
import com.tpago.movil.store.Store;

import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = AppUpgradeActionModule.class)
public final class AppUpgradeModule {

  @Provides
  @Singleton
  AppUpgradeManager appUpgradeManager(Store store, Set<AppUpgradeAction> actions) {
    return AppUpgradeManager.builder()
      .store(store)
      .actions(actions)
      .build();
  }
}
