package com.tpago.movil.app;

import com.tpago.movil.data.DataModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    DataModule.class,
    com.tpago.movil.dep.AppModule.class
  }
)
public interface AppComponent extends com.tpago.movil.dep.AppComponent {
}
