package com.mono40.movil.app.ui.main.settings.primaryPaymentMethod;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * Created by solucionesgbh on 5/30/18.
 */

@FragmentScope
@Subcomponent(modules = PrimaryPaymentMethodModule.class)
public interface PrimaryPaymentMethodComponent {

    void inject(PrimaryPaymentMethodFragment fragment);

    void inject(PrimaryPaymentMethodPresenter presenter);

    @Subcomponent.Builder
    interface Builder extends ComponentBuilder<PrimaryPaymentMethodComponent> {

        PrimaryPaymentMethodComponent.Builder primaryPaymentMethodModule(PrimaryPaymentMethodModule module);
    }
}