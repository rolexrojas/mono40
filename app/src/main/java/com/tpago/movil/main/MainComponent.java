package com.tpago.movil.main;

import com.tpago.movil.app.ActivityModule;
import com.tpago.movil.app.ActivityScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  MainModule.class
})
public interface MainComponent {
}
