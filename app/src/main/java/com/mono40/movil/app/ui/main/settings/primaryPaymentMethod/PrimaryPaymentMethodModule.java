package com.mono40.movil.app.ui.main.settings.primaryPaymentMethod;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by solucionesgbh on 5/30/18.
 */
@Module
public final class PrimaryPaymentMethodModule {

    static PrimaryPaymentMethodModule create(PrimaryPaymentMethodPresentation presentation) {
        return new PrimaryPaymentMethodModule(presentation);
    }

    private final PrimaryPaymentMethodPresentation presentation;

    private PrimaryPaymentMethodModule(PrimaryPaymentMethodPresentation presentation) {
        this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
    }

    @Provides
    @FragmentScope
    PrimaryPaymentMethodPresenter presenter(
    ) {
        return PrimaryPaymentMethodPresenter.builder()
                .presentation(this.presentation)
                .build();
    }
}
