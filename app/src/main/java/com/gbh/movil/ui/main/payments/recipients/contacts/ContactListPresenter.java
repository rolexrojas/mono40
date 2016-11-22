package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.Manifest;
import android.support.annotation.NonNull;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.ui.Presenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListPresenter extends Presenter<ContactListScreen> {
  private final SchedulerProvider schedulerProvider;
  private final RxPermissions permissionManager;
  private final ContactProvider contactProvider;

  private Subscription permissionSubscription = Subscriptions.unsubscribed();
  private Subscription querySubscription = Subscriptions.unsubscribed();

  ContactListPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull RxPermissions permissionManager, @NonNull ContactProvider contactProvider) {
    this.schedulerProvider = schedulerProvider;
    this.permissionManager = permissionManager;
    this.contactProvider = contactProvider;
  }

  void create() {
    permissionSubscription = permissionManager.request(Manifest.permission.READ_CONTACTS)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean granted) {
          if (granted) {
            querySubscription = contactProvider.getAll()
              .subscribeOn(schedulerProvider.io())
              .compose(RxUtils.<Contact>fromCollection())
              .observeOn(schedulerProvider.ui())
              .subscribe(new Action1<Contact>() {
                @Override
                public void call(Contact contact) {
                  Timber.d(contact.toString());
                }
              });
          } else {
            // TODO: Let the user know that the read contacts permission is required.
          }
        }
      });
  }

  void stop() {
    RxUtils.unsubscribe(querySubscription);
  }

  void destroy() {
    RxUtils.unsubscribe(permissionSubscription);
  }
}
