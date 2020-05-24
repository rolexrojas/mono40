package com.mono40.movil.app.ui.main.transaction.disburse.index;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = FragmentModuleDisburseIndex.class)
public interface FragmentComponentDisburseIndex {

  void inject(FragmentDisburseIndex fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentDisburseIndex> {

    Builder disburseModule(FragmentModuleDisburseIndex module);
  }
}
