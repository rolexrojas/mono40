package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

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

  private final ApiBridge apiBridge;

  /**
   * Hash that uses an {@link Account account} as key and a {@link Pair pair} with the creation date
   * and the last queried {@link Balance balance} as value.
   */
  private final Map<Account, Pair<Long, Balance>> balances;

  /**
   * Emits an {@link Account account} every time its {@link Balance balance} expires.
   */
  private final PublishSubject<Account> subject = PublishSubject.create();

  private Subscription subscription = Subscriptions.unsubscribed();

  public BalanceManager(@NonNull ApiBridge apiBridge) {
    this.apiBridge = apiBridge;
    this.balances = new HashMap<>();
  }

  /**
   * Starts notifying observers.
   */
  public final void start() {
    subscription = Observable.interval(0L, 1L, TimeUnit.MINUTES) // One (1) minute intervals.
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Long>() {
        @Override
        public void call(Long interval) {
          long creationDate;
          for (Account account : balances.keySet()) {
            creationDate = balances.get(account).first;
            if ((System.currentTimeMillis() - creationDate) >= EXPIRATION_TIME) {
              subject.onNext(account);
              balances.remove(account);
            }
          }
        }
      });
  }

  /**
   * Expires all queried {@link Balance balances} and stop notifying observers.
   */
  public final void stop() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
    for (Account account : balances.keySet()) {
      subject.onNext(account);
      balances.remove(account);
    }
  }

  /**
   * Returns an {@link Observable observable} that emits every {@link Account account} that its last
   * queried {@link Balance balance} has expired.
   *
   * @return An {@link Observable observable} that emits every {@link Account account} that its last
   * queried {@link Balance balance} has expired.
   */
  public final Observable<Account> expiration() {
    return subject.asObservable();
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
   * Queries the {@link Balance balance} of the given {@link Account account} and stores it until
   * expiration.
   *
   * @param account
   *   {@link Account} that will be queried.
   * @param pin
   *   User's PIN.
   *
   * @return {@link Account}'s queried balance.
   */
  @NonNull
  public final Observable<ApiResult<Balance>> queryBalance(@NonNull final Account account,
    @NonNull String pin) {
    return apiBridge.queryBalance(account, pin)
      .doOnNext(new Action1<ApiResult<Balance>>() {
        @Override
        public void call(ApiResult<Balance> result) {
          if (result.isSuccessful()) {
            final Balance balance = result.getData();
            if (balance != null) {
              balances.put(account, Pair.create(System.currentTimeMillis(), balance));
            }
          }
        }
      });
  }
}
