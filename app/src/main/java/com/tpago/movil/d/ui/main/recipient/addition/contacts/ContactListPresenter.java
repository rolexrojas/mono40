package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * @author hecvasro
 */
class ContactListPresenter extends RecipientCandidateListPresenter {

  private final RxPermissions permissionManager;
  private final ContactProvider contactProvider;

  private Subscription permissionSubscription = Subscriptions.unsubscribed();

  ContactListPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull RxPermissions permissionManager,
    @NonNull ContactProvider contactProvider
  ) {
    super(schedulerProvider);
    this.permissionManager = permissionManager;
    this.contactProvider = contactProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean canStartListeningQueryChangeEvents() {
    return this.permissionManager.isGranted(Manifest.permission.READ_CONTACTS);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  protected Observable<Object> search(@Nullable String query) {
    return this.contactProvider.getAll(query)
      .compose(RxUtils.fromCollection())
      .cast(Object.class);
  }

  final void create() {
    this.assertScreen();
    this.permissionSubscription = this.permissionManager.request(Manifest.permission.READ_CONTACTS)
      .observeOn(schedulerProvider.ui())
      .subscribe((granted) -> {
        if (granted) {
          this.startListeningQueryChangeEvents();
        }
      });
  }

  final void destroy() {
    this.assertScreen();
    RxUtils.unsubscribe(this.permissionSubscription);
  }
}
