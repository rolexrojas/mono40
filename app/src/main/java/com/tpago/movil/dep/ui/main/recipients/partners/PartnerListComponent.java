package com.tpago.movil.dep.ui.main.recipients.partners;

import com.tpago.movil.dep.ui.FragmentScope;
import com.tpago.movil.dep.ui.main.recipients.AddRecipientComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = AddRecipientComponent.class, modules = PartnerListModule.class)
interface PartnerListComponent {
  void inject(PartnerListFragment fragment);
}
