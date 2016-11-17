package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.rx.RxUtils;
import com.gbh.movil.domain.api.ApiBridge;

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
public final class AccountManager implements AccountProvider {
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
   * @return TODO
   */
  @NonNull
  private <T extends Account> Observable.Transformer<ApiResult<Set<T>>, T> handleApiResult() {
    return new Observable.Transformer<ApiResult<Set<T>>, T>() {
      @Override
      public Observable<T> call(Observable<ApiResult<Set<T>>> observable) {
        return observable
          .flatMap(new Func1<ApiResult<Set<T>>, Observable<Set<T>>>() {
            @Override
            public Observable<Set<T>> call(ApiResult<Set<T>> result) {
              if (result.isSuccessful()) {
                final Set<T> data = result.getData();
                if (Utils.isNotNull(data)) {
                  return Observable.just(data);
                } else { // This is no supposed to happen.
                  return Observable.error(new NullPointerException("Result's data is missing"));
                }
              } else {
                // TODO: Find or create a suitable exception for this case.
                return Observable.error(new Exception("Failed to fetch registered accounts"));
              }
            }
          })
          .compose(RxUtils.<T>fromCollection());
      }
    };
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
  @NonNull
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
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Set<Account>> getAll() {
    return accountRepo.getAll()
      .concatWith(Observable.merge(
        apiBridge.accounts()
          .compose(this.<ElectronicAccount>handleApiResult()),
        apiBridge.creditCards()
          .compose(this.<CreditCardAccount>handleApiResult()),
        apiBridge.loans()
          .compose(this.<LoanAccount>handleApiResult()))
        .compose(RxUtils.<Account>toSet())
        .flatMap(new Func1<Set<Account>, Observable<Set<Account>>>() {
          @Override
          public Observable<Set<Account>> call(Set<Account> accounts) {
            return syncAccounts(accounts, false);
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
