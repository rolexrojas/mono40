package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.R;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.BalanceExpirationEvent;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.util.ObjectHelper;

import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
class ProductsPresenter extends Presenter<ProductsScreen> {

    private final SchedulerProvider schedulerProvider;
    private final EventBus eventBus;
    private final ProductManager productManager;
    private final BalanceManager balanceManager;

    private final NetworkService networkService;
    private final DepApiBridge depApiBridge;
    private final StringHelper stringHelper;

    private CompositeSubscription compositeSubscription;

    ProductsPresenter(
            @NonNull SchedulerProvider schedulerProvider,
            @NonNull EventBus eventBus,
            @NonNull ProductManager productManager,
            @NonNull BalanceManager balanceManager,
            NetworkService networkService,
            DepApiBridge depApiBridge,
            StringHelper stringHelper
    ) {
        this.schedulerProvider = schedulerProvider;
        this.eventBus = eventBus;
        this.productManager = productManager;
        this.balanceManager = balanceManager;

        this.networkService = networkService;
        this.depApiBridge = depApiBridge;
        this.stringHelper = stringHelper;
    }

    final void start() {
        assertScreen();
        compositeSubscription = new CompositeSubscription();
        Subscription subscription = eventBus.onEventDispatched(
                EventType.PRODUCT_BALANCE_EXPIRATION)
                .observeOn(schedulerProvider.ui())
                .subscribe(new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        if (event.getType()
                                .equals(EventType.PRODUCT_BALANCE_EXPIRATION)) {
                            screen.setBalance(((BalanceExpirationEvent) event).getProduct(), null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "Listening to balance expiration events");
                    }
                });
        compositeSubscription.add(subscription);
        screen.clear();
        for (Product product : productManager.getProductList()) {
            screen.add(ProductItem.create(product));
            if (balanceManager.hasValidBalance(product)) {
                screen.setBalance(product, balanceManager.getBalance(product));
            }
        }
        screen.add(new ShowRecentTransactionsItem());
    }

    final void stop() {
        assertScreen();
        if (ObjectHelper.isNotNull(compositeSubscription)) {
            RxUtils.unsubscribe(compositeSubscription);
            compositeSubscription = null;
        }
    }

    void queryBalance(@NonNull final Product product, @NonNull final String pin) {
        assertScreen();
        if (ObjectHelper.isNotNull(compositeSubscription)) {
            final Subscription subscription = Single
                    .defer(() -> {
                        final Result<Pair<Long, Balance>, ErrorCode> result;
                        if (networkService.checkIfAvailable()) {
                            final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(pin);
                            if (pinValidationResult.isSuccessful()) {
                                if (pinValidationResult.getData()) {
                                    final ApiResult<Pair<Long, Balance>> queryBalanceResult = balanceManager
                                            .queryBalance(product, pin);
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
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (!result.isSuccessful()) {
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
                        screen.onBalanceQueried(product, result.getSuccessData());
                    }, throwable -> {
                        Timber.e(throwable);
                        screen.showGenericErrorDialog();
                        screen.onBalanceQueried(product, null);
                    });
            compositeSubscription.add(subscription);
        }
    }
}
