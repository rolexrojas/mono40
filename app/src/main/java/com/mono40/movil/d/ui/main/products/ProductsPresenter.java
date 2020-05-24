package com.mono40.movil.d.ui.main.products;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.mono40.movil.R;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.Balance;
import com.mono40.movil.d.domain.BalanceExpirationEvent;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.ErrorCode;
import com.mono40.movil.d.domain.FailureData;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.Result;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.util.Event;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.domain.util.EventType;
import com.mono40.movil.d.misc.rx.RxUtils;
import com.mono40.movil.d.ui.Presenter;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.util.ObjectHelper;

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
public class ProductsPresenter extends Presenter<ProductsScreen> {

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
