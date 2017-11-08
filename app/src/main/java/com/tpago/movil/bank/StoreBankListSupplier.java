package com.tpago.movil.bank;

import com.tpago.movil.store.Store;
import com.tpago.movil.time.Clock;
import com.tpago.movil.time.TimePredicate;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class StoreBankListSupplier implements BankListSupplier {

  private static String createStoreKey(String s) {
    return String.format("StoreBankListSupplier.%1$s", s);
  }

  private static final String STORE_KEY_DATA = createStoreKey("data");

  static Builder builder() {
    return new Builder();
  }

  private final Clock clock;
  private final Store store;
  private final BankListSupplier supplier;
  private final TimePredicate timePredicate;

  private StoreBankListSupplier(Builder builder) {
    this.clock = builder.clock;
    this.store = builder.store;
    this.supplier = builder.supplier;
    this.timePredicate = builder.timePredicate;
  }

  private void saveData(List<Bank> value) {
    final BankList data = BankList.builder()
      .queryTime(this.clock.getTime())
      .value(value)
      .build();
    this.store.set(STORE_KEY_DATA, data);
  }

  private Single<List<Bank>> getData() {
    BankList data = this.store.get(STORE_KEY_DATA, BankList.class);
    if (ObjectHelper.isNull(data) || this.timePredicate.test(data.queryTime())) {
      return this.supplier.get()
        .doOnSuccess(this::saveData);
    } else {
      return Single.just(data.value());
    }
  }

  @Override
  public Single<List<Bank>> get() {
    return Single.defer(this::getData);
  }

  static final class Builder {

    private Clock clock;
    private TimePredicate timePredicate;
    private Store store;
    private BankListSupplier supplier;

    private Builder() {
    }

    final Builder clock(Clock clock) {
      this.clock = ObjectHelper.checkNotNull(clock, "clock");
      return this;
    }

    final Builder store(Store store) {
      this.store = ObjectHelper.checkNotNull(store, "store");
      return this;
    }

    final Builder supplier(BankListSupplier supplier) {
      this.supplier = ObjectHelper.checkNotNull(supplier, "supplier");
      return this;
    }

    final Builder timePredicate(TimePredicate timePredicate) {
      this.timePredicate = ObjectHelper.checkNotNull(timePredicate, "timePredicate");
      return this;
    }

    final StoreBankListSupplier build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("clock", ObjectHelper.isNull(this.clock))
        .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
        .addPropertyNameIfMissing("supplier", ObjectHelper.isNull(this.supplier))
        .addPropertyNameIfMissing("timePredicate", ObjectHelper.isNull(this.timePredicate))
        .checkNoMissingProperties();
      return new StoreBankListSupplier(this);
    }
  }
}
