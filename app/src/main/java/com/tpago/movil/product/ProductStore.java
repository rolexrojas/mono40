package com.tpago.movil.product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface ProductStore {

  Single<ProductsDiff> sync(Products products);

  Maybe<List<Product>> getAccounts();

  Maybe<List<Product>> getCreditCards();

  Maybe<List<Product>> getLoans();

  Maybe<List<Product>> getAll();

  Completable clear();
}
