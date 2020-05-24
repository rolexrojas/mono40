package com.mono40.movil.product.disbursable;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
public interface DisbursableProductStore {

  Completable sync(List<DisbursableProduct> entries);

  Maybe<List<DisbursableProduct>> getAll();

  Completable clear();
}
