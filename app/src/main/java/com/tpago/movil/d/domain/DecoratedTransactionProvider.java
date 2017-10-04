package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.api.ApiUtils;

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
      .compose(ApiUtils.<List<Transaction>>handleApiResult(true));
  }
}
