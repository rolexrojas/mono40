package com.mono40.movil.app.ui.main.transaction.disburse.product;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

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
