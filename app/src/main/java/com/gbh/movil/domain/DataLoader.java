package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.api.ApiBridge;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class DataLoader {
  /**
   * TODO
   */
  private final ApiBridge apiBridge;
  /**
   * TODO
   */
  private final AccountRepository accountRepository;

  /**
   * TODO
   */
  private final BehaviorSubject<Set<Account>> accountsSubject = BehaviorSubject.create();
  /**
   * TODO
   */
  private final BehaviorSubject<List<Transaction>> transactionsSubject = BehaviorSubject.create();

  /**
   * TODO
   */
  public DataLoader(@NonNull ApiBridge apiBridge, @NonNull AccountRepository accountRepository) {
    this.apiBridge = apiBridge;
    this.accountRepository = accountRepository;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final Observable<Pair<Integer, Integer>> load() {
    return Observable.just(Pair.create(0, 0))
      .delay(2L, TimeUnit.SECONDS);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Set<Account>> accounts() {
    return accountsSubject.asObservable();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<List<Transaction>> recentTransactions() {
    return transactionsSubject.asObservable();
  }

  /**
   * TODO
   */
  public enum Result {
    /**
     * TODO
     */
    FAILED,

    /**
     * TODO
     */
    SUCCESS_WITH_NO_CHANGES,

    /**
     * TODO
     */
    SUCCESS_WITH_ACCOUNT_ADDITIONS,

    /**
     * TODO
     */
    SUCCESS_WITH_ACCOUNT_REMOVALS,

    /**
     * TODO
     */
    SUCCESS_WITH_ACCOUNT_ADDITIONS_AND_REMOVALS
  }
}
