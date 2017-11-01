package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.ADD_ACCOUNT;
import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_ACCOUNT;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.RECHARGE;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.Partner;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.dep.User;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.util.DigitHelper;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class RecipientCategoryPresenter extends Presenter<RecipientCategoryScreen> {

  private static final long DEFAULT_IME_SPAN_QUERY = 300L; // 0.3 seconds.

  private final User user;

  private final StringHelper stringHelper;
  private final RecipientManager recipientManager;
  private final NetworkService networkService;
  private final DepApiBridge depApiBridge;
  private final Category category;
  private final CategoryFilter categoryFilter;
  private final String userRecipientLabelForTransfers;
  private final String userRecipientLabelForRecharges;
  private final RecipientComparator recipientComparator;

  private boolean deleting = false;
  private RecipientDeletionFilter recipientDeletableFilter = RecipientDeletionFilter
    .create(this.deleting);
  private List<Recipient> selectedRecipientList = new ArrayList<>();

  private Subscription querySubscription = Subscriptions.unsubscribed();
  private Subscription searchSubscription = Subscriptions.unsubscribed();
  private Subscription signOutSubscription = Subscriptions.unsubscribed();
  private Subscription queryBalanceSubscription = Subscriptions.unsubscribed();

  private Disposable recipientAdditionDisposable = Disposables.disposed();
  private Disposable recipientTransactionDisposable = Disposables.disposed();
  private Disposable recipientRemovalDisposable = Disposables.disposed();

  RecipientCategoryPresenter(
    User user,
    @NonNull StringHelper stringHelper,
    @NonNull RecipientManager recipientManager,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    Category category
  ) {
    this.user = user;
    this.stringHelper = stringHelper;
    this.recipientManager = recipientManager;
    this.networkService = networkService;
    this.depApiBridge = depApiBridge;
    this.category = category;
    this.categoryFilter = CategoryFilter.create(this.category);
    this.userRecipientLabelForTransfers = "Entre mis cuentas";
    this.userRecipientLabelForRecharges = "Mi teléfono";
    this.recipientComparator = RecipientComparator.create();
  }

  private Observable<Object> phoneNumberActions(String s) {
    final PhoneNumber phoneNumber = PhoneNumber.create(s);
    return Observable.just(TransactionWithPhoneNumberAction.create(phoneNumber))
      .cast(Object.class)
      .concatWith(Observable.just(AddPhoneNumberAction.create(phoneNumber)))
      .cast(Object.class);
  }

  final void start() {
    assertScreen();
    querySubscription = screen.onQueryChanged()
      .flatMap(new Func1<String, Observable<String>>() {
        @Override
        public Observable<String> call(String s) {
          Observable<String> o = Observable.just(s);
          if (s != null && !s.isEmpty()) {
            o = o.debounce(DEFAULT_IME_SPAN_QUERY, MILLISECONDS);
          }
          return o;
        }
      })
      .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
      .subscribe(new Action1<String>() {
        @Override
        public void call(final String query) {
          RxUtils.unsubscribe(searchSubscription);

          final MatchableFilter matchableFilter = MatchableFilter.create(query);

          final Observable<Recipient> userRecipientSource;
          if (!deleting && (category == TRANSFER || category == Category.RECHARGE)) {
            final String label;
            if (category == TRANSFER) {
              label = userRecipientLabelForTransfers;
            } else {
              label = userRecipientLabelForRecharges;
            }
            final UserRecipient userRecipient = new UserRecipient(user);
            userRecipient.setLabel(label);

            userRecipientSource = Observable.just(userRecipient)
              .cast(Recipient.class);
          } else {
            userRecipientSource = Observable.empty();
          }

          Observable<Object> source = Observable.from(recipientManager.getAll())
            .filter(categoryFilter)
            .concatWith(userRecipientSource)
            .filter(matchableFilter)
            .filter(recipientDeletableFilter)
            .toSortedList(recipientComparator)
            .compose(RxUtils.fromCollection())
            .cast(Object.class);

          if (!deleting && (query != null && !query.isEmpty())) {
            Observable<Object> actions = Observable.empty();

            if (DigitHelper.containsOnlyDigits(query)) {
              if (category == TRANSFER) {
                if (PhoneNumber.isValid(query)) {
                  actions = phoneNumberActions(query);
                } else if (AccountAction.isProductNumber(query)) {
                  actions = Observable.just(AccountAction.create(TRANSACTION_WITH_ACCOUNT, query))
                    .cast(Object.class)
                    .concatWith(Observable.just(AccountAction.create(ADD_ACCOUNT, query)))
                    .cast(Object.class);
                }
              } else if (category == RECHARGE && PhoneNumber.isValid(query)) {
                actions = phoneNumberActions(query);
              }
            }

            source = source.switchIfEmpty(actions);
          }

          searchSubscription = source
            .switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
            .subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
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

  final void stop() {
    DisposableHelper.dispose(this.recipientAdditionDisposable);
    DisposableHelper.dispose(this.recipientRemovalDisposable);

    RxUtils.unsubscribe(this.searchSubscription);
    RxUtils.unsubscribe(this.querySubscription);
    RxUtils.unsubscribe(this.queryBalanceSubscription);
    RxUtils.unsubscribe(this.signOutSubscription);
  }

  private void showTakeoverLoader(Disposable disposable) {
    this.screen.showLoadIndicator(true);
  }

  private void hideTakeoverLoader() {
    this.screen.hideLoadIndicator();
  }

  private void handleError(Throwable throwable, String message) {
    Timber.e(throwable, message);
    this.screen.showMessage(this.stringHelper.cannotProcessYourRequestAtTheMoment());
  }

  private String handleCustomerResult(ApiResult<Customer> result) {
    String customerName = null;
    if (result.isSuccessful()) {
      customerName = result.getData()
        .getName();
    }
    return customerName;
  }

  private void handleAddPhoneNumberRecipientError(Throwable throwable) {
    this.handleError(throwable, "Adding a phone number recipient");
  }

  private void handleAddPhoneNumberRecipientResult(PhoneNumber phoneNumber, ApiResult<Customer> result) {
    this.addRecipient(new PhoneNumberRecipient(phoneNumber, this.handleCustomerResult(result)));
  }

  final void addRecipient(@NonNull Recipient recipient) {
    if (!(recipient instanceof UserRecipient)) {
      this.recipientManager.add(recipient);
      this.screen.clearQuery();
      this.screen.showRecipientAdditionDialog(recipient);
    }
  }

  final void addRecipient(@NonNull final PhoneNumber phoneNumber) {
    if (!this.recipientAdditionDisposable.isDisposed()) {
      this.recipientAdditionDisposable = Single
        .defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this::showTakeoverLoader)
        .doFinally(this::hideTakeoverLoader)
        .subscribe(
          (result) -> this.handleAddPhoneNumberRecipientResult(phoneNumber, result),
          this::handleAddPhoneNumberRecipientError
        );
    }
  }

  final void updateRecipient(@NonNull Recipient recipient, @Nullable String label) {
    assertScreen();
    if (!(recipient instanceof UserRecipient)) {
      recipient.setLabel(label);
      recipientManager.update(recipient);
      screen.update(recipient);
    }
  }

  private void handleStartTransactionError(Throwable throwable) {
    this.handleError(throwable, "Starting a phone number transaction");
  }

  private void handleStartPhoneNumberTransactionResult(PhoneNumber phoneNumber, ApiResult<Customer> result) {
    this.screen.startTransaction(new PhoneNumberRecipient(phoneNumber, this.handleCustomerResult(result)));
  }

  final void startTransfer(@NonNull final PhoneNumber phoneNumber) {
    if (this.recipientTransactionDisposable.isDisposed()) {
      this.recipientTransactionDisposable = Single
        .defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this::showTakeoverLoader)
        .doFinally(this::hideTakeoverLoader)
        .subscribe(
          (result) -> this.handleStartPhoneNumberTransactionResult(phoneNumber, result),
          this::handleStartTransactionError
        );
    }
  }

  final void startTransfer(String accountNumber) {
    this.screen.startTransaction(
      AccountRecipient.builder()
        .number(accountNumber)
        .build()
    );
  }

  final void showTransactionSummary(final Recipient recipient, final String transactionId) {
    assertScreen();

    final boolean isUser = recipient instanceof UserRecipient;
    if (isUser) {
      final UserRecipient userRecipient = (UserRecipient) recipient;

      final Partner carrierA = user.carrier();
      final Partner carrierB = userRecipient.getCarrier();
      if (ObjectHelper.isNotNull(carrierB) && !carrierB.equals(carrierA)) {
        user.carrier(carrierB);
      }
    }

    screen.clearQuery();
    screen.showTransactionSummary(
      recipient,
      isUser || recipientManager.checkIfExists(recipient),
      transactionId
    );
  }

  final void startDeleting() {
    if (!this.deleting) {
      this.deleting = true;
      this.recipientDeletableFilter = RecipientDeletionFilter.create(true);

      this.screen.setDeleting(true);
      this.screen.clearQuery();
    }
  }

  final void stopDeleting() {
    if (this.deleting) {
      this.selectedRecipientList.clear();

      this.deleting = false;
      this.recipientDeletableFilter = RecipientDeletionFilter.create(false);

      this.screen.setDeleting(false);
      this.screen.clearQuery();
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
      if (ObjectHelper.isNull(billRecipient.getBalance())) {
        queryBalanceSubscription = rx.Single
          .defer(new Callable<rx.Single<Result<BillBalance, ErrorCode>>>() {
            @Override
            public rx.Single<Result<BillBalance, ErrorCode>> call() throws Exception {
              final Result<BillBalance, ErrorCode> result;
              if (networkService.checkIfAvailable()) {
                final ApiResult<BillBalance> queryBalanceResult = depApiBridge
                  .queryBalance(billRecipient);
                if (queryBalanceResult.isSuccessful()) {
                  result = Result.create(queryBalanceResult.getData());
                } else {
                  result = Result.create(
                    FailureData.create(
                      ErrorCode.UNEXPECTED,
                      queryBalanceResult.getError()
                        .getDescription()
                    ));
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
          .subscribeOn(rx.schedulers.Schedulers.io())
          .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
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
        screen.startTransaction(billRecipient);
      }
    } else if (recipient instanceof ProductRecipient) {
      final ProductRecipient productRecipient = (ProductRecipient) recipient;
      if (ObjectHelper.isNull(productRecipient.getBalance())) {
        queryBalanceSubscription = rx.Single
          .defer(new Callable<rx.Single<Result<ProductBillBalance, ErrorCode>>>() {
            @Override
            public rx.Single<Result<ProductBillBalance, ErrorCode>> call() throws Exception {
              final Result<ProductBillBalance, ErrorCode> result;
              if (networkService.checkIfAvailable()) {
                final ApiResult<ProductBillBalance> queryBalanceResult = depApiBridge
                  .queryBalance(productRecipient);
                if (queryBalanceResult.isSuccessful()) {
                  result = Result.create(queryBalanceResult.getData());
                } else {
                  result = Result.create(
                    FailureData.create(
                      ErrorCode.UNEXPECTED,
                      queryBalanceResult.getError()
                        .getDescription()
                    ));
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
          .subscribeOn(rx.schedulers.Schedulers.io())
          .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
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
        screen.startTransaction(productRecipient);
      }
    } else {
      screen.startTransaction(recipient);
    }
  }

  final void onPinRequestFinished(final String pin) {
    if (deleting) {
      recipientRemovalDisposable = Single
        .defer(new Callable<SingleSource<Result<Map<Recipient, Boolean>, ErrorCode>>>() {
          @Override
          public SingleSource<Result<Map<Recipient, Boolean>, ErrorCode>> call() throws Exception {
            final Result<Map<Recipient, Boolean>, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(pin);
              if (pinValidationResult.isSuccessful()) {
                if (pinValidationResult.getData()) {
                  final Map<Recipient, Boolean> resultMap = new HashMap<>();
                  for (Recipient recipient : selectedRecipientList) {
                    boolean resultFlag = true;
                    if (Recipient.checkIfBill(recipient)) {
                      final ApiResult<Void> recipientRemovalResult = depApiBridge
                        .removeBill((BillRecipient) recipient, pin);
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
                    pinValidationResult.getError()
                      .getDescription()
                  ));
              }
            } else {
              result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
            }
            return Single.just(result);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Result<Map<Recipient, Boolean>, ErrorCode>>() {
          @Override
          public void accept(
            Result<Map<Recipient, Boolean>, ErrorCode> result
          ) throws Exception {
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
                  if (com.tpago.movil.util.StringHelper.isNullOrEmpty(label)) {
                    label = recipient.getIdentifier();
                  }
                  if (com.tpago.movil.util.StringHelper.isNullOrEmpty(resultMessageContentBuilder)) {
                    resultMessageContentBuilder.append("\n");
                  }
                  resultMessageContentBuilder
                    .append("\t- ")
                    .append(label);
                }
              }
              if (!com.tpago.movil.util.StringHelper.isNullOrEmpty(resultMessageContentBuilder)) {
                final String resultMessage = new StringBuilder("Error al eliminar:")
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
