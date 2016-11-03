package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.RxUtils;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.TransactionRepo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
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
      .compose(RxUtils.<Transaction>fromList())
      .toSortedList()
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
