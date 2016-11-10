package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class AccountManager {
  private final AccountRepo accountRepo;

  public AccountManager(@NonNull AccountRepo accountRepo) {
    this.accountRepo = accountRepo;
  }

  /**
   * TODO
   *
   * @param accounts
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  final Observable<Object> syncAccounts(@NonNull Set<Account> accounts) {
    return accountRepo.getAll()
      .zipWith(Observable.just(accounts), new Func2<Set<Account>, Set<Account>,
        List<Pair<Action, Account>>>() {
        @Override
        public List<Pair<Action, Account>> call(Set<Account> localAccounts,
          Set<Account> remoteAccounts) {
          final List<Pair<Action, Account>> actions = new ArrayList<>();
          for (Account account : remoteAccounts) {
            if (!localAccounts.contains(account)) {
              actions.add(Pair.create(Action.ADD, account));
            } else {
              actions.add(Pair.create(Action.UPDATE, account));
            }
          }
          for (Account account : localAccounts) {
            if (!remoteAccounts.contains(account)) {
              actions.add(Pair.create(Action.REMOVE, account));
            }
          }
          return actions;
        }
      })
      .compose(RxUtils.<Pair<Action, Account>>fromCollection())
      .doOnNext(new Action1<Pair<Action, Account>>() {
        @Override
        public void call(Pair<Action, Account> pair) {
          final Action action = pair.first;
          final Account account = pair.second;
          if (action == Action.ADD) {
            // TODO: Generate an account addition notification.
          } else if (action == Action.REMOVE) {
            // TODO: Generate an account removal notification.
          }
          Timber.d("%1$s %2$s", account, action);
        }
      })
      .flatMap(new Func1<Pair<Action, Account>, Observable<Object>>() {
        @Override
        public Observable<Object> call(Pair<Action, Account> pair) {
          final Observable<?> observable;
          final Action action = pair.first;
          final Account account = pair.second;
          if (action == Action.ADD || action == Action.UPDATE) {
            observable = accountRepo.save(account);
          } else {
            observable = accountRepo.remove(account);
          }
          return observable.cast(Object.class);
        }
      })
      .last();
  }

  /**
   * TODO
   */
  private enum Action {
    ADD,
    UPDATE,
    REMOVE
  }
}
