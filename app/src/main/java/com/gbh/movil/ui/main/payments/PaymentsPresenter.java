package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class PaymentsPresenter {
  /**
   * TODO
   */
  private static final long TIME_SPAN_QUERY = 300L; // 0.3 seconds.

  private final PaymentsScreen screen;

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  PaymentsPresenter(@NonNull PaymentsScreen screen) {
    this.screen = screen;
  }

  void start() {
    querySubscription = screen.onQueryChanged()
      .debounce(TIME_SPAN_QUERY, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<String>() {
        @Override
        public void call(String query) {
          Timber.d(query);
          RxUtils.unsubscribe(searchSubscription);
          // TODO: Find recipients and construct actions.
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to query change events");
        }
      });
  }

  void stop() {
    RxUtils.unsubscribe(querySubscription);
    RxUtils.unsubscribe(searchSubscription);
  }
}
