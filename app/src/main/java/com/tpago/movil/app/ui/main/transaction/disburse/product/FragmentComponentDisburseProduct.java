package com.tpago.movil.app.ui.main.transaction.disburse.product;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = {
  FragmentModuleDisburseProduct.class,
  FragmentModuleComponentBuilderDisburseProduct.class
})
public interface FragmentComponentDisburseProduct {

  void inject(FragmentDisburseProduct fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentDisburseProduct> {

    Builder disburseProductModule(FragmentModuleDisburseProduct module);
  }
}
