package com.tpago.movil.ui.main.products;

import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = MainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
