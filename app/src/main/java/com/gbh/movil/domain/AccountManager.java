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

  private static final int ACTION_ADD = 0;
  private static final int ACTION_UPDATE = 1;
  private static final int ACTION_REMOVE = 2;

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
        List<Pair<Integer, Account>>>() {
        @Override
        public List<Pair<Integer, Account>> call(Set<Account> localAccounts,
          Set<Account> remoteAccounts) {
          final List<Pair<Integer, Account>> actions = new ArrayList<>();
          for (Account account : remoteAccounts) {
            if (!localAccounts.contains(account)) {
              actions.add(Pair.create(ACTION_ADD, account));
            } else {
              actions.add(Pair.create(ACTION_UPDATE, account));
            }
          }
          for (Account account : localAccounts) {
            if (!remoteAccounts.contains(account)) {
              actions.add(Pair.create(ACTION_REMOVE, account));
            }
          }
          return actions;
        }
      })
      .compose(RxUtils.<Pair<Integer, Account>>fromCollection())
      .doOnNext(new Action1<Pair<Integer, Account>>() {
        @Override
        public void call(Pair<Integer, Account> pair) {
          final int action = pair.first;
          final Account account = pair.second;
          final String actionName;
          if (action == ACTION_ADD) {
            actionName = "added";
            // TODO: Generate an account addition notification.
          } else if (action == ACTION_UPDATE) {
            actionName = "updated";
          } else {
            actionName = "removed";
            // TODO: Generate an account removal notification.
          }
          Timber.d("%1$s %2$s", account, actionName);
        }
      })
      .flatMap(new Func1<Pair<Integer, Account>, Observable<Object>>() {
        @Override
        public Observable<Object> call(Pair<Integer, Account> pair) {
          final int action = pair.first;
          final Account account = pair.second;
          final Observable<?> observable;
          if (action == ACTION_ADD || action == ACTION_UPDATE) {
            observable = accountRepo.save(account);
          } else {
            observable = accountRepo.remove(account);
          }
          return observable.cast(Object.class);
        }
      })
      .last();
  }
}
