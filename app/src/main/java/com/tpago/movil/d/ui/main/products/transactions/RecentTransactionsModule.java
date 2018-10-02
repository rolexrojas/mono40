package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.DecoratedTransactionProvider;
import com.tpago.movil.d.domain.TransactionProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;

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
