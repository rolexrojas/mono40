package com.mono40.movil.product;

import com.mono40.movil.store.DiskStore;
import com.mono40.movil.util.ObjectHelper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ProductStoreDisk implements ProductStore {

  private static final String KEY = ProductStore.class.getCanonicalName();

  static ProductStoreDisk create(DiskStore store) {
    return new ProductStoreDisk(store);
  }

  private final DiskStore store;

  private ProductStoreDisk(DiskStore store) {
    this.store = ObjectHelper.checkNotNull(store, "store");
  }

  @Override
  public Single<ProductsDiff> sync(Products products) {
    ObjectHelper.checkNotNull(products, "products");
    return this.store.get(KEY, Products.class)
      .switchIfEmpty(Single.just(Products.create()))
      .zipWith(Single.just(products), ProductsDiff::create)
      .flatMap((diff) ->
        this.store.set(KEY, diff.value())
          .andThen(Single.just(diff))
      );
  }

  private Maybe<Products> getProducts() {
    return this.store.get(KEY, Products.class);
  }

  @Override
  public Maybe<List<Product>> getAccounts() {
    return this.getProducts()
      .map(Products::accountsSorted);
  }

  @Override
  public Maybe<List<Product>> getCreditCards() {
    return this.getProducts()
      .map(Products::creditCardsSorted);
  }

  @Override
  public Maybe<List<Product>> getLoans() {
    return this.getProducts()
      .map(Products::loansSorted);
  }

  @Override
  public Maybe<List<Product>> getAll() {
    return this.getProducts()
      .map(Products::allSorted);
  }

  @Override
  public Completable clear() {
    return this.store.remove(KEY);
  }
}
