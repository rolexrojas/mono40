package com.gbh.movil.data.repo;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductRepo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * {@link ProductRepo Product repository} implementation that uses memory as storage.
 *
 * @author hecvasro
 */
class InMemoryProductRepo implements ProductRepo {
  private final List<Product> products = new ArrayList<>();

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<List<Product>> getAll() {
    return Observable.just(products);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Product> save(@NonNull Product product) {
    return Observable.just(product)
      .flatMap(new Func1<Product, Observable<Product>>() {
        @Override
        public Observable<Product> call(Product product) {
          return remove(product);
        }
      })
      .doOnNext(new Action1<Product>() {
        @Override
        public void call(Product product) {
          products.add(product);
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<Product> remove(@NonNull Product product) {
    return Observable.just(product)
      .doOnNext(new Action1<Product>() {
        @Override
        public void call(Product product) {
          if (products.contains(product)) {
            products.remove(product);
          }
        }
      });
  }
}
