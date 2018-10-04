package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

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
