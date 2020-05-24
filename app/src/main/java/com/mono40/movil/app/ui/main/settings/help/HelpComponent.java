package com.mono40.movil.app.ui.main.settings.help;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.ui.main.recipient.addition.AddRecipientComponent;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = AddRecipientComponent.class,
        modules = HelpModule.class
)
interface HelpComponent {
    void inject(FragmentHelp fragment);
}
