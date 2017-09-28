package com.tpago.movil.dep.main;

import com.tpago.movil.dep.ActivityModule;
import com.tpago.movil.app.ui.ActivityScope;

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
