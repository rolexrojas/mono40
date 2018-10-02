package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

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
