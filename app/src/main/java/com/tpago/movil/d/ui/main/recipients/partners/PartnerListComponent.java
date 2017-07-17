package com.tpago.movil.d.ui.main.recipients.partners;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.ui.main.recipients.AddRecipientComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = AddRecipientComponent.class, modules = PartnerListModule.class)
interface PartnerListComponent {
  void inject(PartnerListFragment fragment);
}
