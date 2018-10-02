package com.tpago.movil.d.ui.main.recipient.addition.banks;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.bank.BankStore;

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
