package com.mono40.movil.d.ui.main.purchase;

import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Component(dependencies = PurchaseComponent.class, modules = PurchasePaymentModule.class)
public interface PurchasePaymentComponent {
  void inject(PurchasePaymentDialogFragment fragment);
}
