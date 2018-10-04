package com.tpago.movil.app.ui.main.settings.help;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.bank.BankStore;

import dagger.Module;
import dagger.Provides;

/**
 * @author r.suazo
 */
@Module
class HelpModule {

    @Provides
    @FragmentScope
    HelpPresenter providePresenter(BankStore bankStore) {
        return HelpPresenter.create(bankStore);
    }

}
