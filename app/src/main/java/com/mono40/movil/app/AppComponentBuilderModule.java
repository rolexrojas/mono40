package com.mono40.movil.app;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.FragmentActivityBase;
import com.mono40.movil.app.ui.FragmentActivityComponent;
import com.mono40.movil.app.ui.activity.toolbar.ActivityComponentToolbar;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.main.MainComponent;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.mono40.movil.d.ui.main.DepMainActivityBase;

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
