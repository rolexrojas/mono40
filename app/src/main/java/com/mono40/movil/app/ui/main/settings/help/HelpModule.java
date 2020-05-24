package com.mono40.movil.app.ui.main.settings.help;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.bank.BankStore;

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
