package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.Account;

import rx.subscriptions.CompositeSubscription;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountsPresenter {
  private final AccountsScreen screen;

  private CompositeSubscription compositeSubscription;

  AccountsPresenter(@NonNull AccountsScreen screen) {
    this.screen = screen;
  }

  void start() {
//    compositeSubscription = new CompositeSubscription();
//    Subscription subscription;
//    subscription = initialDataLoader.accounts()
//      .observeOn(AndroidSchedulers.mainThread())
//      .doOnSubscribe(new Action0() {
//        @Override
//        public void call() {
//          UiUtils.showRefreshIndicator(screen);
//        }
//      })
//      .doOnUnsubscribe(new Action0() {
//        @Override
//        public void call() {
//          UiUtils.hideRefreshIndicator(screen);
//        }
//      })
//      .subscribe(new Action1<Result<DomainCode, Set<Account>>>() {
//        @Override
//        public void call(Result<DomainCode, Set<Account>> result) {
//          screen.clear();
//          if (DomainUtils.isSuccessful(result)) {
//            final Set<Account> accounts = result.getData();
//            if (Utils.isNotNull(accounts)) {
//              for (Account account : result.getData()) {
//                screen.add(account);
//                if (balanceManager.hasValidBalance(account)) {
//                  screen.setBalance(account, balanceManager.getBalance(account));
//                }
//              }
//            }
//            screen.showLastTransactionsButton();
//          } else {
//             TODO: Let the user know that the loading all his account failed.
//          }
//        }
//      }, new Action1<Throwable>() {
//        @Override
//        public void call(Throwable throwable) {
//          Timber.e(throwable, "Listener to account change events");
//        }
//      });
//    compositeSubscription.add(subscription);
//    subscription = balanceManager.expiration()
//      .observeOn(AndroidSchedulers.mainThread())
//      .subscribe(new Action1<Account>() {
//        @Override
//        public void call(Account account) {
//          screen.setBalance(account, null);
//        }
//      }, new Action1<Throwable>() {
//        @Override
//        public void call(Throwable throwable) {
//          Timber.e(throwable, "Listening to balance expiration events");
//        }
//      });
//    compositeSubscription.add(subscription);
  }

  void stop() {
    if (Utils.isNotNull(compositeSubscription)) {
      RxUtils.unsubscribe(compositeSubscription);
      compositeSubscription = null;
    }
  }

  void queryBalance(@NonNull final Account account, @NonNull final String pin) {
//    if (balanceManager.hasValidBalance(account)) {
//      screen.setBalance(account, balanceManager.getBalance(account));
//    } else if (Utils.isNotNull(compositeSubscription)) {
//      final Subscription subscription = balanceManager.queryBalance(account, pin)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Action1<Result<DomainCode, Balance>>() {
//          @Override
//          public void call(Result<DomainCode, Balance> result) {
//            screen.onBalanceQueried(DomainUtils.isSuccessful(result), account, result.getData());
//          }
//        }, new Action1<Throwable>() {
//          @Override
//          public void call(Throwable throwable) {
//            Timber.e(throwable, "Querying the balance of an account");
//          }
//        });
//      compositeSubscription.add(subscription);
//    }
  }
}
