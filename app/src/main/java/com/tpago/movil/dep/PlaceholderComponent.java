package com.tpago.movil.dep;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

@Deprecated
@FragmentScope
@Subcomponent(modules = PlaceholderModule.class)
public interface PlaceholderComponent {

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<PlaceholderComponent> {

    Builder placeholderModule(PlaceholderModule module);
  }
}
