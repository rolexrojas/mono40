package com.mono40.movil.dep;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.util.Placeholder;

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
