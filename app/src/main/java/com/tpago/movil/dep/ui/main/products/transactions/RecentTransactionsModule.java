package com.tpago.movil.dep.ui.main.products.transactions;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.DecoratedTransactionProvider;
import com.tpago.movil.dep.domain.TransactionProvider;
import com.tpago.movil.dep.domain.TransactionRepo;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;

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
    DepApiBridge apiBridge, SessionManager sessionManager) {
    return new DecoratedTransactionProvider(transactionRepo, apiBridge, sessionManager);
  }

  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter(SchedulerProvider schedulerProvider,
    TransactionProvider transactionProvider) {
    return new RecentTransactionsPresenter(schedulerProvider, transactionProvider);
  }
}
