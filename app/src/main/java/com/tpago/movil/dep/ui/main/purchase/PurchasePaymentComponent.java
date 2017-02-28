package com.tpago.movil.dep.ui.main.purchase;

import com.tpago.movil.dep.ui.ChildFragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ChildFragmentScope
@Component(dependencies = PurchaseComponent.class, modules = PurchasePaymentModule.class)
public interface PurchasePaymentComponent {
  void inject(PurchasePaymentDialogFragment fragment);
}
