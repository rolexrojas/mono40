package com.tpago.movil.bank;

import com.tpago.movil.api.Api;
import com.tpago.movil.store.Store;
import com.tpago.movil.time.Clock;
import com.tpago.movil.time.OneOrMoreDaysTimePredicate;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class BankModule {

  @Provides
  @Singleton
  BankListSupplier bankListSupplier(
    Api api,
    Clock clock,
    Store store,
    OneOrMoreDaysTimePredicate timePredicate
  ) {
    final BankListSupplier supplier = ApiBankListSupplier.create(api);
    return StoreBankListSupplier.builder()
      .clock(clock)
      .store(store)
      .supplier(supplier)
      .timePredicate(timePredicate)
      .build();
  }

  @Provides
  @Singleton
  CodeToBankMapper codeToBankMapper(BankListSupplier supplier) {
    return CodeToBankMapper.create(supplier);
  }
}
