package com.tpago.movil.d.ui.main.recipients;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.misc.UiUtils;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public abstract class RecipientCandidateListPresenter
  extends Presenter<RecipientCandidateListScreen> {
  private static final long DEFAULT_TIME_SPAN_QUERY = 300L; // 0.3 seconds.

  protected final SchedulerProvider schedulerProvider;

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  public RecipientCandidateListPresenter(@NonNull SchedulerProvider schedulerProvider) {
    this.schedulerProvider = schedulerProvider;
  }

  protected abstract boolean canStartListeningQueryChangeEvents();

  @NonNull
  protected abstract Observable<Object> search(@Nullable String query);

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

  void start() {
    if (canStartListeningQueryChangeEvents()) {
      startListeningQueryChangeEvents();
    }
  }

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
  }
}
