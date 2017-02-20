package com.tpago.movil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  UserStore provideUserStore();
}
