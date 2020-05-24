package com.mono40.movil.app.ui.main.transaction.disburse.product.amount;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Subcomponent(modules = FragmentModuleDisburseProductAmount.class)
public interface FragmentComponentDisburseProductAmount {

  void inject(FragmentDisburseProductAmount fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentDisburseProductAmount> {

    Builder amount(FragmentModuleDisburseProductAmount module);
  }
}
