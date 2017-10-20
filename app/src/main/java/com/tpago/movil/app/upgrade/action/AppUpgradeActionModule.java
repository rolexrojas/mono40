package com.tpago.movil.app.upgrade.action;

import android.content.Context;

import com.tpago.movil.store.Store;

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
  AppUpgradeAction appUpgradeAction1(Context context, Store store) {
    return AppUpgradeAction1.create(context, store);
  }

  @Provides
  @Singleton
  @IntoSet
  AppUpgradeAction appUpgradeAction2(Context context) {
    return AppUpgradeAction2.create(context);
  }
}
