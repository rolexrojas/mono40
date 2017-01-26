package com.tpago.movil.ui.main.products.transactions;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.DecoratedTransactionProvider;
import com.tpago.movil.domain.TransactionProvider;
import com.tpago.movil.domain.TransactionRepo;
import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.FragmentScope;

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
    ApiBridge apiBridge, SessionManager sessionManager) {
    return new DecoratedTransactionProvider(transactionRepo, apiBridge, sessionManager);
  }

  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter(SchedulerProvider schedulerProvider,
    TransactionProvider transactionProvider) {
    return new RecentTransactionsPresenter(schedulerProvider, transactionProvider);
  }
}
