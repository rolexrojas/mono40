package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.api.ApiResult;

import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountsPresenter {
  /**
   * TODO
   */
  private final AccountsScreen screen;

  /**
   * TODO
   */
  private final DataLoader dataLoader;

  /**
   * TODO
   */
  private final BalanceManager balanceManager;

  /**
   * TODO
   */
  private Subscription loadAccountsSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private Subscription queryBalanceSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   */
  private Subscription balanceExpirationSubscription = Subscriptions.unsubscribed();

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
    loadAccountsSubscription = dataLoader.accounts()
      .observeOn(AndroidSchedulers.mainThread())
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
          Timber.e(throwable, "Listener to account change events");
        }
      });
    balanceExpirationSubscription = balanceManager.expiration()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Account>() {
        @Override
        public void call(Account account) {
          screen.setBalance(account, null);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to balance expiration events");
        }
      });
  }

  /**
   * TODO
   */
  void stop() {
    if (!queryBalanceSubscription.isUnsubscribed()) {
      queryBalanceSubscription.unsubscribe();
    }
    if (!balanceExpirationSubscription.isUnsubscribed()) {
      balanceExpirationSubscription.unsubscribe();
    }
    if (!loadAccountsSubscription.isUnsubscribed()) {
      loadAccountsSubscription.unsubscribe();
    }
  }

  /**
   * TODO
   *
   * @param account
   *   TODO
   */
  void queryBalance(@NonNull final Account account, @NonNull final String pin) {
    if (balanceManager.hasValidBalance(account)) {
      screen.setBalance(account, balanceManager.getBalance(account));
    } else {
      if (!queryBalanceSubscription.isUnsubscribed()) {
        queryBalanceSubscription.unsubscribe();
      }
      queryBalanceSubscription = balanceManager.queryBalance(account, pin)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ApiResult<Balance>>() {
          @Override
          public void call(ApiResult<Balance> result) {
            screen.onBalanceQueried(result.isSuccessful(), account, result.getData());
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Querying the balance of an account");
          }
        });
    }
  }
}
