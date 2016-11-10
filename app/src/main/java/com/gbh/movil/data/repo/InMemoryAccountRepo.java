package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountRepo;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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
      .flatMap(new Func1<Account, Observable<Account>>() {
        @Override
        public Observable<Account> call(Account account) {
          return remove(account);
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
  public Observable<Account> remove(@NonNull Account account) {
    return Observable.just(account)
      .doOnNext(new Action1<Account>() {
        @Override
        public void call(Account account) {
          if (accounts.contains(account)) {
            accounts.remove(account);
          }
        }
      });
  }
}
