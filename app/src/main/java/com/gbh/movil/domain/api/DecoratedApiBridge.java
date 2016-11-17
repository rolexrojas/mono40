package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.product.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.product.CreditCard;
import com.gbh.movil.domain.product.Account;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.product.Loan;
import com.gbh.movil.domain.product.Product;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.product.transaction.Transaction;

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
        // TODO: Assert user's authorization.
        return observable;
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Bank>>> banks() {
    return apiBridge.banks()
      .compose(this.<Set<Bank>>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad() {
    return apiBridge.initialLoad()
      .compose(this.<InitialData>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Account>>> accounts() {
    return apiBridge.accounts()
      .compose(this.<Set<Account>>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<CreditCard>>> creditCards() {
    return apiBridge.creditCards()
      .compose(this.<Set<CreditCard>>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Loan>>> loans() {
    return apiBridge.loans()
      .compose(this.<Set<Loan>>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull Product product,
    @NonNull String pin) {
    return apiBridge.queryBalance(product, pin)
      .compose(this.<Balance>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions() {
    return apiBridge.recentTransactions()
      .compose(this.<List<Transaction>>assertAuthorization());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Recipient>>> recipients() {
    return apiBridge.recipients()
      .compose(this.<Set<Recipient>>assertAuthorization());
  }
}
