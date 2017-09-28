package com.tpago.movil.d.ui.main.recipient.addition;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.main.recipient.index.category.Category;
import com.tpago.movil.d.ui.misc.UiUtils;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.util.Preconditions;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class AddRecipientPresenter extends Presenter<AddRecipientScreen> {

  private final String authToken;
  private final RecipientManager recipientManager;

  private final Category category;

  private Subscription checkIfAffiliatedSubscription = Subscriptions.unsubscribed();

  AddRecipientPresenter(String authToken, RecipientManager recipientManager, Category category) {
    this.authToken = Preconditions.assertNotNull(authToken, "authToken == null");
    this.recipientManager = Preconditions
      .assertNotNull(recipientManager, "recipientManager == null");

    this.category = category;
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(checkIfAffiliatedSubscription);
  }

  void add(@NonNull final Contact contact) {
    assertScreen();
    if (checkIfAffiliatedSubscription.isUnsubscribed()) {
      final String phoneNumber = contact.getPhoneNumber().toString();
      checkIfAffiliatedSubscription = recipientManager
        .checkIfAffiliated(authToken, phoneNumber)
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
            final String contactPhoneNumber = contact.getPhoneNumber()
              .toString();
            final String contactName = contact.getName();
            if (isAffiliated) {
              final Recipient recipient = new PhoneNumberRecipient(
                PhoneNumber.create(contactPhoneNumber),
                contactName
              );
              recipientManager.add(recipient);
              screen.finish(recipient);
            } else {
              screen.startNonAffiliatedProcess(
                new NonAffiliatedPhoneNumberRecipient(
                  PhoneNumber.create(contactPhoneNumber),
                  contactName
                )
              );
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
