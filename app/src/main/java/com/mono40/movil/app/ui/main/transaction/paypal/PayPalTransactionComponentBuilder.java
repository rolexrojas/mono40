package com.mono40.movil.app.ui.main.transaction.paypal;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.main.transaction.paypal.confirm.PayPalTransactionConfirmComponent;
import com.mono40.movil.app.ui.main.transaction.paypal.confirm.PayPalTransactionConfirmFragment;

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
