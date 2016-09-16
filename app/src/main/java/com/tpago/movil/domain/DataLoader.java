package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.api.ApiResult;

import java.util.HashSet;
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
  private final ApiBridge apiBridge;

  private final AccountRepository accountRepository;

  /**
   * TODO
   */
  private final BehaviorSubject<Set<Account>> accountsSubject = BehaviorSubject.create();

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
    return Observable.zip(accountRepository.getAll(), apiBridge.getAllAccounts(),
      new Func2<Set<Account>, ApiResult<Set<Account>>, Result>() {
        @Override
        public Result call(Set<Account> localData, ApiResult<Set<Account>> result) {
          if (!localData.isEmpty()) {
            accountsSubject.onNext(localData);
          }
          if (result.isSuccessful() && result.getData() != null) {
            final Set<Account> remoteData = result.getData();
            accountsSubject.onNext(remoteData);
            final Set<Account> addedAccounts = new HashSet<>();
            for (Account account : remoteData) {
              if (!localData.contains(account)) {
                addedAccounts.add(account);
                accountRepository.save(account);
              }
            }
            final Set<Account> deletedAccounts = new HashSet<>();
            for (Account account : localData) {
              if (!remoteData.contains(account)) {
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
  public final Observable<Set<Account>> accounts() {
    return accountsSubject.asObservable();
  }

  /**
   * TODO
   */
  public enum Result {
    FAILED,
    SUCCESS_WITH_NO_CHANGES,
    SUCCESS_WITH_ACCOUNT_ADDITIONS,
    SUCCESS_WITH_ACCOUNT_REMOVALS,
    SUCCESS_WITH_ACCOUNT_ADDITIONS_AND_REMOVALS
  }
}
