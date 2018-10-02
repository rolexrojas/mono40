package com.tpago.movil.app.ui.main.transaction.paypal;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.paypal.PayPalAccount;
import com.tpago.movil.util.ObjectHelper;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
public final class PayPalTransactionModule {

  static PayPalTransactionModule create(PayPalAccount recipient) {
    return new PayPalTransactionModule(recipient);
  }

  private final PayPalAccount recipient;

  private PayPalTransactionModule(PayPalAccount recipient) {
    this.recipient = ObjectHelper.checkNotNull(recipient, "recipient");
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  ComponentBuilderSupplier componentBuilderSupplier(@FragmentQualifier Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }

  @Provides
  @FragmentScope
  PayPalAccount recipient() {
    return this.recipient;
  }
}
