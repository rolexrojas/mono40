package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.DecoratedTransactionProvider;
import com.tpago.movil.d.domain.TransactionProvider;
import com.tpago.movil.d.domain.TransactionRepo;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;

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
