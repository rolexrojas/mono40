package com.gbh.movil.data.repo;

import com.gbh.movil.domain.AccountRepository;

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
