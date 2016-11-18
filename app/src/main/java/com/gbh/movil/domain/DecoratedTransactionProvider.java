package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public class DecoratedTransactionProvider implements TransactionProvider {
  private final TransactionRepo transactionRepo;
  private final ApiBridge apiBridge;

  public DecoratedTransactionProvider(@NonNull TransactionRepo transactionRepo,
    @NonNull ApiBridge apiBridge) {
    this.transactionRepo = transactionRepo;
    this.apiBridge = apiBridge;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> getAll() {
    return transactionRepo.getAll()
      .concatWith(apiBridge.recentTransactions()
        .compose(ApiUtils.<List<Transaction>>handleApiResult(true))
        .flatMap(new Func1<List<Transaction>, Observable<List<Transaction>>>() {
          @Override
          public Observable<List<Transaction>> call(List<Transaction> transactions) {
            return transactionRepo.saveAll(transactions);
          }
        }));
  }
}
