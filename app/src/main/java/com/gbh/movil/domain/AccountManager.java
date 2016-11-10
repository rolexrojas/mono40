package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;

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
  private final EventBus eventBus;
  private final AccountRepo accountRepo;
  private final ApiBridge apiBridge;

  public AccountManager(@NonNull EventBus eventBus, @NonNull AccountRepo accountRepo,
    @NonNull ApiBridge apiBridge) {
    this.eventBus = eventBus;
    this.accountRepo = accountRepo;
    this.apiBridge = apiBridge;
  }

  /**
   * TODO
   *
   * @param accounts
   *   TODO
   * @param mustEmitAdditionAndRemovalNotifications
   *   TODO
   *
   * @return TODO
   */
  private Observable<Set<Account>> syncAccounts(@NonNull Set<Account> accounts,
    final boolean mustEmitAdditionAndRemovalNotifications) {
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
          if (mustEmitAdditionAndRemovalNotifications) {
            final Action action = pair.first;
            final Account account = pair.second;
            if (action == Action.ADD) {
              eventBus.dispatch(new AccountAdditionEvent());
            } else if (action == Action.REMOVE) {
              eventBus.dispatch(new AccountRemovalEvent());
            }
            Timber.d("%1$s %2$s", account, action);
          }
        }
      })
      .flatMap(new Func1<Pair<Action, Account>, Observable<Account>>() {
        @Override
        public Observable<Account> call(Pair<Action, Account> pair) {
          final Action action = pair.first;
          final Account account = pair.second;
          final Observable<Account> observable;
          if (action == Action.ADD || action == Action.UPDATE) {
            observable = accountRepo.save(account);
          } else {
            observable = accountRepo.remove(account);
          }
          return observable;
        }
      })
      .compose(RxUtils.<Account>toSet());
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
  final Observable<Set<Account>> syncAccounts(@NonNull Set<Account> accounts) {
    return syncAccounts(accounts, true);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Set<Account>> getAll() {
    return accountRepo.getAll()
      .concatWith(apiBridge.accounts()
        .flatMap(new Func1<Result<ApiCode, Set<Account>>, Observable<Set<Account>>>() {
          @Override
          public Observable<Set<Account>> call(Result<ApiCode, Set<Account>> result) {
            if (ApiUtils.isSuccessful(result)) {
              final Set<Account> accounts = result.getData();
              if (Utils.isNotNull(accounts)) {
                return syncAccounts(accounts, false);
              } else { // This is not supposed to happen.
                return Observable.just(null);
              }
            } else {
              Timber.d("Failed to load all registered accounts (%1$s)", result);
              return Observable.just(null);
            }
          }
        }));
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
