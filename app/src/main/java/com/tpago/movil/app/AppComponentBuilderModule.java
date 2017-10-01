package com.tpago.movil.app;

import com.tpago.movil.app.ui.FragmentActivity;
import com.tpago.movil.app.ui.FragmentActivityComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(
  subcomponents = {
    FragmentActivityComponent.class
  }
)
public abstract class AppComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(FragmentActivity.class)
  public abstract ComponentBuilder fragmentComponentBuidler(
    FragmentActivityComponent.Builder builder
  );
}
