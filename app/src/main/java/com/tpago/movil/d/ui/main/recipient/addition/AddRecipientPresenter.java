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
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.ObjectHelper;

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

  private final RecipientManager recipientManager;
  private final Category category;

  private Subscription checkIfAffiliatedSubscription = Subscriptions.unsubscribed();

  AddRecipientPresenter(RecipientManager recipientManager, Category category) {
    this.recipientManager = ObjectHelper.checkNotNull(recipientManager, "recipientManager");
    this.category = category;
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(checkIfAffiliatedSubscription);
  }

  void add(@NonNull final Contact contact) {
    assertScreen();
    if (checkIfAffiliatedSubscription.isUnsubscribed()) {
      final String phoneNumber = contact.phoneNumber()
        .value();
      checkIfAffiliatedSubscription = recipientManager
        .checkIfAffiliated(phoneNumber)
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
            final String contactPhoneNumber = contact.phoneNumber()
              .value();
            final String contactName = contact.name();
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
