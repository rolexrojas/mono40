package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.DomainCode;
import com.gbh.movil.domain.DomainUtils;
import com.gbh.movil.domain.Result;
import com.gbh.movil.domain.Transaction;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecentTransactionsPresenter {
  private final RecentTransactionsScreen screen;
  private final DataLoader dataLoader;

  private CompositeSubscription compositeSubscription;

  RecentTransactionsPresenter(@NonNull RecentTransactionsScreen screen,
    @NonNull DataLoader dataLoader) {
    this.screen = screen;
    this.dataLoader = dataLoader;
  }

  void start() {
    compositeSubscription = new CompositeSubscription();
    final Subscription subscription = dataLoader.recentTransactions()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Result<DomainCode, List<Transaction>>>() {
        @Override
        public void call(Result<DomainCode, List<Transaction>> result) {
          if (DomainUtils.isSuccessful(result)) {
            screen.clear();
            final List<Transaction> transactions = result.getData();
            if (Utils.isNotNull(transactions)) {
              final Subscription subscription = Observable.just(transactions)
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
                .doOnNext(new Action1<GroupedObservable<Date, Transaction>>() {
                  @Override
                  public void call(GroupedObservable<Date, Transaction> group) {
                    final Date key = group.getKey();
                    Timber.d("Group '%1$s", key);
                    screen.add(key);
                  }
                })
                .flatMap(new Func1<GroupedObservable<Date, Transaction>,
                  Observable<Transaction>>() {
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
                });
              compositeSubscription.add(subscription);
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
        }
      });
    compositeSubscription.add(subscription);
  }

  void stop() {
    if (Utils.isNotNull(compositeSubscription)) {
      RxUtils.unsubscribe(compositeSubscription);
      compositeSubscription = null;
    }
  }
}
