package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.list.NoResultsItem;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
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
        public void call(final String query) {
          Timber.d(query);
          RxUtils.unsubscribe(searchSubscription);
          final Observable<Object> recipientsObservable = recipientManager.getAll(query)
            .compose(RxUtils.<Recipient>fromCollection())
            .map(new Func1<Recipient, Object>() {
              @Override
              public Object call(Recipient recipient) {
                return RecipientItemCreator.create(recipient);
              }
            });
          final Observable<Object> actionsObservable = Observable
            .defer(new Func0<Observable<Object>>() {
              @Override
              public Observable<Object> call() {
                if (PhoneNumber.isValid(query)) {
                  try {
                    final PhoneNumber phoneNumber = new PhoneNumber(query);
                    return Observable.just(new TransactionWithPhoneNumberAction(phoneNumber),
                      new AddPhoneNumberAction(phoneNumber))
                      .cast(Object.class);
                  } catch (NumberParseException exception) {
                    return Observable.error(exception);
                  }
                } else {
                  return Observable.empty();
                }
              }
            });
          searchSubscription = recipientsObservable
            .switchIfEmpty(actionsObservable)
            .switchIfEmpty(Observable.just(new NoResultsItem(query)))
            .subscribeOn(schedulerProvider.io())
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
                Timber.d(item.toString());
                screen.add(item);
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

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  void onItemClicked(@NonNull Object item) {
    if (item instanceof PhoneNumberRecipientItem) {
      // TODO: Start transfer or payment process.
    } else if (item instanceof Action) {
      switch (((Action) item).getType()) {
        case ActionType.ADD_PHONE_NUMBER:
          screen.startAddRecipientScreen(((PhoneNumberAction) item).getPhoneNumber());
          break;
        case ActionType.TRANSACTION_WITH_PHONE_NUMBER:
          // TODO
          break;
      }
    }
  }
}
