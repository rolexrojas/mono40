package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class TransactionManager {
  private final TransactionRepo transactionRepo;
  private final ApiBridge apiBridge;

  public TransactionManager(@NonNull TransactionRepo transactionRepo,
    @NonNull ApiBridge apiBridge) {
    this.transactionRepo = transactionRepo;
    this.apiBridge = apiBridge;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final Observable<List<Transaction>> getAll() {
    return transactionRepo.getAll()
      .concatWith(apiBridge.recentTransactions()
        .flatMap(new Func1<Result<ApiCode, List<Transaction>>, Observable<List<Transaction>>>() {
          @Override
          public Observable<List<Transaction>> call(Result<ApiCode, List<Transaction>> result) {
            if (ApiUtils.isSuccessful(result)) {
              final List<Transaction> transactions = result.getData();
              if (Utils.isNotNull(transactions)) {
                return transactionRepo.saveAll(transactions);
              } else { // This is not supposed to happen.
                return Observable.just(null);
              }
            } else {
              Timber.d("Failed to load latest transactions (%1$s)", result);
              return Observable.just(null);
            }
          }
        }));
  }
}
