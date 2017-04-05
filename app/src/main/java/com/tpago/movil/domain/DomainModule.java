package com.tpago.movil.domain;

import com.tpago.movil.domain.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class DomainModule {
  @Provides
  @Singleton
  BankProvider provideBankProvider(
    BankRepo bankRepo,
    NetworkService networkService,
    ApiService apiService) {
    return new BankProvider(bankRepo, networkService, apiService);
  }
}
