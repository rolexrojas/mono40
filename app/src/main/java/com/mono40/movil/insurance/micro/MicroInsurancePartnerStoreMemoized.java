package com.mono40.movil.insurance.micro;

import com.mono40.movil.util.ObjectHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

final class MicroInsurancePartnerStoreMemoized implements MicroInsurancePartnerStore {

  static MicroInsurancePartnerStoreMemoized create() {
    return new MicroInsurancePartnerStoreMemoized();
  }

  private final AtomicReference<List<MicroInsurancePartner>> entries = new AtomicReference<>();

  private MicroInsurancePartnerStoreMemoized() {
  }

  @Override
  public Completable sync(List<MicroInsurancePartner> entries) {
    return Observable.fromIterable(ObjectHelper.checkNotNull(entries, "entries"))
      .toSortedList(MicroInsurancePartner::compareTo)
      .doOnSuccess(this.entries::set)
      .toCompletable();
  }

  @Override
  public Maybe<List<MicroInsurancePartner>> getAll() {
    return Maybe.defer(() -> {
      final List<MicroInsurancePartner> reference = this.entries.get();
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
