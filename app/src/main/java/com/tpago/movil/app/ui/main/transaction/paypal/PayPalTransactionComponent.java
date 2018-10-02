package com.tpago.movil.app.ui.main.transaction.paypal;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
  PayPalTransactionModule.class,
  PayPalTransactionComponentBuilder.class
})
public interface PayPalTransactionComponent {

  void inject(PayPalTransactionFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<PayPalTransactionComponent> {

    Builder payPalTransactionModule(PayPalTransactionModule module);
  }
}
