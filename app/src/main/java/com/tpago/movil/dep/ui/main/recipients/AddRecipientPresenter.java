package com.tpago.movil.dep.ui.main.recipients;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.Presenter;
import com.tpago.movil.dep.ui.misc.UiUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class AddRecipientPresenter extends Presenter<AddRecipientScreen> {
  private final RecipientManager recipientManager;

  private Subscription checkIfAffiliatedSubscription = Subscriptions.unsubscribed();

  AddRecipientPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull RecipientManager recipientManager) {
    this.recipientManager = recipientManager;
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(checkIfAffiliatedSubscription);
  }

  void add(@NonNull final Contact contact) {
    assertScreen();
    if (checkIfAffiliatedSubscription.isUnsubscribed()) {
      final String phoneNumber = contact.getPhoneNumber().toString();
      checkIfAffiliatedSubscription = recipientManager.checkIfAffiliated(phoneNumber)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            UiUtils.showRefreshIndicator(screen);
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean isAffiliated) {
            UiUtils.hideRefreshIndicator(screen);
            final String contactPhoneNumber = contact.getPhoneNumber().toString();
            final String contactName = contact.getName();
            if (isAffiliated) {
              final Recipient recipient = new PhoneNumberRecipient(contactPhoneNumber, contactName);
              recipientManager.addSync(recipient);
              screen.finish(recipient);
            } else {
              screen.startNonAffiliatedProcess(new NonAffiliatedPhoneNumberRecipient(
                contactPhoneNumber,
                contactName));
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Adding a phone number recipient");
            UiUtils.hideRefreshIndicator(screen);
          }
        });
    }
  }
}
