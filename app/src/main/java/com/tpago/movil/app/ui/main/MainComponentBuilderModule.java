package com.tpago.movil.app.ui.main;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.main.transaction.disburse.index.FragmentComponentDisburseIndex;
import com.tpago.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.tpago.movil.app.ui.main.settings.SettingsComponent;
import com.tpago.movil.app.ui.main.settings.SettingsFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexComponent;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexFragment;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexModule;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryComponent;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
  TransactionSummaryComponent.class,
  FragmentComponentDisburseIndex.class,
  MicroInsuranceIndexComponent.class,
  SettingsComponent.class
})
public abstract class MainComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(TransactionSummaryDialogFragment.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder transactionSummaryComponentBuilder(TransactionSummaryComponent.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(FragmentDisburseIndex.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder disburseComponentBuilder(FragmentComponentDisburseIndex.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(MicroInsuranceIndexFragment.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder microInsuranceIndexComponentBuilder(MicroInsuranceIndexComponent.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(SettingsFragment.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder settingsComponentBuilder(SettingsComponent.Builder builder);
}
