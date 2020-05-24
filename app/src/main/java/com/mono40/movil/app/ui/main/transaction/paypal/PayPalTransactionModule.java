package com.mono40.movil.app.ui.main.transaction.paypal;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.util.ObjectHelper;

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
