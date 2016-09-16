package com.tpago.movil.data.repo;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.AccountRepository;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;

/**
 * @author hecvasro
 */
public class InMemoryAccountRepository implements AccountRepository {
  private final Set<Account> accounts = new HashSet<>();

  @NonNull
  @Override
  public Observable<Set<Account>> getAll() {
    return Observable.just(accounts);
  }

  @Override
  public void save(@NonNull Account account) {
    if (!accounts.contains(account)) {
      accounts.add(account);
    }
  }

  @Override
  public void delete(@NonNull Account account) {
    if (accounts.contains(account)) {
      accounts.remove(account);
    }
  }
}
