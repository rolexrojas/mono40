package com.tpago.movil.app.ui.main.settings.profile.change;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

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
