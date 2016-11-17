package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiResult;

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
        .flatMap(new Func1<ApiResult<List<Transaction>>, Observable<List<Transaction>>>() {
          @Override
          public Observable<List<Transaction>> call(ApiResult<List<Transaction>> result) {
            if (result.isSuccessful()) {
              final List<Transaction> transactions = result.getData();
              if (Utils.isNotNull(transactions)) {
                return transactionRepo.saveAll(transactions);
              } else { // This is not supposed to happen.
                return Observable.error(new NullPointerException("Result's data is missing"));
              }
            } else {
              // TODO: Find or create a suitable exception for this case.
              return Observable.error(new Exception("Failed to load latest transactions"));
            }
          }
        }));
  }
}
