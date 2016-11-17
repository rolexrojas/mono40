package com.gbh.movil.data.repo;

import com.gbh.movil.domain.ProductRepo;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.TransactionRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class RepoModule {
  @Provides
  @Singleton
  ProductRepo provideAccountRepository() {
    return new InMemoryProductRepo();
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
