package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountRepo;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link AccountRepo} that uses memory as storage.
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
  public Observable<Pair<Set<Account>, Pair<Boolean, Boolean>>> saveAll(
    @NonNull final Set<Account> accountsToSave) {
    return Observable.just(accountsToSave)
      .map(new Func1<Set<Account>, Pair<Set<Account>, Pair<Boolean, Boolean>>>() {
        @Override
        public Pair<Set<Account>, Pair<Boolean, Boolean>> call(Set<Account> accountsToSave) {
          boolean additions = false;
          for (Account account : accountsToSave) {
            if (!accounts.contains(account)) {
              accounts.add(account);
              if (!additions) {
                additions = true;
              }
            } else {
              accounts.remove(account);
              accounts.add(account);
            }
          }
          boolean removals = false;
          for (Account account : accounts) {
            if (!accountsToSave.contains(account)) {
              accounts.remove(account);
              if (!removals) {
                removals = true;
              }
            }
          }
          return Pair.create(accounts, Pair.create(additions, removals));
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Account>> getAll() {
    return Observable.just(accounts);
  }
}
