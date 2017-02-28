package com.tpago.movil.dep.ui.main.recipients.contacts;

import android.content.Context;

import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.ui.FragmentScope;
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
