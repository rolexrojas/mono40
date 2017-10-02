package com.tpago.movil.app;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

  @Provides
  @Singleton
  ComponentBuilderSupplier componentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }
}
