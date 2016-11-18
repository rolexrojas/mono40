package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Manager responsible of storing all queried {@link Balance balances} and notifying observers when
 * they expire. Every balance expires after a certain amount of time or when the manager is
 * stopped.
 *
 * @author hecvasro
 */
public final class BalanceManager {
  /**
   * Amount of time (milliseconds) that every balance will be kept alive.
   */
  private static final long EXPIRATION_TIME = 300000L; // Five (5) minutes.

  private final EventBus eventBus;
  private final ApiBridge apiBridge;

  /**
   * Key-value structured used to store the last queried {@link Balance balance} of a {@link Product
   * product}.
   */
  private final Map<Product, Pair<Long, Balance>> balances;

  private Subscription subscription = Subscriptions.unsubscribed();

  public BalanceManager(@NonNull EventBus eventBus, @NonNull ApiBridge apiBridge) {
    this.eventBus = eventBus;
    this.apiBridge = apiBridge;
    this.balances = new HashMap<>();
  }

  public final void start() {
    subscription = Observable.interval(0L, 1L, TimeUnit.MINUTES) // One (1) minute intervals.
      .observeOn(AndroidSchedulers.mainThread())
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          for (Product product : balances.keySet()) {
            eventBus.dispatch(new BalanceExpirationEvent(product));
          }
          balances.clear();
        }
      })
      .subscribe(new Action1<Long>() {
        @Override
        public void call(Long interval) {
          Pair<Long, Balance> pair;
          for (Product product : balances.keySet()) {
            pair = balances.get(product);
            if ((System.currentTimeMillis() - pair.first) >= EXPIRATION_TIME) {
              Timber.d("Account's balance expired (%1$s, %2$s)", product, pair.second);
              eventBus.dispatch(new BalanceExpirationEvent(product));
              balances.remove(product);
            }
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.d(throwable, "Removing expired balances");
        }
      });
  }

  public final void stop() {
    RxUtils.unsubscribe(subscription);
  }

  /**
   * Indicates whether the given {@link Product product} has a valid {@link Balance balance} or
   * not.
   *
   * @param product
   *   {@link Product} that will be checked.
   *
   * @return True if it has a valid {@link Balance balance}, false otherwise.
   */
  public final boolean hasValidBalance(@NonNull Product product) {
    return balances.containsKey(product);
  }

  /**
   * Gets the last queried {@link Balance balance} of the given {@link Product product}.
   *
   * @param product
   *   {@link Product} that will be queried.
   *
   * @return {@link Product}'s last queried {@link Balance balance} or {@code null}.
   */
  @Nullable
  public final Balance getBalance(@NonNull Product product) {
    if (hasValidBalance(product)) {
      return balances.get(product).second;
    } else {
      return null;
    }
  }

  /**
   * TODO
   *
   * @param product
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Pair<Boolean, Balance>> queryBalance(
    @NonNull final Product product, @NonNull String pin) {
    return apiBridge.queryBalance(product, pin)
      .compose(ApiUtils.handleApiResult(true, new Func1<ApiCode, Observable<Balance>>() {
        @Override
        public Observable<Balance> call(ApiCode code) {
          if (code.equals(ApiCode.FORBIDDEN)) {
            return Observable.just(null);
          } else {
            Timber.d("Failed to query the balance of a product (%1$s, %2$s)", product, code);
            return Observable.error(new Exception("Failed to query the balance of a product"));
          }
        }
      }))
      .map(new Func1<Balance, Pair<Boolean, Balance>>() {
        @Override
        public Pair<Boolean, Balance> call(Balance balance) {
          if (Utils.isNotNull(balance)) {
            balances.put(product, Pair.create(System.currentTimeMillis(), balance));
          }
          return Pair.create(Utils.isNotNull(balance), balance);
        }
      });
  }
}
