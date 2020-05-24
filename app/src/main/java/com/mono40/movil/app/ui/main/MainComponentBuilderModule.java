package com.mono40.movil.app.ui.main;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.main.transaction.disburse.index.FragmentComponentDisburseIndex;
import com.mono40.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.mono40.movil.app.ui.main.settings.SettingsComponent;
import com.mono40.movil.app.ui.main.settings.SettingsFragment;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexComponent;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexFragment;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.index.MicroInsuranceIndexModule;
import com.mono40.movil.app.ui.main.transaction.summary.TransactionSummaryComponent;
import com.mono40.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment;

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
