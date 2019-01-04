package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.ADD_ACCOUNT;
import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_ACCOUNT;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.RECHARGE;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.dep.User;
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
import com.tpago.movil.paypal.PayPalAccountRecipient;
import com.tpago.movil.paypal.PayPalAccountStore;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
//TODO: Set a timer for the loadingIndicators
final class RecipientCategoryPresenter extends Presenter<RecipientCategoryScreen> {

    private static final long DEFAULT_IME_SPAN_QUERY = 300L; // 0.3 seconds.

    private final User user;

    private final RecipientManager recipientManager;
    private final DepApiBridge depApiBridge;
    private final Category category;
    private final CategoryFilter categoryFilter;
    private final String userRecipientLabelForTransfers;
    private final String userRecipientLabelForRecharges;
    private final RecipientComparator recipientComparator;
    private final com.tpago.movil.d.data.StringHelper sh;
    private final NetworkService ns;
    private final PayPalAccountStore payPalAccountStore;

    private boolean deleting = false;
    private RecipientDeletionFilter recipientDeletableFilter
            = RecipientDeletionFilter.create(this.deleting);
    private List<Recipient> selectedRecipientList = new ArrayList<>();

    private Disposable queryDisposable = Disposables.disposed();
    private Disposable searchDisposable = Disposables.disposed();
    private Subscription signOutSubscription = Subscriptions.unsubscribed();
    private Subscription queryBalanceSubscription = Subscriptions.unsubscribed();

    private Disposable recipientAdditionDisposable = Disposables.disposed();
    private Disposable recipientTransactionDisposable = Disposables.disposed();
    private Disposable recipientRemovalDisposable = Disposables.disposed();

    RecipientCategoryPresenter(
            User user,
            @NonNull RecipientManager recipientManager,
            DepApiBridge depApiBridge,
            Category category,
            com.tpago.movil.d.data.StringHelper sh,
            NetworkService ns,
            PayPalAccountStore payPalAccountStore
    ) {
        this.user = user;
        this.recipientManager = recipientManager;
        this.depApiBridge = depApiBridge;
        this.category = category;
        this.categoryFilter = CategoryFilter.create(this.category);
        this.userRecipientLabelForTransfers = "Entre mis cuentas";
        this.userRecipientLabelForRecharges = "Mi teléfono";
        this.recipientComparator = RecipientComparator.create();
        this.sh = sh;
        this.ns = ns;
        this.payPalAccountStore = payPalAccountStore;
    }

    private Observable<Object> phoneNumberActions(String s) {
        final PhoneNumber phoneNumber = PhoneNumber.create(s);
        return Observable.just(TransactionWithPhoneNumberAction.create(phoneNumber))
                .cast(Object.class)
                .concatWith(Observable.just(AddPhoneNumberAction.create(phoneNumber)))
                .cast(Object.class);
    }

