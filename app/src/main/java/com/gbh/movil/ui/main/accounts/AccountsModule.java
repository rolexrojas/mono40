package com.gbh.movil.ui.main.accounts;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.EventBus;
import com.gbh.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class AccountsModule {
  AccountsModule() {
  }

  @Provides
  @FragmentScope
  AccountsPresenter providePresenter(SchedulerProvider schedulerProvider, EventBus eventBus,
    AccountManager accountManager, BalanceManager balanceManager) {
    return new AccountsPresenter(schedulerProvider, eventBus, accountManager, balanceManager);
  }
}
