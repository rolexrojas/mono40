package com.gbh.movil.ui.main.recipients.contacts;

import android.content.Context;

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
  ContactProvider provideContactProvider(Context context) {
    return new ContactProvider(context.getContentResolver());
  }

  @Provides
  @FragmentScope
  ContactListPresenter providePresenter(SchedulerProvider schedulerProvider,
    RxPermissions permissionManager, ContactProvider contactProvider) {
    return new ContactListPresenter(schedulerProvider, permissionManager, contactProvider);
  }
}
