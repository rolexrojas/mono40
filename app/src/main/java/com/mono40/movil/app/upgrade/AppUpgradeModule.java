package com.mono40.movil.app.upgrade;

import com.mono40.movil.app.upgrade.action.AppUpgradeAction;
import com.mono40.movil.app.upgrade.action.AppUpgradeActionModule;
import com.mono40.movil.store.DiskStore;

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
  AppUpgradeManager appUpgradeManager(DiskStore diskStore, Set<AppUpgradeAction> actions) {
    return AppUpgradeManager.builder()
      .store(diskStore)
      .actions(actions)
      .build();
  }
}
