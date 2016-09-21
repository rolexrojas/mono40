package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.DataLoader;

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

  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  AccountsPresenter(@NonNull AccountsScreen screen, @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.dataLoader = dataLoader;
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
          }
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
