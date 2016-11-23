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
  /**
   * TODO
   */
  private static final long DEFAULT_TIME_SPAN_QUERY = 300L; // 0.3 seconds.
  /**
   * TODO
   */
  private static final int DEFAULT_PAGE_SIZE = 15;

  private final SchedulerProvider schedulerProvider;
  private final RxPermissions permissionManager;
  private final ContactProvider contactProvider;

  private Contact lastContact;

  private Subscription permissionSubscription = Subscriptions.unsubscribed();
  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  ContactListPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull RxPermissions permissionManager, @NonNull ContactProvider contactProvider) {
    this.schedulerProvider = schedulerProvider;
    this.permissionManager = permissionManager;
    this.contactProvider = contactProvider;
  }

  void create() {
    assertScreen();
    permissionSubscription = permissionManager.request(Manifest.permission.READ_CONTACTS)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean granted) {
          if (granted) {
            searchSubscription = contactProvider.getAll(DEFAULT_PAGE_SIZE, null, lastContact)
              .subscribeOn(schedulerProvider.io())
              .compose(RxUtils.<Contact>fromCollection())
              .observeOn(schedulerProvider.ui())
              .subscribe(new Action1<Contact>() {
                @Override
                public void call(Contact contact) {
                  screen.add(contact);
                }
              }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                  Timber.e(throwable, "Querying user's contacts");
                  // TODO: Let the user know that finding contacts with the given query failed.
                }
              });
          } else {
            // TODO: Let the user know that the read contacts permission is required.
          }
        }
      });
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
  }

  void destroy() {
    assertScreen();
    RxUtils.unsubscribe(permissionSubscription);
  }
}
