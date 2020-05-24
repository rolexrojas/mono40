package com.mono40.movil.app.upgrade.action;

import android.content.Context;

import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.store.DiskStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * @author hecvasro
 */
@Module
public final class AppUpgradeActionModule {

  @Provides
  @Singleton
  @IntoSet
  AppUpgradeAction appUpgradeAction1(Context context, DiskStore diskStore) {
    return AppUpgradeAction1.create(context, diskStore);
  }

  @Provides
  @Singleton
  @IntoSet
  AppUpgradeAction appUpgradeAction2(Context context) {
    return AppUpgradeAction2.create(context);
  }

  @Provides
  @Singleton
  @IntoSet
  AppUpgradeAction appUpgradeAction3(SessionManager sessionManager, PosBridge posBridge) {
    return AppUpgradeAction3.create(sessionManager, posBridge);
  }
}
