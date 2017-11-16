package com.tpago.movil.bank;

import com.tpago.movil.api.Api;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ApiBankListSupplier implements BankListSupplier {

  static ApiBankListSupplier create(Api api) {
    return new ApiBankListSupplier(api);
  }

  private final Api api;

  private ApiBankListSupplier(Api api) {
    this.api = ObjectHelper.checkNotNull(api, "api");
  }

  @Override
  public Single<List<Bank>> get() {
    return this.api.getBanks()
      .flatMapObservable(Observable::fromIterable)
      .toSortedList(Bank::compareTo);
  }
}
