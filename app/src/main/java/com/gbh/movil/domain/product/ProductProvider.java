package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ProductProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Set<Product>> getAll();
}
