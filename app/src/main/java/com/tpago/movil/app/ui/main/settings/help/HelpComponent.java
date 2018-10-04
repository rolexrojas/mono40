package com.tpago.movil.app.ui.main.settings.help;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientComponent;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = AddRecipientComponent.class,
        modules = HelpModule.class
)
interface HelpComponent {
    void inject(FragmentHelp fragment);
}
