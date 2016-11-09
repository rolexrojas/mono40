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
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
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

  private final NetworkHelper networkHelper;
  private final ApiBridge apiBridge;

  /**
   * Hash that uses an {@link Account account} as key and a {@link Pair pair} with the creation date
   * and the last queried {@link Balance balance} as getValue.
   */
  private final Map<Account, Pair<Long, Balance>> balances;

  /**
   * Emits an {@link Account account} every time its {@link Balance balance} expires.
   */
  private final PublishSubject<Account> subject = PublishSubject.create();

  private Subscription subscription = Subscriptions.unsubscribed();

  public BalanceManager(@NonNull NetworkHelper networkHelper, @NonNull ApiBridge apiBridge) {
    this.networkHelper = networkHelper;
    this.apiBridge = apiBridge;
    this.balances = new HashMap<>();
  }

  /**
   * Starts notifying observers.
   */
  public final void start() {
    subscription = Observable.interval(0L, 1L, TimeUnit.MINUTES) // One (1) minute intervals.
      .observeOn(AndroidSchedulers.mainThread())
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          for (Account account : balances.keySet()) {
            subject.onNext(account);
          }
          balances.clear();
        }
      })
      .subscribe(new Action1<Long>() {
        @Override
        public void call(Long interval) {
          for (Account account : balances.keySet()) {
            if ((System.currentTimeMillis() - balances.get(account).first) >= EXPIRATION_TIME) {
              Timber.d("%1$s balance expired", account.toString());
              subject.onNext(account);
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
   * Expires all queried {@link Balance balances} and stop notifying observers.
   */
  public final void stop() {
    RxUtils.unsubscribe(subscription);
  }

  /**
   * Creates an {@link Observable observable} that emits every {@link Account account} of which its
   * last queried {@link Balance balance} has expired.
   * <p>
   * <em>Note:</em> By default {@link #expiration()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits every {@link Account account} of which its
   * last queried {@link Balance balance} has expired.
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
   * Creates an {@link Observable observable} that queries the {@link Balance balance} of the given
   * {@link Account account}, saves and emit it.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(Account, String)} operates on {@link
   * Schedulers#io()}.
   *
   * @param account
   *   {@link Account} that will be queried.
   * @param pin
   *   User's PIN.
   *
   * @return An {@link Observable observable} that queries the {@link Balance balance} of the given
   * {@link Account account}, saves and emit it.
   */
  @NonNull
  public final Observable<Result<DomainCode, Balance>> queryBalance(@NonNull final Account account,
    @NonNull String pin) {
    return apiBridge.queryBalance(account, pin)
      .map(new Func1<Result<ApiCode, Balance>, Result<DomainCode, Balance>>() {
        @Override
        public Result<DomainCode, Balance> call(Result<ApiCode, Balance> apiResult) {
          if (ApiUtils.isSuccessful(apiResult)) {
            final Balance balance = apiResult.getData();
            if (Utils.isNotNull(balance)) {
              balances.put(account, Pair.create(System.currentTimeMillis(), balance));
            }
            return Result.create(DomainCode.SUCCESSFUL, balance);
          } else if (apiResult.getCode() == ApiCode.UNAUTHORIZED) {
            return Result.create(DomainCode.FAILURE_UNAUTHORIZED);
          } else {
            return Result.create(DomainCode.FAILURE_UNKNOWN);
          }
        }
      })
      .compose(DomainUtils.<Balance>assertNetwork(networkHelper))
      .subscribeOn(Schedulers.io());
  }
}
