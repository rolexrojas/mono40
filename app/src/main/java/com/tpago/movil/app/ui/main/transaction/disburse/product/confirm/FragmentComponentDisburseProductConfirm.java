package com.tpago.movil.app.ui.main.transaction.disburse.product.confirm;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Subcomponent(modules = FragmentModuleDisburseProductConfirm.class)
public interface FragmentComponentDisburseProductConfirm {

  void inject(FragmentDisburseProductConfirm fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentDisburseProductConfirm> {

    Builder confirm(FragmentModuleDisburseProductConfirm module);
  }
}
