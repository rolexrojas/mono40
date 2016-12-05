package com.gbh.movil.ui.main.products.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.TransactionProvider;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.UiUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecentTransactionsPresenter extends Presenter<RecentTransactionsScreen> {
  private final SchedulerProvider schedulerProvider;
  private final TransactionProvider transactionProvider;

  private Subscription subscription = Subscriptions.unsubscribed();

  RecentTransactionsPresenter(@NonNull SchedulerProvider schedulerProvider,
    @NonNull TransactionProvider transactionProvider) {
    this.schedulerProvider = schedulerProvider;
    this.transactionProvider = transactionProvider;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    refresh();
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }

  /**
   * TODO
   */
  void refresh() {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = transactionProvider.getAll()
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
        .subscribe(new Action1<List<Transaction>>() {
          @Override
          public void call(List<Transaction> transactions) {
            if (Utils.isNotNull(transactions)) {
              screen.clear();
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
