package com.tpago.movil.app;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.FragmentActivity;
import com.tpago.movil.app.ui.FragmentActivityComponent;
import com.tpago.movil.app.ui.main.MainComponent;
import com.tpago.movil.d.ui.main.DepMainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(
  subcomponents = {
    MainComponent.class,
    FragmentActivityComponent.class
  }
)
public abstract class AppComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(DepMainActivity.class)
  public abstract ComponentBuilder mainActivityComponentBuilder(
    MainComponent.Builder builder
  );

  @Binds
  @IntoMap
  @ContainerKey(FragmentActivity.class)
  public abstract ComponentBuilder fragmentActivityComponentBuidler(
    FragmentActivityComponent.Builder builder
  );
}
