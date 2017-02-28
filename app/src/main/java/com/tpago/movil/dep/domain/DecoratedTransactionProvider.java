package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.api.ApiBridge;
import com.tpago.movil.dep.domain.api.ApiUtils;
import com.tpago.movil.dep.domain.session.SessionManager;

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
  private final com.tpago.movil.dep.domain.session.SessionManager sessionManager;

  public DecoratedTransactionProvider(@NonNull TransactionRepo transactionRepo,
    @NonNull ApiBridge apiBridge, @NonNull SessionManager sessionManager) {
    this.transactionRepo = transactionRepo;
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> getAll() {
    return transactionRepo.getAll()
      .concatWith(apiBridge.recentTransactions(sessionManager.getSession().getAuthToken())
        .compose(ApiUtils.<List<Transaction>>handleApiResult(true))
        .flatMap(new Func1<List<Transaction>, Observable<List<Transaction>>>() {
          @Override
          public Observable<List<Transaction>> call(List<Transaction> transactions) {
            return transactionRepo.saveAll(transactions);
          }
        }));
  }
}
