package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public interface ProductProvider {
  /**
   * Gets all the {@link Product products} registered in the provider.
   * <p>
   * By default the resulting {@link List list} is ordered in the order that they were registered.
   *
   * @return All the {@link Product products} registered in the provider.
   */
  @NonNull
  Observable<List<Product>> getAll();
}
