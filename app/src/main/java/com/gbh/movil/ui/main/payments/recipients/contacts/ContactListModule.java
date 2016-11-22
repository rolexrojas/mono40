package com.gbh.movil.ui.main.payments.recipients.contacts;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.ui.FragmentScope;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class ContactListModule {
  @Provides
  @FragmentScope
  ContactListPresenter providePresenter(SchedulerProvider schedulerProvider,
    RxPermissions permissionManager) {
    return new ContactListPresenter(schedulerProvider, permissionManager);
  }
}
