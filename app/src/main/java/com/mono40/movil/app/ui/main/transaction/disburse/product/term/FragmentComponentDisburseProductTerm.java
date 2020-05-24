package com.mono40.movil.app.ui.main.transaction.disburse.product.term;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Subcomponent(modules = FragmentModuleDisburseProductTerm.class)
public interface FragmentComponentDisburseProductTerm {

  void inject(FragmentDisburseProductTerm fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentDisburseProductTerm> {

    Builder term(FragmentModuleDisburseProductTerm module);
  }
}
