package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.gbh.movil.Utils;
import com.gbh.movil.RxUtils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.Event;
import com.gbh.movil.domain.EventBus;
import com.gbh.movil.domain.EventType;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;

import java.util.Set;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountsPresenter extends Presenter<AccountsScreen> {
  private final SchedulerProvider schedulerProvider;
  private final EventBus eventBus;
  private final AccountManager accountManager;
  private final BalanceManager balanceManager;

  private CompositeSubscription compositeSubscription;

  AccountsPresenter(@NonNull SchedulerProvider schedulerProvider, @NonNull EventBus eventBus,
    @NonNull AccountManager accountManager, @NonNull BalanceManager balanceManager) {
    this.schedulerProvider = schedulerProvider;
    this.eventBus = eventBus;
    this.accountManager = accountManager;
    this.balanceManager = balanceManager;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    compositeSubscription = new CompositeSubscription();
    Subscription subscription = accountManager.getAll()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          UiUtils.showRefreshIndicator(screen);
        }
      })
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
          UiUtils.hideRefreshIndicator(screen);
        }
      })
      .subscribe(new Action1<Set<Account>>() {
        @Override
        public void call(Set<Account> accounts) {
          if (Utils.isNotNull(accounts)) {
            screen.clear();
            for (Account account : accounts) {
              screen.add(account);
              if (balanceManager.hasValidBalance(account)) {
                screen.setBalance(account, balanceManager.getBalance(account));
              }
            }
            screen.showLastTransactionsButton();
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Loading all registered accounts");
        }
      });
    compositeSubscription.add(subscription);
    subscription = eventBus.onEventDispatched(
      EventType.ACCOUNT_BALANCE_EXPIRATION)
      .observeOn(schedulerProvider.ui())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(Event event) {
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to balance expiration events");
        }
      });
    compositeSubscription.add(subscription);
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    if (Utils.isNotNull(compositeSubscription)) {
      RxUtils.unsubscribe(compositeSubscription);
      compositeSubscription = null;
    }
  }

  /**
   * TODO
   *
   * @param account
   *   TODO
   * @param pin
   *   TODO
   */
  void queryBalance(@NonNull final Account account, @NonNull final String pin) {
    assertScreen();
    if (balanceManager.hasValidBalance(account)) {
      screen.setBalance(account, balanceManager.getBalance(account));
    } else if (Utils.isNotNull(compositeSubscription)) {
      final Subscription subscription = balanceManager.queryBalance(account, pin)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe(new Action1<Pair<Boolean, Balance>>() {
          @Override
          public void call(Pair<Boolean, Balance> result) {
            screen.onBalanceQueried(result.first, account, result.second);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Querying the balance of an account (%1$s)", account);
          }
        });
      compositeSubscription.add(subscription);
    }
  }
}
