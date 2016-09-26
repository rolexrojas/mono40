package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;

import java.util.Set;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountsPresenter {
  private static final String TAG = AccountsPresenter.class.getSimpleName();

  private final AccountsScreen screen;

  private final DataLoader dataLoader;

  private final BalanceManager balanceManager;

  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  AccountsPresenter(@NonNull AccountsScreen screen, @NonNull DataLoader dataLoader,
    @NonNull BalanceManager balanceManager) {
    this.screen = screen;
    this.dataLoader = dataLoader;
    this.balanceManager = balanceManager;
  }

  /**
   * TODO
   */
  void start() {
    subscription = dataLoader.accounts()
      .subscribe(new Action1<Set<Account>>() {
        @Override
        public void call(Set<Account> accounts) {
          screen.clear();
          for (Account account : accounts) {
            screen.add(account);
            if (balanceManager.hasValidBalance(account)) {
              screen.setBalance(account, balanceManager.getBalance(account));
            }
          }
          screen.showLastTransactionsButton();
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Log.e(TAG, "Loading all the accounts", throwable);
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
