package com.tpago.movil.ui.main.recipients;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.RecipientManager;
import com.tpago.movil.misc.rx.RxUtils;
import com.tpago.movil.ui.Presenter;
import com.tpago.movil.ui.misc.UiUtils;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class AddRecipientPresenter extends Presenter<AddRecipientScreen> {
  private final SchedulerProvider schedulerProvider;
  private final RecipientManager recipientManager;

  private Subscription subscription = Subscriptions.unsubscribed();

  AddRecipientPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull RecipientManager recipientManager) {
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }

  /**
   * TODO
   *
   * @param contact
   *   TODO
   */
  void add(@NonNull final Contact contact) {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = recipientManager.addRecipient(contact)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            UiUtils.showRefreshIndicator(screen);
          }
        })
        .subscribe(new Action1<Pair<Boolean, Recipient>>() {
          @Override
          public void call(Pair<Boolean, Recipient> pair) {
            UiUtils.hideRefreshIndicator(screen);
            if (pair.first) {
              screen.finish(pair.second);
            } else {
              screen.showNotSupportedOperationMessage();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Adding a phone number recipient");
            UiUtils.hideRefreshIndicator(screen);
            // TODO: Let the user know that adding a phone number recipient failed.
          }
        });
    }
  }
}
