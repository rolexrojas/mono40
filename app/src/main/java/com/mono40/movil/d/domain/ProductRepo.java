package com.mono40.movil.d.domain;

import androidx.annotation.NonNull;

import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public interface ProductRepo extends ProductProvider {
  @NonNull Observable<Product> save(@NonNull Product product);
  @NonNull Observable<Product> remove(@NonNull Product product);
  void clear();
}
