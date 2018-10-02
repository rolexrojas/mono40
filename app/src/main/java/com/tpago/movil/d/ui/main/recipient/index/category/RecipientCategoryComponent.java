package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = DepMainComponent.class,
  modules = RecipientCategoryModule.class
)
interface RecipientCategoryComponent {

  void inject(RecipientCategoryFragment fragment);

  CompanyHelper logoFactory();

  PartnerStore partnerStore();
}
