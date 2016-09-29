package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
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
  public final Observable<Result> load() {
    final Observable<InitialData> localInitialDataObservable = accountRepository.getAll()
      .zipWith(Observable.just(new ArrayList<Transaction>()),
        new Func2<Set<Account>, ArrayList<Transaction>, InitialData>() {
          @Override
          public InitialData call(Set<Account> accounts, ArrayList<Transaction> transactions) {
            return new InitialData(accounts, transactions);
          }
        });
    return Observable.zip(localInitialDataObservable, apiBridge.initialLoad(),
      new Func2<InitialData, ApiResult<InitialData>, Result>() {
        @Override
        public Result call(InitialData localData, ApiResult<InitialData> result) {
          // Emits all the registered accounts saved locally.
          final Set<Account> localAccounts = localData.getAccounts();
          if (!localAccounts.isEmpty()) {
            accountsSubject.onNext(localAccounts);
          }
          // Emits all the transactions saved locally.
          final List<Transaction> localTransactions = localData.getTransactions();
          if (!localTransactions.isEmpty()) {
            transactionsSubject.onNext(localTransactions);
          }
          if (result.isSuccessful() && result.getData() != null) {
            final InitialData remoteData = result.getData();
            // Emits all the recent transactions retrieved from the API.
            final List<Transaction> remoteTransactions = remoteData.getTransactions();
            transactionsSubject.onNext(remoteTransactions);
            // Emits all the registered accounts retrieved from the API.
            final Set<Account> remoteAccounts = remoteData.getAccounts();
            accountsSubject.onNext(remoteAccounts);
            final Set<Account> addedAccounts = new HashSet<>();
            for (Account account : remoteAccounts) {
              if (!localAccounts.contains(account)) {
                addedAccounts.add(account);
                accountRepository.save(account);
              }
            }
            final Set<Account> deletedAccounts = new HashSet<>();
            for (Account account : localAccounts) {
              if (!remoteAccounts.contains(account)) {
                deletedAccounts.add(account);
                accountRepository.delete(account);
              }
            }
            if (!addedAccounts.isEmpty() && !deletedAccounts.isEmpty()) {
              return Result.SUCCESS_WITH_ACCOUNT_ADDITIONS_AND_REMOVALS;
            } else if (!addedAccounts.isEmpty()) {
              return Result.SUCCESS_WITH_ACCOUNT_ADDITIONS;
            } else if (!deletedAccounts.isEmpty()) {
              return Result.SUCCESS_WITH_ACCOUNT_REMOVALS;
            } else {
              return Result.SUCCESS_WITH_NO_CHANGES;
            }
          } else {
            return Result.FAILED;
          }
        }
      })
      .subscribeOn(Schedulers.io());
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
  public final Observable<List<Transaction>> transactions() {
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
