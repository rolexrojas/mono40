package com.mono40.movil.d.ui.main.recipient.addition.contacts;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mono40.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import io.reactivex.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * @author hecvasro
 */
class ContactListPresenter extends RecipientCandidateListPresenter {

  private final RxPermissions permissionManager;
  private final ContactProvider contactProvider;

  private Subscription subscription = Subscriptions.unsubscribed();

  ContactListPresenter(RxPermissions permissionManager, ContactProvider contactProvider) {
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
      .flatMap(Observable::fromIterable)
      .cast(Object.class);
  }

  final void create() {
    this.subscription = this.permissionManager.request(Manifest.permission.READ_CONTACTS)
      .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
      .subscribe((granted) -> {
        if (granted) {
          this.startListeningQueryChangeEvents();
        }
      });
  }

  final void destroy() {
    if (!this.subscription.isUnsubscribed()) {
      this.subscription.unsubscribe();
    }
  }
}
