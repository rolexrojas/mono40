package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public class DecoratedApiBridge implements ApiBridge {
  private final ApiBridge apiBridge;

  public DecoratedApiBridge(@NonNull ApiBridge apiBridge) {
    this.apiBridge = apiBridge;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private <T> Observable.Transformer<ApiResult<T>, ApiResult<T>> assertAuthorization() {
    return new Observable.Transformer<ApiResult<T>, ApiResult<T>>() {
      @Override
      public Observable<ApiResult<T>> call(Observable<ApiResult<T>> observable) {
        // TODO
        return observable;
      }
    };
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Bank>>> banks() {
    return apiBridge.banks()
      .compose(this.<Set<Bank>>assertAuthorization());
  }

  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad() {
    return apiBridge.initialLoad()
      .compose(this.<InitialData>assertAuthorization());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Account>>> accounts() {
    return apiBridge.accounts()
      .compose(this.<Set<Account>>assertAuthorization());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull Account account,
    @NonNull String pin) {
    return apiBridge.queryBalance(account, pin)
      .compose(this.<Balance>assertAuthorization());
  }

  @NonNull
  @Override
  public Observable<ApiResult<Set<Recipient>>> recipients() {
    return apiBridge.recipients()
      .compose(this.<Set<Recipient>>assertAuthorization());
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions() {
    return apiBridge.recentTransactions()
      .compose(this.<List<Transaction>>assertAuthorization());
  }
}
