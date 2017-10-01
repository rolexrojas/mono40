package com.tpago.movil.app;

import com.tpago.movil.data.DataModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    AppComponentBuilderModule.class,
    AppModule.class,
    DataModule.class,
    com.tpago.movil.dep.AppModule.class
  }
)
public interface AppComponent extends com.tpago.movil.dep.AppComponent {
}
