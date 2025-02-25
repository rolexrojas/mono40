package com.tpago.movil.d.ui.main.recipient.addition.partners;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = AddRecipientComponent.class,
  modules = PartnerListModule.class
)
interface PartnerListComponent {

  void inject(PartnerListFragment fragment);

  CompanyHelper logoFactory();
}
