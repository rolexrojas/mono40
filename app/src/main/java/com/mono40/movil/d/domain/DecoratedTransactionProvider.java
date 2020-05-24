package com.mono40.movil.d.domain;

import androidx.annotation.NonNull;

import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.api.ApiUtils;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public class DecoratedTransactionProvider implements TransactionProvider {

  private final DepApiBridge apiBridge;

  public DecoratedTransactionProvider(@NonNull DepApiBridge apiBridge) {
    this.apiBridge = apiBridge;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Transaction>> getAll() {
    return apiBridge.recentTransactions()
      .compose(ApiUtils.handleApiResult(true));
  }
}
