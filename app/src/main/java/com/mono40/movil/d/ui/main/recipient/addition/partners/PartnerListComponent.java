package com.mono40.movil.d.ui.main.recipient.addition.partners;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.ui.main.recipient.addition.AddRecipientComponent;

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
