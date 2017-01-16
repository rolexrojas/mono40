package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.main.list.NoResultsListItemItem;

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
  private static final long DEFAULT_IME_SPAN_QUERY = 300L; // 0.3 seconds.

  private final StringHelper stringHelper;
  private final SchedulerProvider schedulerProvider;
  private final RecipientManager recipientManager;
  private final SessionManager sessionManager;
  private final ProductManager productManager;

  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();
  /**
   * TODO
   */
  private Subscription querySubscription = Subscriptions.unsubscribed();
  /**
   * TODO
   */
  private Subscription searchSubscription = Subscriptions.unsubscribed();

  PaymentsPresenter(@NonNull StringHelper stringHelper,
    @NonNull SchedulerProvider schedulerProvider, @NonNull RecipientManager recipientManager,
    @NonNull SessionManager sessionManager, @NonNull ProductManager productManager) {
    this.stringHelper = stringHelper;
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
    this.sessionManager = sessionManager;
    this.productManager = productManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    querySubscription = screen.onQueryChanged()
      .debounce(DEFAULT_IME_SPAN_QUERY, TimeUnit.MILLISECONDS)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<String>() {
        @Override
        public void call(final String query) {
          RxUtils.unsubscribe(searchSubscription);
          final Observable<Object> recipientsObservable = recipientManager.getAll(query)
            .compose(RxUtils.<Recipient>fromCollection())
            .map(new Func1<Recipient, Object>() {
              @Override
              public Object call(Recipient recipient) {
                return recipient;
              }
            });
          final Observable<Object> actionsObservable = Observable
            .defer(new Func0<Observable<Object>>() {
              @Override
              public Observable<Object> call() {
                if (PhoneNumber.isValid(query)) {
                  return Observable.just(new TransactionWithPhoneNumberAction(query),
                    new AddPhoneNumberAction(query))
                    .cast(Object.class);
                } else {
                  return Observable.empty();
                }
              }
            });
          searchSubscription = recipientsObservable
            .subscribeOn(schedulerProvider.io())
            .switchIfEmpty(actionsObservable)
            .switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                screen.clear();
                screen.showLoadIndicator(false);
              }
            })
            .doOnUnsubscribe(new Action0() {
              @Override
              public void call() {
                screen.hideLoadIndicator();
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
                Timber.e(throwable, "Querying recipients and constructing actions");
                // TODO: Let the user know that finding recipients with the given query failed.
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

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void addRecipient(@NonNull Recipient recipient) {
    assertScreen();
    screen.clearQuery();
    screen.showConfirmationDialog(recipient,
      stringHelper.recipientAdditionConfirmationTitle(recipient),
      stringHelper.recipientAdditionConfirmationMessage(recipient));
  }

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   */
  void addRecipient(@NonNull String phoneNumber) {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = recipientManager.addRecipient(phoneNumber)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            screen.showLoadIndicator(true);
          }
        })
        .subscribe(new Action1<Pair<Boolean, Recipient>>() {
          @Override
          public void call(Pair<Boolean, Recipient> pair) {
            screen.hideLoadIndicator();
            if (pair.first) {
              screen.clear();
              screen.add(pair.second);
              addRecipient(pair.second);
            } else {
              screen.showUnaffiliatedRecipientAdditionNotAvailableMessage();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Adding a phone number recipient");
            screen.hideLoadIndicator();
            // TODO: Let the user know that adding a phone number recipient failed.
          }
        });
    }
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   * @param label
   *   TODO
   */
  void updateRecipient(@NonNull Recipient recipient, @Nullable String label) {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      recipient.setLabel(label);
      subscription = recipientManager.updateRecipient(recipient)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            screen.showLoadIndicator(true);
          }
        })
        .doOnUnsubscribe(new Action0() {
          @Override
          public void call() {
            screen.hideLoadIndicator();
          }
        })
        .subscribe(new Action1<Recipient>() {
          @Override
          public void call(Recipient recipient) {
            screen.update(recipient);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Updating a recipient");
            // TODO: Let the user know that updating a recipient failed.
          }
        });
    }
  }

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   */
  void startTransfer(@NonNull final String phoneNumber) {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = recipientManager.checkIfAffiliated(phoneNumber)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            screen.showLoadIndicator(true);
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean isAffiliated) {
            screen.hideLoadIndicator();
            if (isAffiliated) {
              screen.startTransfer(phoneNumber);
            } else {
              screen.showPaymentToUnaffiliatedRecipientNotAvailableMessage();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Checking if a recipient is affiliated");
            screen.hideLoadIndicator();
            // TODO: Let the user know that checking if a recipient is affiliated failed.
          }
        });
    }
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void showTransactionConfirmation(@NonNull final Recipient recipient) {
    assertScreen();
    screen.clearQuery();
    screen.showConfirmationDialog(recipient, stringHelper.transactionCreationConfirmationTitle(),
      stringHelper.transactionCreationConfirmationMessage(recipient));
  }

  final void signOut() {
    sessionManager.deactivate();
    productManager.clear();
    recipientManager.clear();
    screen.openIndexScreen();
    screen.finish();
  }
}
