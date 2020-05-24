package com.mono40.movil.d.ui.main.recipient.addition.banks;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.bank.BankStore;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class BankListModule {

  @Provides
  @FragmentScope
  BankListPresenter providePresenter(BankStore bankStore) {
    return BankListPresenter.create(bankStore);
  }
}
