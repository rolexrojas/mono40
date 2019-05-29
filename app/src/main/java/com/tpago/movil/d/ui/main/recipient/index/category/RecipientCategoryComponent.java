package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.main.DepMainComponent;
import com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier.CarrierSelectFragment;
import com.tpago.movil.dep.ActivityModule;
import com.tpago.movil.dep.AppModule;

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
