package com.tpago.movil.app.ui.init.unlock.ChangePassword;

/**
 * Created by solucionesgbh on 6/12/18.
 */

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

@Module
public final class ChangePasswordModule {
    static ChangePasswordModule create(ChangePasswordPresentation presentation) {
        return new ChangePasswordModule(presentation);
    }

    private final ChangePasswordPresentation presentation;

    private ChangePasswordModule(ChangePasswordPresentation presentation) {
        this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
    }

    @Provides
    @FragmentScope
    ChangePasswordPresenter presenter(
    ) {
        return ChangePasswordPresenter.builder()
                .presentation(this.presentation)
                .build();
    }
}
