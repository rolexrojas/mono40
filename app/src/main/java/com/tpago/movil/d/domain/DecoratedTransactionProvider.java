package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.api.ApiUtils;
import com.tpago.movil.d.domain.session.SessionManager;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public class DecoratedTransactionProvider implements TransactionProvider {
  private final TransactionRepo transactionRepo;
  private final DepApiBridge apiBridge;
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public DecoratedTransactionProvider(@NonNull TransactionRepo transactionRepo,
    @NonNull DepApiBridge apiBridge, @NonNull SessionManager sessionManager) {
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
    return apiBridge.recentTransactions(
      sessionManager.getSession()
        .getAuthToken()
    )
      .compose(ApiUtils.<List<Transaction>>handleApiResult(true));
  }
}
