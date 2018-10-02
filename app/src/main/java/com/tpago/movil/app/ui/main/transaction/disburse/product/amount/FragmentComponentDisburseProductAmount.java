package com.tpago.movil.app.ui.main.transaction.disburse.product.amount;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;

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
