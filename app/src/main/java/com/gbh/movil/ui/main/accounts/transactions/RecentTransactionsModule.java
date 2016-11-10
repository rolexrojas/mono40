package com.gbh.movil.ui.main.accounts.transactions;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.domain.TransactionRepo;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class RecentTransactionsModule {
  RecentTransactionsModule() {
  }

  @Provides
  @FragmentScope
  TransactionManager provideTransactionManager(TransactionRepo transactionRepo,
    ApiBridge apiBridge) {
    return new TransactionManager(transactionRepo, apiBridge);
  }

  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter(SchedulerProvider schedulerProvider,
    TransactionManager transactionManager) {
    return new RecentTransactionsPresenter(schedulerProvider, transactionManager);
  }
}
