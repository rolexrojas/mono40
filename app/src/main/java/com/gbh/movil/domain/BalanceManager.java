package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;

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
   * Hash that uses an {@link Account account} as key and a {@link Pair pair} with the creation date
   * and the last queried {@link Balance balance} as getValue.
   */
  private final Map<Account, Pair<Long, Balance>> balances;

  private Subscription subscription = Subscriptions.unsubscribed();

  public BalanceManager(@NonNull EventBus eventBus, @NonNull ApiBridge apiBridge) {
    this.eventBus = eventBus;
    this.apiBridge = apiBridge;
    this.balances = new HashMap<>();
  }

  /**
   * Starts checking for {@link Account account} {@link Balance balance} expiration.
   */
  public final void start() {
    subscription = Observable.interval(0L, 1L, TimeUnit.MINUTES) // One (1) minute intervals.
      .observeOn(AndroidSchedulers.mainThread())
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          for (Account account : balances.keySet()) {
            eventBus.dispatch(new BalanceExpirationEvent(account));
          }
          balances.clear();
        }
      })
      .subscribe(new Action1<Long>() {
        @Override
        public void call(Long interval) {
          Pair<Long, Balance> pair;
          for (Account account : balances.keySet()) {
            pair = balances.get(account);
            if ((System.currentTimeMillis() - pair.first) >= EXPIRATION_TIME) {
              Timber.d("Account's balance expired (%1$s, %2$s)", account, pair.second);
              eventBus.dispatch(new BalanceExpirationEvent(account));
              balances.remove(account);
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

  /**
   * Stops checking for {@link Account account} {@link Balance balance} expiration.
   */
  public final void stop() {
    RxUtils.unsubscribe(subscription);
  }

  /**
   * Indicates whether the given {@link Account account} has a valid {@link Balance balance} or
   * not.
   *
   * @param account
   *   {@link Account} that will be checked.
   *
   * @return True if it has a valid {@link Balance balance}, false otherwise.
   */
  public final boolean hasValidBalance(@NonNull Account account) {
    return balances.containsKey(account);
  }

  /**
   * Gets the last queried {@link Balance balance} of the given {@link Account account}.
   *
   * @param account
   *   {@link Account} that will be queried.
   *
   * @return {@link Account}'s last queried {@link Balance balance} or {@code null} if it hasn't
   * been queried.
   */
  @Nullable
  public final Balance getBalance(@NonNull Account account) {
    if (hasValidBalance(account)) {
      return balances.get(account).second;
    } else {
      return null;
    }
  }

  /**
   * TODO
   *
   * @param account
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Pair<Boolean, Balance>> queryBalance(@NonNull final Account account,
    @NonNull String pin) {
    return apiBridge.queryBalance(account, pin)
      .map(new Func1<Result<ApiCode, Balance>, Pair<Boolean, Balance>>() {
        @Override
        public Pair<Boolean, Balance> call(Result<ApiCode, Balance> result) {
          Balance balance = null;
          if (ApiUtils.isSuccessful(result)) {
            balance = result.getData();
            if (Utils.isNotNull(balance)) {
              balances.put(account, Pair.create(System.currentTimeMillis(), balance));
            }
          } else {
            Timber.d("Failed to query the balance of an account (%1$s, %2$s)", account, result);
          }
          return Pair.create(Utils.isNotNull(balance), balance);
        }
      });
  }
}
