package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.PhoneNumber;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.pos.PosResult;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.ui.Presenter;
import com.tpago.movil.dep.ui.main.list.NoResultsListItemItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
  private final PosBridge posBridge;

  private boolean deleting = false;
  private List<Recipient> selectedRecipients = new ArrayList<>();

  private Subscription subscription = Subscriptions.unsubscribed();
  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();
  private Subscription deleteSubscription = Subscriptions.unsubscribed();
  private Subscription signOutSubscription = Subscriptions.unsubscribed();

  PaymentsPresenter(@NonNull StringHelper stringHelper,
    @NonNull SchedulerProvider schedulerProvider, @NonNull RecipientManager recipientManager,
    @NonNull SessionManager sessionManager, @NonNull ProductManager productManager,
    PosBridge posBridge) {
    this.stringHelper = stringHelper;
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
    this.sessionManager = sessionManager;
    this.productManager = productManager;
    this.posBridge = posBridge;
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
          Observable<Object> observable = recipientsObservable
            .subscribeOn(Schedulers.io());
          if (!deleting) {
            observable = observable.switchIfEmpty(actionsObservable);
          }
          observable.switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
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
    RxUtils.unsubscribe(deleteSubscription);
    RxUtils.unsubscribe(signOutSubscription);
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
      stringHelper.doneWithExclamationMark(),
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
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
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
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
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

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void showTransactionConfirmation(@NonNull final Recipient recipient) {
    assertScreen();
    screen.clearQuery();
    screen.showConfirmationDialog(
      recipient,
      stringHelper.transactionCreationConfirmationTitle(),
      stringHelper.transactionCreationConfirmationMessage(recipient));
  }

  final void signOut() {
    posBridge.reset(sessionManager.getSession().getPhoneNumber())
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          screen.showLoadIndicator(true);
        }
      })
      .subscribe(new Action1<PosResult>() {
        @Override
        public void call(PosResult result) {
          screen.hideLoadIndicator();
          if (result.isSuccessful()) {
            sessionManager.deactivate();
            productManager.clear();
            recipientManager.clear();
            screen.openIndexScreen();
            screen.finish();
          } else {
            screen.showMessage(result.getData());
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Signing out");
          screen.hideLoadIndicator();
          screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
        }
      });
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

  final void resolve(Recipient recipient) {
    if (deleting) {
      if (!selectedRecipients.contains(recipient)) {
        recipient.setSelected(true);
        selectedRecipients.add(recipient);
      } else {
        recipient.setSelected(false);
        selectedRecipients.remove(recipient);
      }
      screen.update(recipient);
      screen.setDeleteButtonEnabled(!selectedRecipients.isEmpty());
    } else {
      screen.startTransfer(recipient);
    }
  }

  final void deleteSelectedRecipients() {
    if (deleting && deleteSubscription.isUnsubscribed()) {
      deleteSubscription = recipientManager.remove(selectedRecipients)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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
        .compose(RxUtils.<Recipient>fromCollection())
        .subscribe(new Action1<Recipient>() {
          @Override
          public void call(Recipient recipient) {
            screen.remove(recipient);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Removing one or more recipients");
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        }, new Action0() {
          @Override
          public void call() {
            stopDeleting();
          }
        });
    }
  }
}