    final void start() {
        this.queryDisposable = this.screen.onQueryChanged()
                .flatMap((query) -> {
                    Observable<String> qo = Observable.just(StringHelper.emptyIfNull(query));
                    if (StringHelper.isNullOrEmpty(query)) {
                        qo = qo.debounce(DEFAULT_IME_SPAN_QUERY, MILLISECONDS);
                    }
                    return qo;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((query) -> {
                    DisposableUtil.dispose(this.searchDisposable);

                    final MatchableFilter matchableFilter = MatchableFilter.create(query);

                    final Observable<Recipient> userRecipientSource;
                    if (!this.deleting && (this.category == TRANSFER || this.category == RECHARGE)) {
                        final String label;
                        if (this.category == TRANSFER) {
                            label = this.userRecipientLabelForTransfers;
                        } else {
                            label = this.userRecipientLabelForRecharges;
                        }
                        final UserRecipient userRecipient = new UserRecipient(user);
                        userRecipient.setLabel(label);

                        userRecipientSource = Observable.just(userRecipient)
                                .cast(Recipient.class);
                    } else {
                        userRecipientSource = Observable.empty();
                    }

                    Observable<Recipient> payPalAccountRecipients = Observable.empty();
                    if (!this.deleting && this.category == RECHARGE) {
                        payPalAccountRecipients = this.payPalAccountStore.getAll()
                                .flatMapObservable(Observable::fromIterable)
                                .map(PayPalAccountRecipient::create);
                    }

                    Observable<Object> source = Observable.fromIterable(this.recipientManager.getAll())
                            .filter(this.categoryFilter)
                            .concatWith(userRecipientSource)
                            .concatWith(payPalAccountRecipients)
                            .filter(matchableFilter)
                            .filter(this.recipientDeletableFilter)
                            .toSortedList(this.recipientComparator)
                            .flatMapObservable(Observable::fromIterable)
                            .cast(Object.class);

                    if (!this.deleting && !StringHelper.isNullOrEmpty(query)) {
                        Observable<Object> actions = Observable.empty();

                        if (DigitUtil.containsOnlyDigits(query)) {
                            if (this.category == TRANSFER) {
                                if (PhoneNumber.isValidWithAdditionalCode(query)) {
                                    actions = phoneNumberActions(query);
                                } else if (AccountAction.isProductNumber(query)) {
                                    actions = Observable.just(AccountAction.create(TRANSACTION_WITH_ACCOUNT, query))
                                            .cast(Object.class)
                                            .concatWith(Observable.just(AccountAction.create(ADD_ACCOUNT, query)))
                                            .cast(Object.class);
                                }
                            } else if (this.category == RECHARGE && PhoneNumber.isValidWithAdditionalCode(query)) {
                                actions = phoneNumberActions(query);
                            }
                        }

                        source = source.switchIfEmpty(actions);
                    }

                    if (this.screen != null) {
                        this.searchDisposable = source
                                .switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe((disposable) -> {
                                    if (this.screen != null) {
                                        this.screen.clear();
                                    }
                                })
                                .subscribe(this.screen::add);
                    }
                });
    }

    final void stop() {
        DisposableUtil.dispose(this.recipientAdditionDisposable);
        DisposableUtil.dispose(this.recipientRemovalDisposable);
        RxUtils.unsubscribe(this.queryBalanceSubscription);
        RxUtils.unsubscribe(this.signOutSubscription);
    }

    private void showTakeoverLoader() {
        this.screen.showLoadIndicator(true);
    }

    private void showTakeoverLoader(Disposable disposable) {
        this.showTakeoverLoader();
    }

    private void hideTakeoverLoader() {
        this.screen.hideLoadIndicator();
    }

    private void handleError(Throwable throwable, String message) {
        Timber.e(throwable, message);
        this.screen.showMessage(this.sh.cannotProcessYourRequestAtTheMoment());
    }

    private Recipient handleCustomerResult(PhoneNumber phoneNumber, ApiResult<Customer> result) {
        String customerName = null;
        if (result.isSuccessful()) {
            customerName = result.getData()
                    .getName();
        }
        if (com.tpago.movil.util.StringHelper.isNullOrEmpty(customerName)) {
            return new NonAffiliatedPhoneNumberRecipient(phoneNumber, customerName);
        } else {
            return new PhoneNumberRecipient(phoneNumber, customerName);
        }
    }

    private void handleAddPhoneNumberRecipientError(Throwable throwable) {
        this.handleError(throwable, "Adding a phone number recipient");
    }

    private void handleAddPhoneNumberRecipientResult(
            PhoneNumber phoneNumber,
            ApiResult<Customer> result,
            Category category
    ) {
        this.addRecipient(this.handleCustomerResult(phoneNumber, result), category);
    }

    final void addRecipient(@NonNull Recipient recipient, Category category) {
        if (!(recipient instanceof UserRecipient)) {
            this.recipientManager.add(recipient);
            this.screen.clearQuery();
            if (category == Category.RECHARGE) {
                if ((recipient.getType() == RecipientType.PHONE_NUMBER &&
                        (recipient instanceof PhoneNumberRecipient) && ((PhoneNumberRecipient) recipient).getCarrier() == null)
                        || (recipient.getType() == RecipientType.NON_AFFILIATED_PHONE_NUMBER
                        && ((NonAffiliatedPhoneNumberRecipient) recipient).getCarrier() == null)
                ) {
                    this.screen.showRecipientAdditionCarrierSelection(recipient);
                } else {
                    this.screen.showRecipientAdditionBankSelection(recipient);
                }
            } else {
                if ((recipient instanceof AccountRecipient && ((AccountRecipient) recipient).bank() == null)) {
                    this.screen.showRecipientAdditionBankSelection(recipient);
                } else {
                    this.screen.showRecipientAdditionDialog(recipient);
                }
            }
        }
    }

    void onCarrierSelected(Recipient recipient) {
        this.screen.showRecipientAdditionDialog(recipient);
    }

    final void addRecipient(@NonNull final PhoneNumber phoneNumber, @NonNull final Category category) {
        if (this.recipientAdditionDisposable.isDisposed()) {
            this.recipientAdditionDisposable = Single
                    .defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (result) -> this.handleAddPhoneNumberRecipientResult(phoneNumber, result, category),
                            this::handleAddPhoneNumberRecipientError
                    );
        }
    }

    final void updateRecipient(@NonNull Recipient recipient, @Nullable String label) {
        if (!(recipient instanceof UserRecipient)) {
            recipient.setLabel(label);
            this.recipientManager.update(recipient);
            this.screen.update(recipient);
        }
    }

    private void handleStartTransactionError(Throwable throwable) {
        this.handleError(throwable, "Starting a phone number transaction");
    }

    private void handleStartPhoneNumberTransactionResult(
            PhoneNumber phoneNumber,
            ApiResult<Customer> result
    ) {
        this.screen.startTransaction(this.handleCustomerResult(phoneNumber, result));
    }

    final void startTransfer(@NonNull final PhoneNumber phoneNumber) {
        if (this.recipientTransactionDisposable.isDisposed()) {
            this.recipientTransactionDisposable = Single
                    .defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
        final boolean isUser = recipient instanceof UserRecipient;
        if (isUser) {
            final UserRecipient userRecipient = (UserRecipient) recipient;

            final Partner carrierA = this.user.carrier();
            final Partner carrierB = userRecipient.getCarrier();
            if (ObjectHelper.isNotNull(carrierB) && !carrierB.equals(carrierA)) {
                this.user.carrier(carrierB);
            }
        }

        this.screen.clearQuery();
        this.screen.showTransactionSummary(
                recipient,
                isUser || this.recipientManager.checkIfExists(recipient),
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
        if (this.deleting) {
            if (!this.selectedRecipientList.contains(recipient)) {
                recipient.setSelected(true);
                this.selectedRecipientList.add(recipient);
            } else {
                recipient.setSelected(false);
                this.selectedRecipientList.remove(recipient);
            }
            this.screen.update(recipient);
            this.screen.setDeleteButtonEnabled(!selectedRecipientList.isEmpty());
        } else if (recipient instanceof BillRecipient) {
            final BillRecipient billRecipient = (BillRecipient) recipient;
            if (ObjectHelper.isNull(billRecipient.getBalance())) {
                this.queryBalanceSubscription = rx.Single
                        .defer(new Callable<rx.Single<Result<BillBalance, ErrorCode>>>() {
                            @Override
                            public rx.Single<Result<BillBalance, ErrorCode>> call() throws Exception {
                                final Result<BillBalance, ErrorCode> result;
                                if (ns.checkIfAvailable()) {
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
                        .subscribeOn(rx.schedulers.Schedulers.io())
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.isSuccessful()) {
                                billRecipient.setBalance(result.getSuccessData());
                                recipientManager.update(recipient);
                                screen.update(billRecipient);
                            } else {
                                final FailureData<ErrorCode> failureData = result.getFailureData();
                                switch (failureData.getCode()) {
                                    case INCORRECT_PIN:
                                        screen.showGenericErrorDialog(sh.resolve(R.string.error_incorrect_pin));
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
                        }, throwable -> {
                            Timber.e(throwable);
                            screen.hideLoadIndicator();
                            screen.showGenericErrorDialog();
                        });
            } else {
                this.screen.startTransaction(billRecipient);
            }
        } else if (recipient instanceof ProductRecipient) {
            final ProductRecipient productRecipient = (ProductRecipient) recipient;
            if (ObjectHelper.isNull(productRecipient.getBalance())) {
                queryBalanceSubscription = rx.Single
                        .defer(() -> {
                            final Result<ProductBillBalance, ErrorCode> result;
                            if (ns.checkIfAvailable()) {
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
                        })
                        .doOnSubscribe(() -> screen.showLoadIndicator(true))
                        .subscribeOn(rx.schedulers.Schedulers.io())
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.isSuccessful()) {
                                productRecipient.setBalance(result.getSuccessData());
                                recipientManager.update(recipient);
                                screen.update(productRecipient);
                            } else {
                                final FailureData<ErrorCode> failureData = result.getFailureData();
                                switch (failureData.getCode()) {
                                    case INCORRECT_PIN:
                                        screen.showGenericErrorDialog(sh.resolve(R.string.error_incorrect_pin));
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
                        }, throwable -> {
                            Timber.e(throwable);
                            screen.hideLoadIndicator();
                            screen.showGenericErrorDialog();
                        });
            } else {
                screen.startTransaction(productRecipient);
            }
        } else if (recipient instanceof PayPalAccountRecipient) {
            this.screen.startPayPalTransaction(((PayPalAccountRecipient) recipient).reference());
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
                            if (ns.checkIfAvailable()) {
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
                    .subscribe(result -> {
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
                                    if (StringHelper.isNullOrEmpty(label)) {
                                        label = recipient.getIdentifier();
                                    }
                                    if (StringHelper.isNullOrEmpty(resultMessageContentBuilder)) {
                                        resultMessageContentBuilder.append("\n");
                                    }
                                    resultMessageContentBuilder
                                            .append("\t- ")
                                            .append(label);
                                }
                            }
                            if (!StringHelper.isNullOrEmpty(resultMessageContentBuilder)) {
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
                                    screen.showGenericErrorDialog(sh.resolve(R.string.error_incorrect_pin));
                                    break;
                                case UNAVAILABLE_NETWORK:
                                    screen.showUnavailableNetworkError();
                                    break;
                                default:
                                    screen.showGenericErrorDialog(failureData.getDescription());
                                    break;
                            }
                        }
                    }, throwable -> {
                        Timber.e(throwable, "Removing one or more recipients");
                        screen.setDeletingResult(false);
                        screen.showMessage(sh.cannotProcessYourRequestAtTheMoment());
                    });
        }
    }

    final void deleteSelectedRecipients() {
        if (deleting) {
            screen.requestPin();
        }
    }
}
