package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

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
class PaymentsPresenter extends Presenter<PaymentsScreen> {
  /**
   * TODO
   */
  private static final long TIME_SPAN_QUERY = 300L; // 0.3 seconds.

  private final SchedulerProvider schedulerProvider;
  private final RecipientManager recipientManager;

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  PaymentsPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull RecipientManager recipientManager) {
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    querySubscription = screen.onQueryChanged()
      .debounce(TIME_SPAN_QUERY, TimeUnit.MILLISECONDS)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<String>() {
        @Override
        public void call(String query) {
          Timber.d(query);
          RxUtils.unsubscribe(searchSubscription);
          // TODO: Construct actions and concat them with the result.
          // TODO: Let the user know that no results were found.
          searchSubscription = recipientManager.getAll(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                screen.clear();
                UiUtils.showRefreshIndicator(screen);
              }
            })
            .doOnNext(new Action1<Set<Recipient>>() {
              @Override
              public void call(Set<Recipient> recipients) {
                screen.clear();
              }
            })
            .doOnUnsubscribe(new Action0() {
              @Override
              public void call() {
                UiUtils.hideRefreshIndicator(screen);
              }
            })
            .compose(RxUtils.<Recipient>fromCollection())
            .subscribe(new Action1<Recipient>() {
              @Override
              public void call(Recipient recipient) {
                Timber.d(recipient.toString());
                // TODO: Add recipients to the list.
              }
            }, new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                Timber.e(throwable, "Querying recipients and constructing actions");
                // TODO: Let the user know that finding recipients with the given query failed.
              }
            });
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to query change events");
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
  }
}
