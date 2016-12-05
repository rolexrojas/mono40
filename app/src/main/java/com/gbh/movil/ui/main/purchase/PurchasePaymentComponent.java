package com.gbh.movil.ui.main.purchase;

import com.gbh.movil.ui.ChildFragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ChildFragmentScope
@Component(dependencies = PurchaseComponent.class, modules = PurchasePaymentModule.class)
public interface PurchasePaymentComponent {
  void inject(PurchasePaymentDialogFragment fragment);
}
