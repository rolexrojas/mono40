package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.BalanceExpirationEvent;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.misc.UiUtils;

import java.util.List;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
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

  private CompositeSubscription compositeSubscription;

  ProductsPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull EventBus eventBus,
    @NonNull ProductManager productManager,
    @NonNull BalanceManager balanceManager) {
    this.schedulerProvider = schedulerProvider;
    this.eventBus = eventBus;
    this.productManager = productManager;
    this.balanceManager = balanceManager;
  }

  void start() {
    assertScreen();
    compositeSubscription = new CompositeSubscription();
    Subscription subscription = eventBus.onEventDispatched(
      EventType.PRODUCT_BALANCE_EXPIRATION)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(Event event) {
          if (event.getType().equals(EventType.PRODUCT_BALANCE_EXPIRATION)) {
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
      screen.add(new ProductItem(product));
      if (balanceManager.hasValidBalance(product)) {
        screen.setBalance(product, balanceManager.getBalance(product));
      }
    }
    screen.add(new ShowRecentTransactionsItem());
  }

  void stop() {
    assertScreen();
    if (Utils.isNotNull(compositeSubscription)) {
      RxUtils.unsubscribe(compositeSubscription);
      compositeSubscription = null;
    }
  }

  void queryBalance(@NonNull final Product product, @NonNull final String pin) {
    assertScreen();
    if (balanceManager.hasValidBalance(product)) {
      screen.setBalance(product, balanceManager.getBalance(product));
    } else if (Utils.isNotNull(compositeSubscription)) {
      final Subscription subscription = balanceManager.queryBalance(product, pin)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe(new Action1<ApiResult<Balance>>() {
          @Override
          public void call(ApiResult<Balance> result) {
            final boolean b = result.isSuccessful();
            final String m = b ? null : result.getError().getDescription();
            screen.onBalanceQueried(b, product, result.getData(), m);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Querying the balance of an account (%1$s)", product);
            screen.onBalanceQueried(false, product, null, null);
          }
        });
      compositeSubscription.add(subscription);
    }
  }
}
