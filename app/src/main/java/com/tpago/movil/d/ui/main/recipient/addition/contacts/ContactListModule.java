package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import android.content.Context;

import com.tpago.movil.app.ui.fragment.FragmentScope;
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
  ContactListPresenter providePresenter(
    RxPermissions permissionManager,
    ContactProvider contactProvider
  ) {
    return new ContactListPresenter(permissionManager, contactProvider);
  }
}
