package com.gbh.movil.ui.main.payments.recipients;

import android.support.annotation.NonNull;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
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
      subscription = recipientManager.checkIfAssociated(contact.getPhoneNumber())
        .flatMap(new Func1<Boolean, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(Boolean associated) {
            if (associated) {
              return recipientManager.addRecipient(contact)
                .map(new Func1<Recipient, Boolean>() {
                  @Override
                  public Boolean call(Recipient recipient) {
                    return true;
                  }
                });
            } else {
              return Observable.just(false);
            }
          }
        })
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            UiUtils.showRefreshIndicator(screen);
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean added) {
            UiUtils.hideRefreshIndicator(screen);
            if (added) {
              screen.terminate();
            } else {
              screen.showNotSupportedOperationMessage();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Adding a contact recipient");
            UiUtils.hideRefreshIndicator(screen);
          }
        });
    }
  }
}
