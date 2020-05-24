package com.mono40.movil.d.ui.main.recipient.index.category.selectcarrier;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.d.ui.main.DepMainComponent;

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
