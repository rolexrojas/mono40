package com.mono40.movil.d.ui.main.products.transactions;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.domain.DecoratedTransactionProvider;
import com.mono40.movil.d.domain.TransactionProvider;
import com.mono40.movil.d.domain.api.DepApiBridge;

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
  @ActivityScope
  TransactionProvider provideTransactionManager(DepApiBridge apiBridge) {
    return new DecoratedTransactionProvider(apiBridge);
  }

  @Provides
  @ActivityScope
  RecentTransactionsPresenter providePresenter(
    SchedulerProvider schedulerProvider,
    TransactionProvider transactionProvider
  ) {
    return new RecentTransactionsPresenter(schedulerProvider, transactionProvider);
  }
}
