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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
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
        .map(new Func1<Result<DomainCode,List<Transaction>>, List<Transaction>>() {
          @Override
          public List<Transaction> call(Result<DomainCode, List<Transaction>> result) {
            final List<Transaction> transactions = new ArrayList<>();
            if (DomainUtils.isSuccessful(result)) {
              screen.clear();
              final List<Transaction> data = result.getData();
              if (Utils.isNotNull(data)) {
                transactions.addAll(data);
              } else {
                // TODO: Let the user know that have never made a transaction.
              }
            } else {
              // TODO: Let the user know that loading the latest transactions failed.
            }
            return transactions;
          }
        })
        .compose(RxUtils.<Transaction>fromList())
        .groupBy(new Func1<Transaction, Date>() {
          @Override
          public Date call(Transaction transaction) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(transaction.getDate());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
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
        .doOnNext(new Action1<GroupedObservable<Date, Transaction>>() {
          @Override
          public void call(GroupedObservable<Date, Transaction> group) {
            final Date key = group.getKey();
            Timber.d("Group '%1$s", key);
            screen.add(key);
          }
        })
        .flatMap(new Func1<GroupedObservable<Date, Transaction>, Observable<Transaction>>() {
          @Override
          public Observable<Transaction> call(GroupedObservable<Date, Transaction> group) {
            return group.asObservable();
          }
        })
        .subscribe(new Action1<Transaction>() {
          @Override
          public void call(Transaction transaction) {
            Timber.d(transaction.toString());
            screen.add(transaction);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Loading the latest transactions");
          }
        });
    }
  }
}
