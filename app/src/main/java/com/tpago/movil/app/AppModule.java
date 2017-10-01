package com.tpago.movil.app;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

  @Provides
  @Singleton
  ComponentBuilderSupplier componentBuilderSupplier(
    Map<Class<?>, ComponentBuilder> componentBuilderMap
  ) {
    return ComponentBuilderSupplier.create(componentBuilderMap);
  }
}
