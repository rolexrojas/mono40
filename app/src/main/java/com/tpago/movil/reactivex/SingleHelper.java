package com.tpago.movil.reactivex;

import com.tpago.movil.util.ObjectHelper;

import java.util.function.Supplier;

import io.reactivex.Single;

/**
 * Collections of helper for {@link Single single streams}.
 *
 * @author hecvasro
 */
public final class SingleHelper {

  public static <T> Single<T> defer(Supplier<T> supplier) {
    ObjectHelper.checkNotNull(supplier, "supplier");

    return Single.defer(() -> Single.just(supplier.get()));
  }

  private SingleHelper() {
  }
}
