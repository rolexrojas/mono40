package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecentTransactionsPresenter {
  private final RecentTransactionsScreen screen;

  private Subscription subscription = Subscriptions.unsubscribed();

  RecentTransactionsPresenter(@NonNull RecentTransactionsScreen screen) {
    this.screen = screen;
  }

  void start() {
    refresh();
  }

  void stop() {
    RxUtils.unsubscribe(subscription);
  }

  void refresh() {
//    if (subscription.isUnsubscribed()) {
//      subscription = initialDataLoader.recentTransactions()
//        .observeOn(AndroidSchedulers.mainThread())
//        .doOnSubscribe(new Action0() {
//          @Override
//          public void call() {
//            UiUtils.showRefreshIndicator(screen);
//          }
//        })
//        .doOnUnsubscribe(new Action0() {
//          @Override
//          public void call() {
//            UiUtils.hideRefreshIndicator(screen);
//          }
//        })
//        .subscribe(new Action1<Result<DomainCode, List<Transaction>>>() {
//          @Override
//          public void call(Result<DomainCode, List<Transaction>> result) {
//            if (DomainUtils.isSuccessful(result)) {
//              screen.clear();
//              final List<Transaction> transactions = result.getData();
//              if (Utils.isNotNull(transactions)) {
//                Date key;
//                List<Transaction> items;
//                final List<Date> keys = new ArrayList<>();
//                final Map<Date, List<Transaction>> groups = new HashMap<>();
//                for (Transaction transaction : transactions) {
//                  key = Utils.getTime(transaction.getDate(), true);
//                  if (keys.contains(key)) {
//                    items = groups.get(key);
//                  } else {
//                    keys.add(key);
//                    items = new ArrayList<>();
//                  }
//                  items.add(transaction);
//                  groups.put(key, items);
//                }
//                for (int i = 0; i < keys.size(); i++) {
//                  key = keys.get(i);
//                  Timber.d("Group '%1$s", key);
//                  screen.add(key);
//                  for (Transaction transaction : groups.get(key)) {
//                    Timber.d(transaction.toString());
//                    screen.add(transaction);
//                  }
//                }
//              } else {
//                 TODO: Let the user know that have never made a transaction.
//              }
//            } else {
//               TODO: Let the user know that loading the latest transactions failed.
//            }
//          }
//        }, new Action1<Throwable>() {
//          @Override
//          public void call(Throwable throwable) {
//            Timber.e(throwable, "Loading the latest transactions");
//             TODO: Let the user know that an error occurred while loading the latest transactions.
//          }
//        });
  }
}
