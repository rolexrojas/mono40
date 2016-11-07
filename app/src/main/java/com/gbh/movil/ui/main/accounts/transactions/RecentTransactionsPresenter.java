package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.DomainCode;
import com.gbh.movil.domain.DomainUtils;
import com.gbh.movil.domain.Result;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.RefreshIndicator;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecentTransactionsPresenter {
  private final RecentTransactionsScreen screen;
  private final DataLoader dataLoader;

  private Subscription subscription = Subscriptions.unsubscribed();

  RecentTransactionsPresenter(@NonNull RecentTransactionsScreen screen,
    @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.dataLoader = dataLoader;
  }

  void start() {
    refresh();
  }

  void stop() {
    RxUtils.unsubscribe(subscription);
  }

  void refresh() {
    if (subscription.isUnsubscribed()) {
      subscription = dataLoader.recentTransactions()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            final RefreshIndicator refreshIndicator = screen.getRefreshIndicator();
            if (Utils.isNotNull(refreshIndicator)) {
              refreshIndicator.show();
            }
          }
        })
        .doOnUnsubscribe(new Action0() {
          @Override
          public void call() {
            final RefreshIndicator refreshIndicator = screen.getRefreshIndicator();
            if (Utils.isNotNull(refreshIndicator)) {
              refreshIndicator.hide();
            }
          }
        })
        .subscribe(new Action1<Result<DomainCode, List<Transaction>>>() {
          @Override
          public void call(Result<DomainCode, List<Transaction>> result) {
            if (DomainUtils.isSuccessful(result)) {
              screen.clear();
              final List<Transaction> transactions = result.getData();
              if (Utils.isNotNull(transactions)) {
                Date key;
                List<Transaction> items;
                final List<Date> keys = new ArrayList<>();
                final Map<Date, List<Transaction>> groups = new HashMap<>();
                for (Transaction transaction : transactions) {
                  key = Utils.getTime(transaction.getDate(), true);
                  if (keys.contains(key)) {
                    items = groups.get(key);
                  } else {
                    keys.add(key);
                    items = new ArrayList<>();
                  }
                  items.add(transaction);
                  groups.put(key, items);
                }
                for (int i = 0; i < keys.size(); i++) {
                  key = keys.get(i);
                  Timber.d("Group '%1$s", key);
                  screen.add(key);
                  for (Transaction transaction : groups.get(key)) {
                    Timber.d(transaction.toString());
                    screen.add(transaction);
                  }
                }
              } else {
                // TODO: Let the user know that have never made a transaction.
              }
            } else {
              // TODO: Let the user know that loading the latest transactions failed.
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Loading the latest transactions");
            // TODO: Let the user know that an error occurred while loading the latest transactions.
          }
        });
    }
  }
}
