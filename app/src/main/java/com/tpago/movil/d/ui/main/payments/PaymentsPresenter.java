package com.tpago.movil.d.ui.main.payments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.tpago.movil.R;
import com.tpago.movil.UserStore;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.api.ApiError;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
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

  private final NetworkService networkService;
  private final DepApiBridge depApiBridge;
  private final String authToken;

  private boolean deleting = false;
  private List<Recipient> selectedRecipientList = new ArrayList<>();

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();
  private Subscription signOutSubscription = Subscriptions.unsubscribed();
  private Subscription queryBalanceSubscription = Subscriptions.unsubscribed();

  private Disposable recipientAdditionSubscription = Disposables.disposed();
  private Disposable recipientRemovalSubscription = Disposables.disposed();

  PaymentsPresenter(
    @NonNull StringHelper stringHelper,
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull RecipientManager recipientManager,
    @NonNull SessionManager sessionManager,
    @NonNull ProductManager productManager,
    PosBridge posBridge,
    UserStore userStore,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    String authToken) {
    this.stringHelper = stringHelper;
    this.schedulerProvider = schedulerProvider;
    this.recipientManager = recipientManager;
    this.sessionManager = sessionManager;
    this.productManager = productManager;
    this.posBridge = posBridge;
    this.userStore = userStore;

    this.networkService = networkService;
    this.depApiBridge = depApiBridge;
    this.authToken = authToken;
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
          final Observable<Object> recipientsObservable = Observable
            .just(recipientManager.getAll(query))
            .compose(RxUtils.<Recipient>fromCollection())
            .cast(Object.class);
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
    Disposables.dispose(recipientAdditionSubscription);
    Disposables.dispose(recipientRemovalSubscription);
    RxUtils.unsubscribe(searchSubscription);
    RxUtils.unsubscribe(querySubscription);
    RxUtils.unsubscribe(queryBalanceSubscription);
    RxUtils.unsubscribe(signOutSubscription);
  }

  void addRecipient(@NonNull Recipient recipient) {
    assertScreen();
    recipientManager.add(recipient);
    screen.clearQuery();
    screen.showRecipientAdditionDialog(recipient);
  }

  void addRecipient(@NonNull final String phoneNumber) {
    assertScreen();
    if (recipientAdditionSubscription.isDisposed()) {
      recipientAdditionSubscription = Single
        .defer(new Callable<SingleSource<Result<Customer, ErrorCode>>>() {
          @Override
          public SingleSource<Result<Customer, ErrorCode>> call() throws Exception {
            final Result<Customer, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final ApiResult<Customer.State> customerStateResult = depApiBridge
                .fetchCustomerState(authToken, phoneNumber);
              if (customerStateResult.isSuccessful()) {
                if (Customer.checkIfCanBeFetched(customerStateResult.getData())) {
                  final ApiResult<Customer> customerResult = depApiBridge
                    .fetchCustomer(authToken, phoneNumber);
                  if (customerResult.isSuccessful()) {
                    result = Result.create(customerResult.getData());
                  } else {
                    final ApiError apiError = customerResult.getError();
                    result = Result.create(
                      FailureData.create(
                        ErrorCode.UNEXPECTED,
                        apiError.getDescription()));
                  }
                } else {
                  result = Result.create(FailureData.create(ErrorCode.NOT_AFFILIATED_CUSTOMER));
                }
              } else {
                final ApiError apiError = customerStateResult.getError();
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    apiError.getDescription()));
              }
            } else {
              result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
            }
            return Single.just(result);
          }
        })
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            screen.showLoadIndicator(true);
          }
        })
        .subscribe(new Consumer<Result<Customer, ErrorCode>>() {
          @Override
          public void accept(Result<Customer, ErrorCode> result) throws Exception {
            screen.hideLoadIndicator();
            if (result.isSuccessful()) {
              addRecipient(new PhoneNumberRecipient(phoneNumber, result.getSuccessData().getName()));
            } else {
              final FailureData<ErrorCode> failureData = result.getFailureData();
              switch (failureData.getCode()) {
                case NOT_AFFILIATED_CUSTOMER:
                  screen.startNonAffiliatedPhoneNumberRecipientAddition(phoneNumber);
                  break;
                case UNAVAILABLE_NETWORK:
                  screen.showUnavailableNetworkError();
                  break;
                default:
                  screen.showGenericErrorDialog(failureData.getDescription());
                  break;
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
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
    recipientManager.update(recipient);
    screen.update(recipient);
  }

  void startTransfer(@NonNull final String phoneNumber) {
    assertScreen();
    if (recipientAdditionSubscription.isDisposed()) {
      recipientAdditionSubscription = Single
        .defer(new Callable<SingleSource<Result<Pair<Boolean, String>, ErrorCode>>>() {
          @Override
          public SingleSource<Result<Pair<Boolean, String>, ErrorCode>> call() throws Exception {
            final Result<Pair<Boolean, String>, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final ApiResult<Customer.State> customerStateResult = depApiBridge
                .fetchCustomerState(authToken, phoneNumber);
              if (customerStateResult.isSuccessful()) {
                if (Customer.checkIfCanBeFetched(customerStateResult.getData())) {
                  final ApiResult<Customer> customerResult = depApiBridge
                    .fetchCustomer(authToken, phoneNumber);
                  if (customerResult.isSuccessful()) {
                    result = Result.create(Pair.create(true, customerResult.getData().getName()));
                  } else {
                    final ApiError apiError = customerStateResult.getError();
                    result = Result.create(
                      FailureData.create(
                        ErrorCode.UNEXPECTED,
                        apiError.getDescription()));
                  }
                } else {
                  result = Result.create(Pair.<Boolean, String>create(false, null));
                }
              } else {
                final ApiError apiError = customerStateResult.getError();
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    apiError.getDescription()));
              }
            } else {
              result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
            }
            return Single.just(result);
          }
        })
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            screen.showLoadIndicator(true);
          }
        })
        .subscribe(new Consumer<Result<Pair<Boolean, String>, ErrorCode>>() {
          @Override
          public void accept(Result<Pair<Boolean, String>, ErrorCode> result) throws Exception {
            screen.hideLoadIndicator();
            if (result.isSuccessful()) {
              final Pair<Boolean, String> successData = result.getSuccessData();
              final Recipient recipient;
              if (successData.first) {
                recipient = new PhoneNumberRecipient(phoneNumber, successData.second);
              } else {
                recipient = new NonAffiliatedPhoneNumberRecipient(phoneNumber);
              }
              screen.startTransfer(recipient);
            } else {
              final FailureData<ErrorCode> failureData = result.getFailureData();
              switch (failureData.getCode()) {
                case UNAVAILABLE_NETWORK:
                  screen.showUnavailableNetworkError();
                  break;
                default:
                  screen.showGenericErrorDialog(failureData.getDescription());
                  break;
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);
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
    screen.clearQuery();
    screen.showTransactionSummary(
      recipient,
      recipientManager.checkIfExists(recipient),
      transactionId);
  }

  final void signOut() {
    if (signOutSubscription.isUnsubscribed()) {
      posBridge.unregister(sessionManager.getSession().getPhoneNumber())
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
            screen.showLoadIndicator(false);
            if (result.isSuccessful()) {
              recipientManager.clear();
              productManager.clear();
              sessionManager.deactivate();
              userStore.clear();
              screen.openInitScreen();
              screen.finish();
            } else {
              screen.showGenericErrorDialog();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable);
            screen.showLoadIndicator(false);
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
      selectedRecipientList.clear();
      deleting = false;
      screen.setDeleting(false);
    }
  }

  final void resolve(final Recipient recipient) {
    if (deleting) {
      if (!selectedRecipientList.contains(recipient)) {
        recipient.setSelected(true);
        selectedRecipientList.add(recipient);
      } else {
        recipient.setSelected(false);
        selectedRecipientList.remove(recipient);
      }
      screen.update(recipient);
      screen.setDeleteButtonEnabled(!selectedRecipientList.isEmpty());
    } else if (recipient instanceof BillRecipient) {
      final BillRecipient billRecipient = (BillRecipient) recipient;
      if (Objects.checkIfNull(billRecipient.getBalance())) {
        queryBalanceSubscription = rx.Single
          .defer(new Callable<rx.Single<Result<BillBalance, ErrorCode>>>() {
            @Override
            public rx.Single<Result<BillBalance, ErrorCode>> call() throws Exception {
              final Result<BillBalance, ErrorCode> result;
              if (networkService.checkIfAvailable()) {
                final ApiResult<BillBalance> queryBalanceResult = depApiBridge
                  .queryBalance(authToken, billRecipient);
                if (queryBalanceResult.isSuccessful()) {
                  result = Result.create(queryBalanceResult.getData());
                } else {
                  result = Result.create(
                    FailureData.create(
                      ErrorCode.UNEXPECTED,
                      queryBalanceResult.getError().getDescription()));
                }
              } else {
                result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
              }
              return rx.Single.just(result);
            }
          })
          .doOnSubscribe(new Action0() {
            @Override
            public void call() {
              screen.showLoadIndicator(true);
            }
          })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<Result<BillBalance, ErrorCode>>() {
            @Override
            public void call(Result<BillBalance, ErrorCode> result) {
              if (result.isSuccessful()) {
                billRecipient.setBalance(result.getSuccessData());
                recipientManager.update(recipient);
                screen.update(billRecipient);
              } else {
                final FailureData<ErrorCode> failureData = result.getFailureData();
                switch (failureData.getCode()) {
                  case INCORRECT_PIN:
                    screen.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                    break;
                  case UNAVAILABLE_NETWORK:
                    screen.showUnavailableNetworkError();
                    break;
                  default:
                    screen.showGenericErrorDialog(failureData.getDescription());
                    break;
                }
              }
              screen.hideLoadIndicator();
            }
          }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              Timber.e(throwable);
              screen.hideLoadIndicator();
              screen.showGenericErrorDialog();
            }
          });
      } else {
        screen.startTransfer(billRecipient);
      }
    } else if (recipient instanceof ProductRecipient) {
      final ProductRecipient productRecipient = (ProductRecipient) recipient;
      if (Objects.checkIfNull(productRecipient.getBalance())) {
        queryBalanceSubscription = rx.Single
          .defer(new Callable<rx.Single<Result<ProductBillBalance, ErrorCode>>>() {
            @Override
            public rx.Single<Result<ProductBillBalance, ErrorCode>> call() throws Exception {
              final Result<ProductBillBalance, ErrorCode> result;
              if (networkService.checkIfAvailable()) {
                final ApiResult<ProductBillBalance> queryBalanceResult = depApiBridge
                  .queryBalance(authToken, productRecipient);
                if (queryBalanceResult.isSuccessful()) {
                  result = Result.create(queryBalanceResult.getData());
                } else {
                  result = Result.create(
                    FailureData.create(
                      ErrorCode.UNEXPECTED,
                      queryBalanceResult.getError().getDescription()));
                }
              } else {
                result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
              }
              return rx.Single.just(result);
            }
          })
          .doOnSubscribe(new Action0() {
            @Override
            public void call() {
              screen.showLoadIndicator(true);
            }
          })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<Result<ProductBillBalance, ErrorCode>>() {
            @Override
            public void call(Result<ProductBillBalance, ErrorCode> result) {
              if (result.isSuccessful()) {
                productRecipient.setBalance(result.getSuccessData());
                recipientManager.update(recipient);
                screen.update(productRecipient);
              } else {
                final FailureData<ErrorCode> failureData = result.getFailureData();
                switch (failureData.getCode()) {
                  case INCORRECT_PIN:
                    screen.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                    break;
                  case UNAVAILABLE_NETWORK:
                    screen.showUnavailableNetworkError();
                    break;
                  default:
                    screen.showGenericErrorDialog(failureData.getDescription());
                    break;
                }
              }
              screen.hideLoadIndicator();
            }
          }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              Timber.e(throwable);
              screen.hideLoadIndicator();
              screen.showGenericErrorDialog();
            }
          });
      } else {
        screen.startTransfer(productRecipient);
      }
    } else {
      screen.startTransfer(recipient);
    }
  }

  final void onPinRequestFinished(final String pin) {
    if (deleting) {
      recipientRemovalSubscription = Single
        .defer(new Callable<SingleSource<Result<Map<Recipient, Boolean>, ErrorCode>>>() {
          @Override
          public SingleSource<Result<Map<Recipient, Boolean>, ErrorCode>> call() throws Exception {
            final Result<Map<Recipient, Boolean>, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(authToken, pin);
              if (pinValidationResult.isSuccessful()) {
                if (pinValidationResult.getData()) {
                  final Map<Recipient, Boolean> resultMap = new HashMap<>();
                  for (Recipient recipient : selectedRecipientList) {
                    boolean resultFlag = true;
                    if (Recipient.checkIfBill(recipient)) {
                      final ApiResult<Void> recipientRemovalResult = depApiBridge
                        .removeBill(authToken, (BillRecipient) recipient, pin);
                      resultFlag = recipientRemovalResult.isSuccessful();
                    }
                    if (resultFlag) {
                      recipientManager.remove(recipient);
                    }
                    resultMap.put(recipient, resultFlag);
                  }
                  result = Result.create(resultMap);
                } else {
                  result = Result.create(FailureData.create(ErrorCode.INCORRECT_PIN));
                }
              } else {
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    pinValidationResult.getError().getDescription()));
              }
            } else {
              result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
            }
            return Single.just(result);
          }
        })
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Result<Map<Recipient, Boolean>, ErrorCode>>() {
          @Override
          public void accept(
            Result<Map<Recipient, Boolean>, ErrorCode> result) throws Exception {
            screen.setDeletingResult(result.isSuccessful());
            if (result.isSuccessful()) {
              stopDeleting();
              final Map<Recipient, Boolean> resultMap = result.getSuccessData();
              final StringBuilder resultMessageContentBuilder = new StringBuilder();
              for (Recipient recipient : resultMap.keySet()) {
                if (resultMap.get(recipient)) {
                  screen.remove(recipient);
                } else {
                  String label = recipient.getLabel();
                  if (Texts.checkIfEmpty(label)) {
                    label = recipient.getIdentifier();
                  }
                  if (Texts.checkIfEmpty(resultMessageContentBuilder)) {
                    resultMessageContentBuilder.append("\n");
                  }
                  resultMessageContentBuilder
                    .append("\t- ")
                    .append(label);
                }
              }
              if (Texts.checkIfNotEmpty(resultMessageContentBuilder)) {
                final String resultMessage = new StringBuilder(
                  "Los destinatarios listados a continuación no pudieron ser eliminados:")
                  .append("\n")
                  .append(resultMessageContentBuilder.toString())
                  .toString();
                screen.showGenericErrorDialog(resultMessage);
              }
            } else {
              final FailureData<ErrorCode> failureData = result.getFailureData();
              switch (failureData.getCode()) {
                case INCORRECT_PIN:
                  screen.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                  break;
                case UNAVAILABLE_NETWORK:
                  screen.showUnavailableNetworkError();
                  break;
                default:
                  screen.showGenericErrorDialog(failureData.getDescription());
                  break;
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Removing one or more recipients");
            screen.setDeletingResult(false);
            screen.showMessage(stringHelper.cannotProcessYourRequestAtTheMoment());
          }
        });
    }
  }

  final void deleteSelectedRecipients() {
    if (deleting) {
      screen.requestPin();
    }
  }
}
