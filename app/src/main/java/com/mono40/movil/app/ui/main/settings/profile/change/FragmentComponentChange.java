package com.mono40.movil.app.ui.main.settings.profile.change;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = {
  FragmentModuleComponentBuilderChange.class
})
public interface FragmentComponentChange {

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentChange> {
  }
}
