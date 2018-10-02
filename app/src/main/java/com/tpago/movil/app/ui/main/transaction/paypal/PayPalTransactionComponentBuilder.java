package com.tpago.movil.app.ui.main.transaction.paypal;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.main.transaction.paypal.confirm.PayPalTransactionConfirmComponent;
import com.tpago.movil.app.ui.main.transaction.paypal.confirm.PayPalTransactionConfirmFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(subcomponents = PayPalTransactionConfirmComponent.class)
public abstract class PayPalTransactionComponentBuilder {

  @Binds
  @IntoMap
  @ContainerKey(PayPalTransactionConfirmFragment.class)
  @FragmentScope
  @FragmentQualifier
  public abstract ComponentBuilder confirm(PayPalTransactionConfirmComponent.Builder builder);
}
