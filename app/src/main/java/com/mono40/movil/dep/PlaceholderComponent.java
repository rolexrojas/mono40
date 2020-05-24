package com.mono40.movil.dep;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

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
