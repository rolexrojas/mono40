package com.mono40.movil.d.ui.main.recipient.addition.partners;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.partner.PartnerStore;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class PartnerListModule {

  @Provides
  @FragmentScope
  PartnerListPresenter providePresenter(PartnerStore partnerStore) {
    return new PartnerListPresenter(partnerStore);
  }
}
