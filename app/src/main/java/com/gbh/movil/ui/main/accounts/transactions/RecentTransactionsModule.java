package com.gbh.movil.ui.main.accounts.transactions;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.DecoratedTransactionProvider;
import com.gbh.movil.domain.TransactionProvider;
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
  TransactionProvider provideTransactionManager(TransactionRepo transactionRepo,
    ApiBridge apiBridge) {
    return new DecoratedTransactionProvider(transactionRepo, apiBridge);
  }

  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter(SchedulerProvider schedulerProvider,
    TransactionProvider transactionProvider) {
    return new RecentTransactionsPresenter(schedulerProvider, transactionProvider);
  }
}
