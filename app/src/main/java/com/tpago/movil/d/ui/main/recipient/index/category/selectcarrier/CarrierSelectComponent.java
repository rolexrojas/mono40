package com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.main.DepMainComponent;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = DepMainComponent.class,
        modules = CarrierSelectModule.class
)
public interface CarrierSelectComponent {
    void inject(CarrierSelectFragment fragment);

    CompanyHelper logoFactory();

    PartnerStore partnerStore();
}
