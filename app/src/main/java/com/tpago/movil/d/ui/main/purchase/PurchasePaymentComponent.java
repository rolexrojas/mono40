package com.tpago.movil.d.ui.main.purchase;

import com.tpago.movil.app.ui.ChildFragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ChildFragmentScope
@Component(dependencies = PurchaseComponent.class, modules = PurchasePaymentModule.class)
public interface PurchasePaymentComponent {
  void inject(PurchasePaymentDialogFragment fragment);
}
