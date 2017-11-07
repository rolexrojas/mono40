package com.tpago.movil.bank;

import com.tpago.movil.api.Api;
import com.tpago.movil.store.Store;
import com.tpago.movil.time.Clock;
import com.tpago.movil.time.OneOrMoreDaysExpirationPredicate;

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
    OneOrMoreDaysExpirationPredicate expirationPredicate,
    Store store
  ) {
    final BankListSupplier supplier = ApiBankListSupplier.create(api);
    return StoreBankListSupplier.builder()
      .clock(clock)
      .expirationPredicate(expirationPredicate)
      .store(store)
      .supplier(supplier)
      .build();
  }

  @Provides
  @Singleton
  CodeToBankMapper codeToBankMapper(BankListSupplier supplier) {
    return CodeToBankMapper.create(supplier);
  }
}
