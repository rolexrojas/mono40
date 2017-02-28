package com.tpago.movil.dep.ui.main.products;

import com.tpago.movil.dep.ui.FragmentScope;
import com.tpago.movil.dep.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = MainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
