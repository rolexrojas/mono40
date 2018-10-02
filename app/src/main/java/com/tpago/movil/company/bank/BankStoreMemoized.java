package com.tpago.movil.company.bank;

import com.tpago.movil.company.CompanyStoreMemoized;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
final class BankStoreMemoized implements BankStore {

  static BankStoreMemoized create() {
    return new BankStoreMemoized();
  }

  private final CompanyStoreMemoized<Bank> store;

  private BankStoreMemoized() {
    this.store = CompanyStoreMemoized.create();
  }

  @Override
  public Completable sync(List<Bank> banks) {
    return this.store.sync(banks);
  }

  @Override
  public Maybe<List<Bank>> getAll() {
    return this.store.getAll();
  }

  @Override
  public Maybe<Bank> findByCode(int code) {
    return this.store.findByCode(code);
  }

  @Override
  public Completable clear() {
    return this.store.clear();
  }
}
