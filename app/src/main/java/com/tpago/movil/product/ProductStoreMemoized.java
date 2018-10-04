package com.tpago.movil.product;

import com.tpago.movil.util.ObjectHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ProductStoreMemoized implements ProductStore {

  static ProductStoreMemoized create(ProductStore store) {
    return new ProductStoreMemoized(store);
  }

  private final ProductStore store;

  private final AtomicReference<List<Product>> accounts = new AtomicReference<>();
  private final AtomicReference<List<Product>> creditCards = new AtomicReference<>();
  private final AtomicReference<List<Product>> loans = new AtomicReference<>();
  private final AtomicReference<List<Product>> all = new AtomicReference<>();

  private ProductStoreMemoized(ProductStore store) {
    this.store = ObjectHelper.checkNotNull(store, "store");
  }

  @Override
  public Single<ProductsDiff> sync(Products products) {
    return this.store.sync(products)
      .doOnSuccess((diff) -> {
        final Products value = diff.value();
        this.accounts.set(value.accounts());
        this.creditCards.set(value.creditCards());
        this.loans.set(value.loans());
        this.all.set(value.allSorted());
      });
  }

  private Maybe<List<Product>> get(List<Product> products, Maybe<List<Product>> fallback) {
    return Maybe.defer(() -> {
      if (ObjectHelper.isNotNull(products)) {
        return Maybe.just(products);
      }
      return fallback;
    });
  }

  @Override
  public Maybe<List<Product>> getAccounts() {
    return this.get(this.accounts.get(), this.store.getAccounts());
  }

  @Override
  public Maybe<List<Product>> getCreditCards() {
    return this.get(this.creditCards.get(), this.store.getCreditCards());
  }

  @Override
  public Maybe<List<Product>> getLoans() {
    return this.get(this.loans.get(), this.store.getLoans());
  }

  @Override
  public Maybe<List<Product>> getAll() {
    return this.get(this.all.get(), this.store.getAll());
  }

  @Override
  public Completable clear() {
    return this.store.clear()
      .doOnComplete(() -> {
        this.accounts.set(null);
        this.creditCards.set(null);
        this.loans.set(null);
        this.all.set(null);
      });
  }
}
