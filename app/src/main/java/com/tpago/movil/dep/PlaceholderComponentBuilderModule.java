package com.tpago.movil.dep;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.util.Placeholder;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Deprecated
@Module(subcomponents = PlaceholderComponent.class)
public abstract class PlaceholderComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(Placeholder.class)
  public abstract ComponentBuilder placeholderComponentBuilder(
    PlaceholderComponent.Builder builder
  );
}
