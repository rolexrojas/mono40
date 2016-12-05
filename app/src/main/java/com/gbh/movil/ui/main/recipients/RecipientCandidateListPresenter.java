package com.gbh.movil.ui.main.payments.recipients;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.list.NoResultsListItemItem;

import java.util.concurrent.TimeUnit;

import rx.Observable;
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
public abstract class RecipientCandidateListPresenter
  extends Presenter<RecipientCandidateListScreen> {
  /**
   * TODO
   */
  private static final long DEFAULT_TIME_SPAN_QUERY = 300L; // 0.3 seconds.

  protected final SchedulerProvider schedulerProvider;

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param schedulerProvider
   *   TODO
   */
  public RecipientCandidateListPresenter(@NonNull SchedulerProvider schedulerProvider) {
    this.schedulerProvider = schedulerProvider;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  protected abstract boolean canStartListeningQueryChangeEvents();

  /**
   * TODO
   *
   * @param query
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  protected abstract Observable<Object> search(@Nullable String query);

  /**
   * TODO
   */
  protected final void startListeningQueryChangeEvents() {
    if (querySubscription.isUnsubscribed()) {
      querySubscription = screen.onQueryChanged()
        .debounce(DEFAULT_TIME_SPAN_QUERY, TimeUnit.MILLISECONDS)
        .observeOn(schedulerProvider.ui())
        .subscribe(new Action1<String>() {
          @Override
          public void call(String query) {
            RxUtils.unsubscribe(searchSubscription);
            searchSubscription = search(query)
              .subscribeOn(schedulerProvider.io())
              .switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
              .observeOn(schedulerProvider.ui())
              .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                  screen.clear();
                  UiUtils.showRefreshIndicator(screen);
                }
              })
              .doOnUnsubscribe(new Action0() {
                @Override
                public void call() {
                  UiUtils.hideRefreshIndicator(screen);
                }
              })
              .subscribe(new Action1<Object>() {
                @Override
                public void call(Object item) {
                  screen.add(item);
                }
              }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                  Timber.e(throwable, "Querying recipient candidates");
                  // TODO: Let the user know that querying recipient candidates failed.
                }
              });
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Listening to query change events");
            // TODO: Let the user know that listening to query change events failed.
          }
        });
    }
  }

  /**
   * TODO
   */
  void start() {
    if (canStartListeningQueryChangeEvents()) {
      startListeningQueryChangeEvents();
    }
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
