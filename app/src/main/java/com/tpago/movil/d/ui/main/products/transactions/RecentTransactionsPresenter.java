package com.tpago.movil.d.ui.main.products.transactions;

import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.TransactionProvider;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.misc.UiUtils;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class RecentTransactionsPresenter extends Presenter<RecentTransactionsScreen> {

  private final SchedulerProvider schedulerProvider;
  private final TransactionProvider transactionProvider;

  private Subscription subscription = Subscriptions.unsubscribed();

  RecentTransactionsPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull TransactionProvider transactionProvider
  ) {
    this.schedulerProvider = schedulerProvider;
    this.transactionProvider = transactionProvider;
  }

  final void start() {
    assertScreen();
    refresh();
  }

  final void stop() {
    assertScreen();
    RxUtils.unsubscribe(subscription);
  }

  final void refresh() {
    assertScreen();
    if (subscription.isUnsubscribed()) {
      subscription = transactionProvider.getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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
            if (ObjectHelper.isNotNull(transactions)) {
              screen.clear();
              Date key;
              List<Transaction> items;
              final List<Date> keys = new ArrayList<>();
              final Map<Date, List<Transaction>> groups = new HashMap<>();
              for (Transaction transaction : transactions) {
                key = transaction.date();
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
