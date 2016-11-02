package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.data.RxUtils;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.Transaction;

import java.util.Date;

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
    subscription = dataLoader.recentTransactions()
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
          screen.clear();
        }
      })
      .flatMap(RxUtils.<Transaction>fromList())
      .groupBy(new Func1<Transaction, Date>() {
        @Override
        public Date call(Transaction transaction) {
          return new Date(transaction.getDate());
        }
      })
      .doOnNext(new Action1<GroupedObservable<Date, Transaction>>() {
        @Override
        public void call(GroupedObservable<Date, Transaction> group) {
          final Date key = group.getKey();
          Timber.d("Group '%1$s'", key);
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
          Timber.e(throwable, "Loading recent transactions");
        }
      });
  }

  void stop() {
    RxUtils.unsubscribe(subscription);
  }
}
