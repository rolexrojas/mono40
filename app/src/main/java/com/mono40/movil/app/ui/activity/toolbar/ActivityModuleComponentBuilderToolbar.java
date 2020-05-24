package com.mono40.movil.app.ui.activity.toolbar;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.main.transaction.disburse.product.FragmentComponentDisburseProduct;
import com.mono40.movil.app.ui.main.transaction.disburse.product.FragmentDisburseProduct;
import com.mono40.movil.app.ui.main.settings.profile.change.FragmentChange;
import com.mono40.movil.app.ui.main.settings.profile.change.FragmentComponentChange;
import com.mono40.movil.app.ui.main.transaction.paypal.PayPalTransactionComponent;
import com.mono40.movil.app.ui.main.transaction.paypal.PayPalTransactionFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(subcomponents = {
  PayPalTransactionComponent.class,
  FragmentComponentDisburseProduct.class,
  FragmentComponentChange.class
})
public abstract class ActivityModuleComponentBuilderToolbar {

  @Binds
  @IntoMap
  @ContainerKey(PayPalTransactionFragment.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder payPalTransaction(PayPalTransactionComponent.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(FragmentDisburseProduct.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder disburseProduct(FragmentComponentDisburseProduct.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(FragmentChange.class)
  @ActivityScope
  @ActivityQualifier
  public abstract ComponentBuilder settingsProfileChange(FragmentComponentChange.Builder builder);
}
