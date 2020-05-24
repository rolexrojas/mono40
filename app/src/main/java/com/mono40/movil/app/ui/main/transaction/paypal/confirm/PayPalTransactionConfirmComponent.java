package com.mono40.movil.app.ui.main.transaction.paypal.confirm;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

@FragmentScopeChild
@Subcomponent(modules = PayPalTransactionConfirmModule.class)
public interface PayPalTransactionConfirmComponent {

  void inject(PayPalTransactionConfirmFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<PayPalTransactionConfirmComponent> {

    Builder payPalTransactionConfirmModule(PayPalTransactionConfirmModule module);
  }
}
