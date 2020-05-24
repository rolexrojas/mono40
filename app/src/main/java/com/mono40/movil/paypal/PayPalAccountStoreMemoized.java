package com.mono40.movil.paypal;

import com.mono40.movil.util.ObjectHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * {@link PayPalAccountStore} implementation that uses memory as storage
 */
final class PayPalAccountStoreMemoized implements PayPalAccountStore {

  static PayPalAccountStoreMemoized create() {
    return new PayPalAccountStoreMemoized();
  }

  private final AtomicReference<List<PayPalAccount>> entries = new AtomicReference<>();

  private PayPalAccountStoreMemoized() {
  }

  @Override
  public Completable sync(List<PayPalAccount> entries) {
    return Observable.fromIterable(ObjectHelper.checkNotNull(entries, "entries"))
      .toSortedList(PayPalAccount::compareTo)
      .doOnSuccess(this.entries::set)
      .toCompletable();
  }

  @Override
  public Maybe<List<PayPalAccount>> getAll() {
    return Maybe.defer(() -> {
      final List<PayPalAccount> reference = this.entries.get();
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
