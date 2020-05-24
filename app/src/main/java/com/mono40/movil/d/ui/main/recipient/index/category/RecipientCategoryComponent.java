package com.mono40.movil.d.ui.main.recipient.index.category;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.d.ui.main.DepMainComponent;
import com.mono40.movil.d.ui.main.recipient.index.category.selectcarrier.CarrierSelectFragment;
import com.mono40.movil.dep.ActivityModule;
import com.mono40.movil.dep.AppModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = DepMainComponent.class,
  modules = {ActivityModule.class, RecipientCategoryModule.class}
)
interface RecipientCategoryComponent {

  void inject(RecipientCategoryFragment fragment);

  void inject(CarrierSelectFragment fragment);

  CompanyHelper logoFactory();

  PartnerStore partnerStore();
}
