package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.UserStore;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.PhoneNumber;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.ui.Presenter;
import com.tpago.movil.dep.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.util.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class PaymentsPresenter extends Presenter<PaymentsScreen> {
  private static final long DEFAULT_IME_SPAN_QUERY = 300L; // 0.3 seconds.

  private final StringHelper stringHelper;
  private final SchedulerProvider schedulerProvider;
  private final RecipientManager recipientManager;
  private final SessionManager sessionManager;
  private final ProductManager productManager;
  private final PosBridge posBridge;
  private final UserStore userStore;

  private boolean deleting = false;
  private List<Recipient> selectedRecipients = new ArrayList<>();

  private Subscription subscription = Subscriptions.unsubscribed();
  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();
  private Subscription deleteSubscription = Subscriptions.unsubscribed();
  private Subscription signOutSubscription = Subscriptions.unsubscribed();

  PaymentsPresenter(
    @NonNull StringHelper stringHelper,
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull RecipientManager recipientManager,
    @NonNull SessionManager sessionManager,
    @NonNull ProductManager productManager,
    PosBridge posBridge,
    UserStore userStore) {
    this.stringHelper = stringHelper;
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
    this.sessionManager = sessionManager;
    this.productManager = productManager;
    this.posBridge = posBridge;
    this.userStore = userStore;
  }

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
                  return Observable.just(
                    new TransactionWithPhoneNumberAction(query),
                    new AddPhoneNumberAction(query))
                    .cast(Object.class);
                } else {
                  return Observable.empty();
                }
              }
            });
          Observable<Object> observable = recipientsObservable
            .subscribeOn(Schedulers.io());
          if (!deleting) {
            observable = observable.switchIfEmpty(actionsObservable);
          }
          searchSubscription = observable
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
                screen.hideLoadIndicator();
                screen.add(item);
              }
            }, new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                Timber.e(throwable, "Querying recipients and constructing actions");
                screen.hideLoadIndicator();
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

  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
    RxUtils.unsubscribe(deleteSubscription);
    RxUtils.unsubscribe(signOutSubscription);
  }

  void addRecipient(@NonNull Recipient recipient) {
    assertScreen();
    screen.showRecipientAdditionDialog(recipient);
  }

  void addRecipient(@NonNull final String phoneNumber) {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = recipientManager.checkIfAffiliated(phoneNumber)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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
              screen.showRecipientAdditionDialog(new PhoneNumberRecipient(phoneNumber));
            } else {
              screen.startNonAffiliatedPhoneNumberRecipientAddition(phoneNumber);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Adding a phone number recipient");
            screen.hideLoadIndicator();
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        });
    }
  }

  void updateRecipient(@NonNull Recipient recipient, @Nullable String label) {
    assertScreen();
    recipient.setLabel(label);
    recipientManager.addSync(recipient);
    screen.clearQuery();
  }

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
            screen.startTransfer(phoneNumber, isAffiliated);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Checking if a recipient is affiliated");
            screen.hideLoadIndicator();
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        });
    }
  }

  void showTransactionSummary(
    final Recipient recipient,
    final String transactionId) {
    assertScreen();
    screen.showTransactionSummary(
      recipient,
      recipientManager.checkIfExists(recipient),
      transactionId);
  }

  final void signOut() {
    if (signOutSubscription.isUnsubscribed()) {
      Completable.defer(new Func0<Completable>() {
        @Override
        public Completable call() {
          recipientManager.clear();
          posBridge.unregisterSync(userStore.get().getPhoneNumber().getValue());
          productManager.clear();
          sessionManager.deactivate();
          userStore.clear();
          return Completable.complete();
        }
      })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action1<Subscription>() {
          @Override
          public void call(Subscription subscription) {
            screen.showLoadIndicator(true);
          }
        })
        .doOnUnsubscribe(new Action0() {
          @Override
          public void call() {
            screen.showLoadIndicator(false);
          }
        })
        .subscribe(new Action0() {
          @Override
          public void call() {
            screen.openInitScreen();
            screen.finish();
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable);
            screen.showGenericErrorDialog();
          }
        });
    }
  }

  final void startDeleting() {
    if (!deleting) {
      deleting = true;
      screen.setDeleting(true);
      screen.clearQuery();
    }
  }

  final void stopDeleting() {
    if (deleting) {
      selectedRecipients.clear();
      deleting = false;
      screen.setDeleting(false);
    }
  }

  final void resolve(Recipient r) {
    if (deleting) {
      if (!selectedRecipients.contains(r)) {
        r.setSelected(true);
        selectedRecipients.add(r);
      } else {
        r.setSelected(false);
        selectedRecipients.remove(r);
      }
      screen.update(r);
      screen.setDeleteButtonEnabled(!selectedRecipients.isEmpty());
    } else if (!(r instanceof BillRecipient) || Objects.isNotNull(((BillRecipient) r).getBalance())) {
      screen.startTransfer(r);
    }
  }

  final void onPinRequestFinished(String pin) {
    if (deleting && deleteSubscription.isUnsubscribed()) {
      deleteSubscription = recipientManager.remove(selectedRecipients, pin)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Recipient>>() {
          @Override
          public void call(List<Recipient> recipientList) {
            screen.dismissPinConfirmator();
            screen.clearQuery();
            stopDeleting();
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Removing one or more recipients");
            screen.dismissPinConfirmator();
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        });
    }
  }

  final void deleteSelectedRecipients() {
    if (deleting && deleteSubscription.isUnsubscribed()) {
      screen.requestPin();
    }
  }
}
