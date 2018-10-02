package com.tpago.movil.app;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.FragmentActivityComponent;
import com.tpago.movil.app.ui.activity.toolbar.ActivityComponentToolbar;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.main.MainComponent;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.tpago.movil.d.ui.main.DepMainActivityBase;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(subcomponents = {
  ActivityComponentToolbar.class,
  MicroInsurancePurchaseComponent.class,
  MainComponent.class,
  FragmentActivityComponent.class
})
public abstract class AppComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(ActivityToolbar.class)
  public abstract ComponentBuilder activityToolbar(ActivityComponentToolbar.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(MicroInsurancePurchaseActivity.class)
  public abstract ComponentBuilder microInsurancePurchase(MicroInsurancePurchaseComponent.Builder builder);

  @Deprecated
  @Binds
  @IntoMap
  @ContainerKey(DepMainActivityBase.class)
  public abstract ComponentBuilder mainActivityComponentBuilder(
    MainComponent.Builder builder
  );

  @Deprecated
  @Binds
  @IntoMap
  @ContainerKey(FragmentActivityBase.class)
  public abstract ComponentBuilder fragmentActivityComponentBuidler(
    FragmentActivityComponent.Builder builder
  );
}
