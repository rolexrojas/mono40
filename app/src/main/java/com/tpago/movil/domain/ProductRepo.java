package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ProductRepo extends ProductProvider {
  /**
   * TODO
   *
   * @param product
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Product> save(@NonNull Product product);

  /**
   * TODO
   *
   * @param product
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Product> remove(@NonNull Product product);

  void clear();
}
