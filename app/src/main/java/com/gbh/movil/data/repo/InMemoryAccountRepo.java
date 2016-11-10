package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountRepo;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * {@link AccountRepo Account repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class InMemoryAccountRepo implements AccountRepo {
  private final Set<Account> accounts = new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Account>> getAll() {
    return Observable.just(accounts);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Account> save(@NonNull Account account) {
    return Observable.just(account)
      .zipWith(remove(account), new Func2<Account, Boolean, Account>() {
        @Override
        public Account call(Account account, Boolean wasRemoved) {
          return account;
        }
      })
      .doOnNext(new Action1<Account>() {
        @Override
        public void call(Account account) {
          accounts.add(account);
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Boolean> remove(@NonNull Account account) {
    if (accounts.contains(account)) {
      return Observable.just(accounts.remove(account));
    } else {
      return Observable.just(false);
    }
  }
}
