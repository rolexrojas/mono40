package com.mono40.movil.product.disbursable;

import com.mono40.movil.util.ObjectHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class DisbursableProductStoreMemoized implements DisbursableProductStore {

  static DisbursableProductStoreMemoized create() {
    return new DisbursableProductStoreMemoized();
  }

  private final AtomicReference<List<DisbursableProduct>> entries = new AtomicReference<>();

  private DisbursableProductStoreMemoized() {
  }

  @Override
  public Completable sync(List<DisbursableProduct> entries) {
    return Observable.fromIterable(ObjectHelper.checkNotNull(entries, "entries"))
      .toSortedList(DisbursableProduct::compareTo)
      .doOnSuccess(this.entries::set)
      .toCompletable();
  }

  @Override
  public Maybe<List<DisbursableProduct>> getAll() {
    return Maybe.defer(() -> {
      final List<DisbursableProduct> reference = this.entries.get();
      if (ObjectHelper.isNull(reference) || reference.isEmpty()) {
        return Maybe.empty();
      }
      return Maybe.just(reference);
    });
  }

  @Override
  public Completable clear() {
    return Completable.fromAction(() -> this.entries.set(null));
  }
}
