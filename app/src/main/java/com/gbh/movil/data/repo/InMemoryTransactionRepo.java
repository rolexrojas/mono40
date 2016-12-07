package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.TransactionRepo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * {@link TransactionRepo Transaction repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class InMemoryTransactionRepo implements TransactionRepo {
  private final List<Transaction> transactions = new ArrayList<>();

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> saveAll(@NonNull List<Transaction> transactionsToSave) {
    return Observable.just(transactionsToSave)
      .compose(RxUtils.<Transaction>fromCollection())
      .toSortedList(new Func2<Transaction, Transaction, Integer>() {
        @Override
        public Integer call(Transaction a, Transaction b) {
          return a.getDate() > b.getDate() ? -1 : (a.getDate() == b.getDate() ? 0 : 1);
        }
      })
      .map(new Func1<List<Transaction>, List<Transaction>>() {
        @Override
        public List<Transaction> call(List<Transaction> transactionsToSave) {
          transactions.clear();
          transactions.addAll(transactionsToSave);
          return transactions;
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> getAll() {
    return Observable.just(transactions);
  }
}
