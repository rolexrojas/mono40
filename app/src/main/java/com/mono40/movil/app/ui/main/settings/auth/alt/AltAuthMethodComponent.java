package com.mono40.movil.app.ui.main.settings.auth.alt;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = AltAuthMethodModule.class)
public interface AltAuthMethodComponent {

  void inject(AltAuthMethodFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<AltAuthMethodComponent> {

    Builder altAuthMethodModule(AltAuthMethodModule module);
  }
}
