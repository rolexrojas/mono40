package com.gbh.movil.ui.main.recipients.contacts;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.main.recipients.Contact;
import com.gbh.movil.ui.main.recipients.RecipientCandidateListPresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListPresenter extends RecipientCandidateListPresenter {
  private final RxPermissions permissionManager;
  private final ContactProvider contactProvider;

  private Subscription permissionSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param schedulerProvider
   *   TODO
   * @param permissionManager
   *   TODO
   * @param contactProvider
   *   TODO
   */
  ContactListPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull RxPermissions permissionManager, @NonNull ContactProvider contactProvider) {
    super(schedulerProvider);
    this.permissionManager = permissionManager;
    this.contactProvider = contactProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean canStartListeningQueryChangeEvents() {
    return permissionManager.isGranted(Manifest.permission.READ_CONTACTS);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  protected Observable<Object> search(@Nullable String query) {
    return contactProvider.getAll(query)
      .compose(RxUtils.<Contact>fromCollection())
      .cast(Object.class);
  }

  /**
   * TODO
   */
  void create() {
    assertScreen();
    permissionSubscription = permissionManager.request(Manifest.permission.READ_CONTACTS)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean granted) {
          if (granted) {
            startListeningQueryChangeEvents();
          } else {
            // TODO: Let the user know that the read contacts permission is required.
          }
        }
      });
  }

  /**
   * TODO
   */
  void destroy() {
    assertScreen();
    RxUtils.unsubscribe(permissionSubscription);
  }
}
