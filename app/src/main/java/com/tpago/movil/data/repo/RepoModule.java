package com.tpago.movil.data.repo;

import com.tpago.movil.domain.AccountRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public class RepoModule {
  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  AccountRepository provideAccountRepository() {
    return new InMemoryAccountRepository();
  }
}
