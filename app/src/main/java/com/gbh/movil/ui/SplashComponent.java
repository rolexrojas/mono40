package com.gbh.movil.ui;

import com.gbh.movil.AppComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
interface SplashComponent {
  void inject(SplashActivity activity);
}
