package com.tpago.movil.dep.ui.main.products;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.BalanceExpirationEvent;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.util.Event;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.domain.util.EventType;
import com.tpago.movil.dep.ui.Presenter;
import com.tpago.movil.dep.ui.misc.UiUtils;

import java.util.List;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductsPresenter extends Presenter<ProductsScreen> {
  private final SchedulerProvider schedulerProvider;
  private final EventBus eventBus;
  private final ProductManager productManager;
  private final BalanceManager balanceManager;

  private CompositeSubscription compositeSubscription;

  ProductsPresenter(@NonNull SchedulerProvider schedulerProvider, @NonNull EventBus eventBus,
    @NonNull ProductManager productManager, @NonNull BalanceManager balanceManager) {
    this.schedulerProvider = schedulerProvider;
    this.eventBus = eventBus;
    this.productManager = productManager;
    this.balanceManager = balanceManager;
  }

  /**
   * TODO
   */
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
    subscription = productManager.getAll()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          UiUtils.showRefreshIndicator(screen);
        }
      })
      .doOnNext(new Action1<List<Product>>() {
        @Override
        public void call(List<Product> products) {
          screen.clear();
        }
      })
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          UiUtils.hideRefreshIndicator(screen);
          screen.add(new ShowRecentTransactionsItem());
        }
      })
      .compose(RxUtils.<Product>fromCollection())
      .subscribe(new Action1<Product>() {
        @Override
        public void call(Product product) {
          screen.add(new ProductItem(product));
          if (balanceManager.hasValidBalance(product)) {
            screen.setBalance(product, balanceManager.getBalance(product));
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all registered accounts");
        }
      });
    compositeSubscription.add(subscription);
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    if (Utils.isNotNull(compositeSubscription)) {
      RxUtils.unsubscribe(compositeSubscription);
      compositeSubscription = null;
    }
  }

  /**
   * TODO
   *
   * @param product
   *   TODO
   * @param pin
   *   TODO
   */
  void queryBalance(@NonNull final Product product, @NonNull final String pin) {
    assertScreen();
    if (balanceManager.hasValidBalance(product)) {
      screen.setBalance(product, balanceManager.getBalance(product));
    } else if (Utils.isNotNull(compositeSubscription)) {
      final Subscription subscription = balanceManager.queryBalance(product, pin)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe(new Action1<Pair<Boolean, Balance>>() {
          @Override
          public void call(Pair<Boolean, Balance> result) {
            screen.onBalanceQueried(result.first, product, result.second);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Querying the balance of an account (%1$s)", product);
            screen.onBalanceQueried(false, product, null);
            // TODO: Let the user know that an error occurred.
          }
        });
      compositeSubscription.add(subscription);
    }
  }
}
