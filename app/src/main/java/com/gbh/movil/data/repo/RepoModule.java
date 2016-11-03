package com.gbh.movil.data.repo;

import com.gbh.movil.domain.AccountRepo;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.TransactionRepo;

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
  @Provides
  @Singleton
  AccountRepo provideAccountRepository() {
    return new InMemoryAccountRepo();
  }

  @Provides
  @Singleton
  RecipientRepo provideRecipientRepository() {
    return new InMemoryRecipientRepo();
  }

  @Provides
  @Singleton
  TransactionRepo provideTransactionRepository() {
    return new InMemoryTransactionRepo();
  }
}
